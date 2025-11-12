package ru.don_polesie.back_end.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoFull;
import ru.don_polesie.back_end.service.product.WorkerProductService;

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductManageController {

    private final WorkerProductService productServiceImpl;

    @Operation(
            summary = "Добавить товар",
            description = "Создание нового товара в каталоге"
    )
    @PostMapping
    public ResponseEntity<ProductDtoFull> save(@RequestBody @Valid ProductDtoFull productDtoFull) {
        ProductDtoFull newProductDtoFull = productServiceImpl.save(productDtoFull);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProductDtoFull);
    }

    @Operation(
            summary = "Обновить товар",
            description = "Обновление информации о существующем товаре"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid ProductDtoFull productDtoFull,
                                       @PathVariable @Min(value = 1) Long id) {
        productServiceImpl.update(productDtoFull, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(
            summary = "Обновить скидку",
            description = "Обновление скидки товара"
    )
    @PutMapping("sale/{id}")
    public ResponseEntity<Void> editSale(
            @PathVariable @Min(value = 1) Long id,
            @RequestParam @Min(value = 0) @Max(value = 100) Integer sale) {
        productServiceImpl.editSale(id, sale);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(
            summary = "Удалить товар",
            description = "Удаление товара из каталога по идентификатору"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1) Long id) {
        productServiceImpl.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
