package ru.don_polesie.back_end.service;

import org.springframework.data.domain.Page;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;

import java.util.List;

public interface OrderService {

    Page<OrderDtoRR> findOrdersPage(Integer pageNumber);

    OrderCreateResponse save(OrderDtoRR orderDtoRR);

    void deleteOrder(Long orderId);

    void deleteProductFromOrder(Long orderId, Long productId);

    void markShipped(Long id);

    OrderDtoRR findById(Long id);

    void processOrder(Long id, ProcessWeightsRequest req);
}
