package ru.don_polesie.back_end.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;

@Tag(
        name = "Управление товарами (Работник)",
        description = "API для управления каталогом товаров сотрудниками"
)
@RequestMapping("/api/worker/products")
public interface WorkerProductController {
    @Operation(
            summary = "Обновить товар",
            description = "Обновление информации о существующем товаре"
    )
    @PutMapping("/{id}")
    ResponseEntity<ProductDtoRR> update(@RequestBody @Valid ProductDtoRR productDtoRR,
                                        @PathVariable @Min(value = 1) Long id);
    @Operation(
            summary = "Добавить товар",
            description = "Создание нового товара в каталоге"
    )
    @PostMapping("")
    ResponseEntity<ProductDtoRR> save(@RequestBody @Valid ProductDtoRR productDtoRR);

    @Operation(
            summary = "Удалить товар",
            description = "Удаление товара из каталога по идентификатору"
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable @Min(value = 1) Long id);
}
