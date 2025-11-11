package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PopularProductDto {
    private Long productId;
    private String productName;
    private Long totalQuantity;
    private Double totalRevenue;
}