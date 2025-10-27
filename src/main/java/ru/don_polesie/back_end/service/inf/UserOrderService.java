package ru.don_polesie.back_end.service.inf;

import org.springframework.data.domain.Page;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.model.User;

public interface UserOrderService {

    Page<OrderDtoRR> findUserOrdersPage(Integer pageNumber, String username);

    Page<OrderDtoRR> findShippedUserOrdersPage(Integer pageNumber, String username);

    OrderCreateResponse save(OrderDtoRR orderDtoRR, User user);

    OrderCreateResponse changeQuantityProductFromOrder(Long orderId, Long productId, int quantity);

    OrderCreateResponse deleteProductFromOrder(Long orderId, Long productId);

    void deleteOrder(Long orderId);

    OrderDtoRR findById(Long id);
}
