package ru.don_polesie.back_end.controller.product.staff;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoFull;
import ru.don_polesie.back_end.service.product.WorkerProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/product")
public class ProductControllerImpl {

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
    public ResponseEntity<ProductDtoFull> update(@RequestBody @Valid ProductDtoFull productDtoFull,
                                                 @PathVariable @Min(value = 1) Long id) {
        ProductDtoFull productDtoFullUpdated = productServiceImpl.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productDtoFullUpdated);
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
