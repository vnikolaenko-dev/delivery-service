package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.model.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.OrderProduct;
import ru.don_polesie.back_end.repository.OrderProductRepository;
import ru.don_polesie.back_end.repository.OrderRepository;
import ru.don_polesie.back_end.service.inf.WorkerOrderService;
import ru.don_polesie.back_end.service.impl.order.PriceService;
import ru.don_polesie.back_end.service.impl.system.YooKassaServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerOrderServiceImpl implements WorkerOrderService {
    private static final int PAGE_SIZE = 10;

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final YooKassaServiceImpl yooKassaService;
    private final PriceService priceService;


    @Override
    public OrderDtoRR findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Order not found"));
        return orderMapper.toOrderDtoRR(order);
    }

    /**
     * Получает страницу заказов
     *
     * @param pageNumber номер страницы (начинается с 1)
     * @return страница заказов в формате DTO
     */
    @Override
    public Page<OrderDtoRR> findOrdersPage(Integer pageNumber) {
        var pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("id").descending());
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderDtoRR);
    }

    /**
     * Получает страницу заказов со статусом RESERVAITED
     *
     * @param pageNumber номер страницы (начинается с 1)
     * @return страница заказов в формате DTO
     */
    @Override
    public Page<OrderDtoRR> findMoneyReservaitedOrdersPage(Integer pageNumber) {
        var pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("id").descending());
        return orderRepository.findAllMoneyReservaited(pageable)
                .map(orderMapper::toOrderDtoRR);
    }

    /**
     * Отмечает заказ как отправленный
     *
     * @param id идентификатор заказа
     * @throws ObjectNotFoundException если заказ не найден
     */
    @Override
    @Transactional
    public void markShipped(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    /**
     * Обрабатывает заказ: обновляет веса товаров и пересчитывает стоимость
     *
     * @param id идентификатор заказа
     * @param request запрос с весами для весовых товаров
     * @throws ObjectNotFoundException если заказ не найден
     * @throws IllegalArgumentException если не указан вес для весового товара
     */
    @Override
    @Transactional
    public void processOrder(Long id, ProcessWeightsRequest request) {
        Order order = getOrderById(id);
        Map<Long, ProcessWeightsRequest.WeightDto> weightMap = createWeightMap(request);

        BigDecimal newTotal = processOrderProducts(order, weightMap);
        updateOrderPayment(order, newTotal);

        orderRepository.save(order);
    }

    /**
     * Получает общее количество продаж товара за указанную дату
     *
     * @param productId идентификатор товара
     * @param date дата для анализа продаж
     * @return общее количество проданного товара
     */
    @Override
    public Long getTotalSalesForProductByDate(Long productId, Instant date) {
        return orderRepository.getTotalSalesForProductByDate(productId, date);
    }

    /**
     * Получает количество заказов с указанным товаром за дату
     *
     * @param productId идентификатор товара
     * @param date дата для анализа
     * @return количество заказов
     */
    @Override
    public Long getOrderCountForProductByDate(Long productId, Instant date) {
        return orderRepository.getOrderCountForProductByDate(productId, date);
    }

    // ========== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    /**
     * Находит заказ по ID или выбрасывает исключение
     *
     * @param id идентификатор заказа
     * @return найденный заказ
     * @throws ObjectNotFoundException если заказ не найден
     */
    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found with id: " + id));
    }

    /**
     * Создает карту весов товаров для быстрого доступа
     *
     * @param request запрос с весами товаров
     * @return карта: productId -> WeightDto
     */
    private Map<Long, ProcessWeightsRequest.WeightDto> createWeightMap(ProcessWeightsRequest request) {
        return request.getWeights().stream()
                .collect(Collectors.toMap(
                        ProcessWeightsRequest.WeightDto::getProductId,
                        weight -> weight
                ));
    }

    /**
     * Обрабатывает все товары в заказе и вычисляет новую общую стоимость
     *
     * @param order заказ для обработки
     * @param weightMap карта весов товаров
     * @return новая общая стоимость заказа
     */
    private BigDecimal processOrderProducts(Order order, Map<Long, ProcessWeightsRequest.WeightDto> weightMap) {
        return order.getOrderProducts().stream()
                .map(orderProduct -> processOrderProduct(orderProduct, weightMap))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Обрабатывает один товар в заказе: обновляет количество и рассчитывает стоимость
     *
     * @param orderProduct товар в заказе
     * @param weightMap карта весов товаров
     * @return стоимость товара после обработки
     */
    private BigDecimal processOrderProduct(OrderProduct orderProduct, Map<Long, ProcessWeightsRequest.WeightDto> weightMap) {
        int quantityGrams = getProductQuantity(orderProduct, weightMap);
        updateOrderProductQuantity(orderProduct, quantityGrams);

        return calculateProductCost(orderProduct, quantityGrams);
    }

    /**
     * Получает количество товара с учетом типа (весовой/штучный)
     *
     * @param orderProduct товар в заказе
     * @param weightMap карта весов товаров
     * @return количество в граммах (для весового) или штуках (для штучного)
     * @throws IllegalArgumentException если не указан вес для весового товара
     */
    private int getProductQuantity(OrderProduct orderProduct, Map<Long, ProcessWeightsRequest.WeightDto> weightMap) {
        if (Boolean.TRUE.equals(orderProduct.getProduct().getIsWeighted())) {
            ProcessWeightsRequest.WeightDto weightDto = weightMap.get(orderProduct.getProduct().getId());
            if (weightDto == null) {
                throw new IllegalArgumentException(
                        "Weight not provided for weighted product with id: " + orderProduct.getProduct().getId()
                );
            }
            return weightDto.getWeight();
        }
        return orderProduct.getQuantity();
    }

    /**
     * Обновляет количество товара в заказе
     *
     * @param orderProduct товар в заказе
     * @param quantity новое количество
     */
    private void updateOrderProductQuantity(OrderProduct orderProduct, int quantity) {
        orderProduct.setQuantity(quantity);
        orderProductRepository.save(orderProduct);
    }

    /**
     * Рассчитывает стоимость товара
     *
     * @param orderProduct товар в заказе
     * @param quantity количество товара
     * @return стоимость товара
     */
    private BigDecimal calculateProductCost(OrderProduct orderProduct, int quantity) {
        return priceService.calculateProductCost(orderProduct.getProduct(), quantity);
    }

    /**
     * Обновляет платеж заказа: устанавливает новую сумму и обрабатывает платеж
     *
     * @param order заказ для обновления
     * @param newTotal новая общая стоимость заказа
     */
    private void updateOrderPayment(Order order, BigDecimal newTotal) {
        order.setTotalAmount(newTotal);

        try {
            yooKassaService.getMoney(order);
            order.setStatus(OrderStatus.READY_FOR_DELIVERY);
        } catch (Exception e) {
            order.setStatus(OrderStatus.ERROR_ON_PAYMENT);
        }
    }
}