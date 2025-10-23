package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.model.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.AddressMapper;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.*;
import ru.don_polesie.back_end.model.product.Product;
import ru.don_polesie.back_end.repository.*;
import ru.don_polesie.back_end.service.UserOrderService;
import ru.don_polesie.back_end.service.YooKassaService;
import ru.don_polesie.back_end.service.impl.order.PriceService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserOrderServiceImpl implements UserOrderService {

    private static final int PAGE_SIZE = 10;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final YooKassaService yooKassaServiceImpl;
    private final PriceService priceService;

    /**
     * Находит заказ по идентификатору
     *
     * @param id идентификатор заказа
     * @return DTO заказа
     * @throws ObjectNotFoundException если заказ не найден
     */
    @Override
    public OrderDtoRR findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderDtoRR)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found with id: " + id));
    }

    /**
     * Получает страницу заказов пользователя
     *
     * @param pageNumber номер страницы
     * @param username имя пользователя
     * @return страница с заказами
     */
    @Override
    public Page<OrderDtoRR> findUserOrdersPage(Integer pageNumber, String phoneNumber) {
        Pageable pageable = createPageable(pageNumber);
        return orderRepository.findByUserPhoneNumber(phoneNumber, pageable)
                .map(orderMapper::toOrderDtoRR);
    }

    /**
     * Получает страницу доставленных заказов пользователя
     *
     * @param pageNumber номер страницы
     * @param username имя пользователя
     * @return страница с доставленными заказами
     */
    @Override
    public Page<OrderDtoRR> findShippedUserOrdersPage(Integer pageNumber, String phoneNumber) {
        Pageable pageable = createPageable(pageNumber);
        return orderRepository.findByUserPhoneNumberAndStatus(phoneNumber, OrderStatus.SHIPPED, pageable)
                .map(orderMapper::toOrderDtoRR);
    }

    /**
     * Удаляет заказ по идентификатору
     *
     * @param orderId идентификатор заказа
     */
    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    /**
     * Создает новый заказ и платеж
     *
     * @param orderDtoRR DTO с данными заказа
     * @param user пользователь, создающий заказ
     * @return ответ с созданным заказом и данными платежа
     * @throws RuntimeException если не удалось создать платеж
     */
    @Override
    @Transactional
    public OrderCreateResponse save(OrderDtoRR orderDtoRR, User user) {
        Address address = processAddress(orderDtoRR.getAddress());
        Order order = createOrder(orderDtoRR, user, address);
        processOrderItems(orderDtoRR, order);

        return createPaymentResponse(order);
    }

    /**
     * Удаляет товар из заказа и пересчитывает стоимость
     *
     * @param orderId идентификатор заказа
     * @param productId идентификатор товара
     * @throws ObjectNotFoundException если заказ, товар или связка не найдены
     */
    @Override
    @Transactional
    public void deleteProductFromOrder(Long orderId, Long productId) {
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);
        OrderProduct orderProduct = getOrderProduct(orderId, productId);

        BigDecimal productPrice = priceService.calculateProductCost(product, orderProduct.getQuantity());

        order.setTotalAmount(order.getTotalAmount().subtract(productPrice));
        order.removeProduct(orderProduct);
        orderRepository.save(order);
    }

    // ========== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    /**
     * Создает объект пагинации
     */
    private Pageable createPageable(Integer pageNumber) {
        return PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("id").descending());
    }

    /**
     * Обрабатывает адрес доставки
     */
    private Address processAddress(AddressDTO addressDto) {
        Address address = addressMapper.toEntity(addressDto);
        return findExistingAddressOrSaveNew(address);
    }

    /**
     * Находит существующий адрес или сохраняет новый
     */
    private Address findExistingAddressOrSaveNew(Address address) {
        if (address.getId() == null) {
            return addressRepository.save(address);
        }
        return addressRepository.findById(address.getId())
                .orElseGet(() -> addressRepository.save(address));
    }

    /**
     * Создает новый заказ
     */
    private Order createOrder(OrderDtoRR orderDtoRR, User user, Address address) {
        Order order = orderMapper.toOrder(orderDtoRR);
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.NEW);
        order.setTotalAmount(BigDecimal.ZERO);
        return orderRepository.save(order);
    }

    /**
     * Обрабатывает товары в заказе и рассчитывает общую стоимость
     */
    private void processOrderItems(OrderDtoRR orderDtoRR, Order order) {
        BigDecimal totalAmount = orderDtoRR.getItems().stream()
                .map(orderItem -> {
                    Product product = productRepository.findById(orderItem.getProductId()).get(); // предположим, что есть метод getProduct()
                    int quantity = orderItem.getQuantity();   // предположим, что есть метод getQuantity()
                    return priceService.calculateProductCost(product, orderItem.getQuantity());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PAYING);
        orderRepository.save(order);
    }

    /**
     * Обрабатывает один товар в заказе
     */
    private BigDecimal processOrderItem(OrderItemDto orderItem, Order order) {
        Product product = getProductById(orderItem.getProductId());
        OrderProduct orderProduct = new OrderProduct(order, product, orderItem.getQuantity());

        BigDecimal itemCost = priceService.calculateProductCost(product, orderItem.getQuantity());
        order.addProduct(orderProduct);

        return itemCost;
    }

    /**
     * Создает ответ с данными платежа
     */
    private OrderCreateResponse createPaymentResponse(Order order) {
        try {
            return new OrderCreateResponse(
                    orderMapper.toOrderDtoRR(order),
                    yooKassaServiceImpl.createPayment(order.getId())
            );
        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed: " + e.getMessage());
        }
    }

    /**
     * Находит заказ по ID
     */
    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found with id: " + orderId));
    }

    /**
     * Находит товар по ID
     */
    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
    }

    /**
     * Находит связку товара в заказе
     */
    private OrderProduct getOrderProduct(Long orderId, Long productId) {
        OrderProductId orderProductId = new OrderProductId(orderId, productId);
        return orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Order product not found for orderId: " + orderId + " and productId: " + productId));
    }
}