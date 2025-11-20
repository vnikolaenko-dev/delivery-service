package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.model.user.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.product.CategoryService;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/category")
@Log4j2
public class CategoryController {
    private CategoryService categoryService;
    private final SecurityUtils securityUtils;

    @Operation(
            summary = "Создать категорию"
    )
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestParam String name) {
        categoryService.save(new Category(name));

        User user = securityUtils.getCurrentUser();
        log.info("{} created category {}", user.toString(), name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить категорию"
    )
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam @Min(1) Integer id, @RequestParam String name) throws BadAttributeValueExpException {
        categoryService.update(id, name);

        User user = securityUtils.getCurrentUser();
        log.info("{} edit category {}", user.toString(), name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить категорию"
    )
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(@RequestParam @Min(1) Integer id)  {
        categoryService.remove(id);

        User user = securityUtils.getCurrentUser();
        log.info("{} removed category {}", user.toString(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Сделать неактивным категорию"
    )
    @PutMapping("/deactivate")
    public ResponseEntity<Void> deactivateBrand(@RequestParam @Min(1) Integer id) {
        categoryService.deactivate(id);

        User user = securityUtils.getCurrentUser();
        log.info("{} deactivated category {}", user.toString(), id);
        return ResponseEntity.ok().build();
    }
}
