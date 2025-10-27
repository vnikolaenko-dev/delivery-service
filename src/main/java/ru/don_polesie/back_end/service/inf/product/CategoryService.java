package ru.don_polesie.back_end.service.inf.product;

import ru.don_polesie.back_end.model.product.Category;

import java.util.List;

public interface CategoryService {
    void save(Category category);
    List<Category> findAll();
    Category findByName(String name);
}
