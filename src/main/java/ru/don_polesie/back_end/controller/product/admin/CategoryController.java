package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.service.product.CategoryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/category")
public class CategoryController {
    private CategoryService categoryService;

    @Operation(
            summary = "Создать категорию"
    )
    @GetMapping("/create")
    public ResponseEntity<Void> createCategory(@RequestParam String name) {
        categoryService.save(new Category(name));
        return ResponseEntity.ok().build();
    }
}
