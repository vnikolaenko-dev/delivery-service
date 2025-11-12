package ru.don_polesie.back_end.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.don_polesie.back_end.dto.user.BasketDTO;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.model.basket.Basket;
import ru.don_polesie.back_end.model.basket.BasketProduct;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BasketMapper INSTANCE = Mappers.getMapper(BasketMapper.class);

    @Mapping(target = "items", source = "basketProducts")
    BasketDTO toDto(Basket basket);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemDto toItemDto(BasketProduct basketProduct);
}
