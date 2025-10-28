package ru.don_polesie.back_end.service.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    public void save(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category " + category.getName() + " already exists");
        }
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
