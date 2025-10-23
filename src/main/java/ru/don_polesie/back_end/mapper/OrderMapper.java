package ru.don_polesie.back_end.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.OrderProduct;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderProducts", target = "items", qualifiedByName = "mapItems")
    OrderDtoRR toOrderDtoRR(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderProducts", ignore = true)
    Order toOrder(OrderDtoRR orderDtoRR);


    @Named("mapItems")
    default List<OrderItemDto> mapItems(Set<OrderProduct> ops) {
        return ops.stream()
                .map(op -> new OrderItemDto(
                        op.getProduct().getId(),
                        op.getQuantity())
                )
                .collect(Collectors.toList());
    }
}
