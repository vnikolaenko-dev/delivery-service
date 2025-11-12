package ru.don_polesie.back_end.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.don_polesie.back_end.dto.basket.BasketDtoResponse;
import ru.don_polesie.back_end.model.basket.Basket;
import ru.don_polesie.back_end.model.basket.BasketProduct;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BasketMapper INSTANCE = Mappers.getMapper(BasketMapper.class);

    @Mapping(target = "items", source = "basketProducts")
    @Mapping(target = "totalPrice", ignore = true)
    BasketDtoResponse toDto(Basket basket);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "price", ignore = true)
    BasketDtoResponse.BasketItemDto toItemDto(BasketProduct basketProduct);
}
