package ru.don_polesie.back_end.service;

import org.springframework.data.domain.Page;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;

import java.time.Instant;

public interface WorkerOrderService {
    OrderDtoRR findById(Long id);

    Page<OrderDtoRR> findOrdersPage(Integer pageNumber);

    Page<OrderDtoRR> findMoneyReservaitedOrdersPage(Integer pageNumber);

    void markShipped(Long id);

    void processOrder(Long id, ProcessWeightsRequest req);

    Long getTotalSalesForProductByDate(Long productId, Instant date);

    Long getOrderCountForProductByDate(Long productId, Instant date);

}
