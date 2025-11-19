package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.service.product.CategoryService;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/category")
public class CategoryController {
    private CategoryService categoryService;

    @Operation(
            summary = "Создать категорию"
    )
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestParam String name) {
        categoryService.save(new Category(name));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить категорию"
    )
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam @Min(1) Integer id, @RequestParam String name) throws BadAttributeValueExpException {
        categoryService.update(id, name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить категорию"
    )
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(@RequestParam @Min(1) Integer id) throws BadAttributeValueExpException {
        categoryService.remove(id);
        return ResponseEntity.ok().build();
    }
}
