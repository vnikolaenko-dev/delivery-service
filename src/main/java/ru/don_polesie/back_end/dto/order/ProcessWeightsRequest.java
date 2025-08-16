package ru.don_polesie.back_end.dto.order;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessWeightsRequest {

    @NotEmpty(message = "Список весов не должен быть пустым")
    private List<WeightDto> weights;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeightDto {
        @NotNull(message = "productId обязателен")
        private Long productId;

        @NotNull(message = "weight обязателен")
        @PositiveOrZero(message = "Вес не может быть отрицательным")
        private Double weight;
    }
}

