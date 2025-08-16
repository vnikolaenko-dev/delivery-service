package ru.don_polesie.back_end.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDtoSearch {
    private Long id;
    private String brand;
    private String name;
}
