package ru.don_polesie.back_end.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.repository.BrandRepository;
import ru.don_polesie.back_end.repository.CategoryRepository;
import ru.don_polesie.back_end.service.inf.product.CategoryService;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Override
    public void save(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category " + category.getName() + " already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
