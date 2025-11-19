package ru.don_polesie.back_end.service.product;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.product.ProductDtoFull;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.model.product.Product;
import ru.don_polesie.back_end.repository.CategoryRepository;
import ru.don_polesie.back_end.repository.ProductRepository;

import javax.management.BadAttributeValueExpException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

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

    public void update(@Min(1) Integer id, String name) throws BadAttributeValueExpException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new BadAttributeValueExpException("Категорию невозможно удалить: такой категории не существует");
        }
        category.get().setName(name);
        categoryRepository.save(category.get());
    }


    public void remove(@Min(1) Integer id) throws BadAttributeValueExpException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new BadAttributeValueExpException("Категорию невозможно удалить: такой категории не существует");
        }
        Page<Product> page = productRepository.findPByCategory(category.get(), PageRequest.of(0, 1, Sort.by("id").descending()));
        if (!page.isEmpty()) {
            throw new BadAttributeValueExpException("Категорию невозможно удалить: существуют товары с такой категорией.");
        }

        categoryRepository.deleteById(id);
    }
}
