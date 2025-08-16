package ru.don_polesie.back_end.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDtoRR {

    private Long id;

    @NotBlank(message = "Бренд не должен быть пустым")
    @Size(max = 255, message = "Бренд должен содержать не более 255 символов")
    private String brand;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 255, message = "Имя должно содержать не более 255 символов")
    private String name;

    @NotBlank(message = "Фото не должно быть пустым")
    private String imageUrl;

    @NotNull(message = "Цена обязательна")
    @PositiveOrZero(message = "Цена не может быть меньше 0")
    private Double price;

    @NotNull(message = "Жиры обязательны")
    @DecimalMin(value = "0.0", message = "Жиры не могут быть меньше 0")
    private Double fatGrams;

    @NotNull(message = "Белки обязательны")
    @DecimalMin(value = "0.0", message = "Белки не могут быть меньше 0")
    private Double proteinGrams;

    @NotNull(message = "Углеводы обязательны")
    @DecimalMin(value = "0.0", message = "Углеводы не могут быть меньше 0")
    private Double carbohydrateGrams;

    @NotNull(message = "Энергетическая ценность обязательна")
    @Min(value = 0, message = "Энергетическая ценность не может быть отрицательной")
    private Integer energyKcalPer100g;

    @NotNull(message = "Вес/объём обязателен")
    private String Volume;

    @NotNull(message = "Количество товара обязателно")
    @Min(value = 1, message = "Количество товара должно быть положительным")
    private Integer amount;

    @NotNull(message = "Минимальная температура хранения обязательна")
    private Integer storageTemperatureMin;

    private Boolean isWeighted;

    @NotNull(message = "Максимальная температура хранения обязательна")
    @AssertTrue(message = "Максимальная температура должна быть больше или равна минимальной")
    private boolean isValidTemperatureRange() {
        if (storageTemperatureMin == null || storageTemperatureMax == null) return true;
        return storageTemperatureMax >= storageTemperatureMin;
    }

    @NotNull(message = "Максимальная температура хранения обязательна")
    private Integer storageTemperatureMax;


    @NotBlank(message = "Страна-производитель обязательна")
    private String countryOfOrigin;
}
