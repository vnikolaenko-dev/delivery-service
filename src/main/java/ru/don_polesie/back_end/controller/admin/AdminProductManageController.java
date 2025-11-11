package ru.don_polesie.back_end.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
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
    public ResponseEntity<ProductDtoRR> save(@RequestBody @Valid ProductDtoRR productDtoRR) {
        ProductDtoRR newProductDtoRR = productServiceImpl.save(productDtoRR);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProductDtoRR);
    }

    @Operation(
            summary = "Обновить товар",
            description = "Обновление информации о существующем товаре"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDtoRR> update(@RequestBody @Valid ProductDtoRR productDtoRR,
                                               @PathVariable @Min(value = 1) Long id) {
        ProductDtoRR productDtoRRUpdated = productServiceImpl.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productDtoRRUpdated);
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
