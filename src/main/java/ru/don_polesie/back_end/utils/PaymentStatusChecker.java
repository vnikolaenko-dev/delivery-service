package ru.don_polesie.back_end.utils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.don_polesie.back_end.model.enums.OrderStatus;
import ru.don_polesie.back_end.model.order.Order;
import ru.don_polesie.back_end.repository.OrderRepository;
import ru.don_polesie.back_end.service.system.YooKassaService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStatusChecker {

    private final OrderRepository orderRepository;
    private final YooKassaService yooKassaService;

    /**
     * Каждые 5 минут проверяем заказы со статусом PAYING
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void checkPayments() {
        List<Order> orders = orderRepository.findByStatusAndPaymentIdIsNotNull(OrderStatus.PAYING);
        if (orders.isEmpty()) return;

        log.info("{} заказ(-а) ожидает оплаты", orders.size());

        for (Order order : orders) {
            try {
                yooKassaService.getPayment(order.getId());
            } catch (Exception e) {
                log.error("Failed to update status for order {}: {}", order.getId(), e.getMessage());
            }
        }
    }
}
