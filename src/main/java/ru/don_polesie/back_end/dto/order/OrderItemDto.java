package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private String name;
    private String brand;
    private String imageUrl;
    private String quantity;
    private Boolean isWeighted;
}
