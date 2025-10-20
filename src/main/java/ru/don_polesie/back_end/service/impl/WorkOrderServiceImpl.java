package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.OrderProduct;
import ru.don_polesie.back_end.repository.OrderProductRepository;
import ru.don_polesie.back_end.repository.OrderRepository;
import ru.don_polesie.back_end.service.WorkOrderService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkOrderServiceImpl implements WorkOrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final YooKassaServiceImpl yooKassaService;
    private final OrderService orderService;


    @Override
    public Page<OrderDtoRR> findOrdersPage(Integer pageNumber) {
        var pageable =
                PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());

        Page<Order> orderPage = orderRepository.findAllMoneyRESERVAITED(pageable);

        return orderPage
                .map(orderMapper::toOrderDtoRR);
    }

    @Override
    @Transactional
    public void markShipped(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(""));
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void processOrder(Long id, ProcessWeightsRequest req) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ не найден"));

        Map<Long, ProcessWeightsRequest.WeightDto> weightMap = req.getWeights().stream()
                .collect(Collectors.toMap(ProcessWeightsRequest.WeightDto::getProductId, w -> w));

        BigDecimal newTotal = BigDecimal.ZERO;

        for (OrderProduct op : order.getOrderProducts()) {
            var product = op.getProduct();

            int quantityGrams;
            if (Boolean.TRUE.equals(product.getIsWeighted())) {
                var w = weightMap.get(product.getId());
                if (w == null) {
                    throw new IllegalArgumentException("Не передан вес для взвешиваемого товара: " + product.getId());
                }
                quantityGrams = w.getWeight();
            } else {
                quantityGrams = op.getQuantity();
            }

            op.setQuantity(quantityGrams);
            orderProductRepository.save(op);

            newTotal = newTotal.add(orderService.calculateProductCost(product, quantityGrams));
        }

        order.setTotalAmount(newTotal);

        try {
            System.out.println("Итоговая цена: " + newTotal);
            yooKassaService.getMoney(order);
            order.setStatus(OrderStatus.READY_FOR_DELIVERY);
        } catch (Exception e) {
            order.setStatus(OrderStatus.ERROR_ON_PAYMENT);
        }

        orderRepository.save(order);
    }


    @Override
    public Long getTotalSalesForProductByDate(Long productId, Instant date) {
        return orderRepository.getTotalSalesForProductByDate(productId, date);
    }

    @Override
    public Long getOrderCountForProductByDate(Long productId, Instant date) {
        return orderRepository.getOrderCountForProductByDate(productId, date);
    }

}
