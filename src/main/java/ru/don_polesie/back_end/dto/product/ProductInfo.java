package ru.don_polesie.back_end.dto.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private int id;
    private String name;
    private String description;
}
