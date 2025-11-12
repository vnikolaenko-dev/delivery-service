package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PopularProductDto {
    private Long productId;
    private String productName;
    private Long  totalQuantity;
    private BigDecimal totalRevenue;

    public PopularProductDto(Long productId, String productName, Long totalQuantity, BigDecimal totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }
}