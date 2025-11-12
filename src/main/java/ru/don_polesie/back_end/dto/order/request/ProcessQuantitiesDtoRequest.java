package ru.don_polesie.back_end.dto.order.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessQuantitiesDtoRequest {

    private List<QuantityDto> quantities;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuantityDto {
        @NotNull(message = "productId обязателен")
        private Long productId;

        @NotNull(message = "quantity обязателен")
        @PositiveOrZero(message = "Количество товара не может быть отрицательным")
        private Integer quantity;
    }
}

