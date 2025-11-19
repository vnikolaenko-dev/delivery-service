package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.service.product.BrandService;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/brand")
public class BrandController {
    private BrandService brandService;

    @Operation(
            summary = "Создать бренд"
    )
    @PostMapping
    public ResponseEntity<Void> createBrand(@RequestParam String name) {
        brandService.save(new Brand(name));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить брэнд"
    )
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam @Min(1) Integer id, @RequestParam String name) throws BadAttributeValueExpException {
        brandService.update(id, name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить бренд"
    )
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(@RequestParam @Min(1) Integer id) throws BadAttributeValueExpException {
        brandService.remove(id);
        return ResponseEntity.ok().build();
    }
}
