package ru.don_polesie.back_end.controller.product.staff;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.service.product.CategoryService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/staff/category")
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping("/create")
    public ResponseEntity<Void> createCategory(@RequestParam String name) {
        categoryService.save(new Category(name));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategory(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
