package ru.don_polesie.back_end.mapper;

import org.mapstruct.*;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoXML;
import ru.don_polesie.back_end.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDtoRR toProductDtoRR(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "isWeighted", target = "isWeighted")
    Product productDtoXMLtoProduct(ProductDtoXML productDtoXML);

    @Mapping(target = "id", ignore = true)
    Product productDtoRRtoProduct(ProductDtoRR productDtoRR);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(ProductDtoXML dto, @MappingTarget Product entity);

}
