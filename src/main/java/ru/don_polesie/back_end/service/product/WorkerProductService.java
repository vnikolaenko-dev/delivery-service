package ru.don_polesie.back_end.service.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.product.ProductDtoFull;
import ru.don_polesie.back_end.dto.product.request.ProductDtoSearchRequest;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.ProductMapper;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.model.product.Category;
import ru.don_polesie.back_end.model.product.Product;
import ru.don_polesie.back_end.repository.ProductRepository;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class WorkerProductService {

    @Value("${utils.page-size}")
    private int PAGE_SIZE;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final BrandService brandService;


    /**
     * Получает страницу с товарами, имеющими ненулевое количество
     *
     * @param pageNumber номер страницы (начинается с 1)
     * @return страница с товарами в формате DTO
     */

    public Page<ProductDtoFull> findProductsPage(@Min(value = 0) Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        // только те товары, которые есть в наличии
        return productRepository.findAllByAmountGreaterThan(0, pageable)
                .map(productMapper::toProductDtoRR);
    }

    public Page<ProductDtoFull> findProductsPageWithSale(@Min(value = 0) Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        // только те товары, которые со скидкой
        return productRepository.findAllBySaleGreaterThan(0, pageable)
                .map(productMapper::toProductDtoRR);
    }

    public Page<ProductDtoFull> findProductsByCategory(@Min(value = 0) Integer pageNumber, Category category) {
        Pageable pageable = createDefaultPageable(pageNumber);
        Page<Product> productsPage = productRepository.findPByCategory(category, pageable);
        if (!productsPage.hasContent()) {
            throw new ObjectNotFoundException("Продуктов с такой категорией не найдено");
        }
        return productsPage.map(productMapper::toProductDtoRR);
    }

    public Page<ProductDtoFull> findProductsByBrand(@Min(value = 0) Integer pageNumber, Brand brand) {
        Pageable pageable = createDefaultPageable(pageNumber);
        Page<Product> productsPage = productRepository.findPByBrand(brand, pageable);
        if (!productsPage.hasContent()) {
            throw new ObjectNotFoundException("Продуктов с таким брендом не найдено");
        }
        return productsPage.map(productMapper::toProductDtoRR);
    }

    /**
     * Находит товар по идентификатору
     *
     * @param id идентификатор товара
     * @return DTO товара
     * @throws ObjectNotFoundException если товар не найден
     */

    public ProductDtoFull findById(@Min(value = 0) Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDtoRR)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + id));
    }

    /**
     * Находит товары по параметрам поиска
     *
     * @param productDtoSearch DTO с параметрами поиска (id, бренд, название)
     * @param pageNumber номер страницы
     * @return страница с найденными товарами
     */

    public Page<ProductDtoFull> findAllByParams(ProductDtoSearchRequest productDtoSearch, @Min(value = 0) Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return productRepository.findProductsByParams(
                        productDtoSearch.getId(),
                        productDtoSearch.getBrand(),
                        productDtoSearch.getName(),
                        pageable
                )
                .map(productMapper::toProductDtoRR);
    }

    /**
     * Находит товары по текстовому запросу
     *
     * @param query текстовый запрос для поиска
     * @param pageNumber номер страницы
     * @return страница с найденными товарами
     */

    public Page<ProductDtoFull> findProductByQuery(String query, @Min(value = 0) Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        return productRepository.searchProductsByQuery(query, pageable)
                .map(productMapper::toProductDtoRR);
    }

    /**
     * Сохраняет новый товар
     *
     * @param productDtoFull DTO с данными нового товара
     * @return сохраненный DTO товара
     */

    @Transactional
    public ProductDtoFull save(ProductDtoFull productDtoFull) {
        Product newProduct = productMapper.productDtoRRtoProduct(productDtoFull);
        checkBrandAndCategory(newProduct);
        Product savedProduct = productRepository.save(newProduct);
        log.info("Saved product: {}", savedProduct);
        return productMapper.toProductDtoRR(savedProduct);
    }

    @Transactional
    public void editSale(@Min(value = 1) Long id, @Min(value = 1) @Max(value = 100) Integer sale) {
        Product existingProduct = getProductById(id);
        existingProduct.setSale(sale);
        log.info("Edited sale for product with id: {} sale:{}", existingProduct.getId(), existingProduct.getSale());
        productRepository.save(existingProduct);
    }

    /**
     * Обновляет данные товара
     *
     * @param productDtoFull новые данные товара
     * @param id идентификатор обновляемого товара
     * @return обновленный DTO товара
     * @throws ObjectNotFoundException если товар не найден
     */

    @Transactional
    public ProductDtoFull update(ProductDtoFull productDtoFull, @Min(value = 0) Long id) {
        Product existingProduct = getProductById(id);
        checkBrandAndCategory(existingProduct);
        updateProductFromDto(existingProduct, productDtoFull);
        Product savedProduct = productRepository.save(existingProduct);
        log.info("Updated product: {}", savedProduct);
        return productMapper.toProductDtoRR(savedProduct);
    }

    /**
     * Удаляет товар по идентификатору
     *
     * @param id идентификатор товара для удаления
     * @throws ObjectNotFoundException если товар не найден
     */

    @Transactional
    public void deleteById(@Min(value = 0) Long id) {
        if (!productRepository.existsById(id)) {
            throw new ObjectNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product with id: {}", id);
    }


    // ========== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private void checkBrandAndCategory(Product product) {
        String brandName = product.getBrand().getName();
        String categoryName = product.getCategory().getName();
        product.setBrand(brandService.findByName(brandName));
        product.setCategory(categoryService.findByName(categoryName));
        if (product.getBrand() == null) {
            throw new ObjectNotFoundException("Brand not found with name: " + brandName);
        } else if (product.getCategory() == null) {
            throw new ObjectNotFoundException("Category not found with name: " + categoryName);
        }
    }

    /**
     * Создает объект пагинации с настройками по умолчанию
     *
     * @param pageNumber номер страницы (начинается с 1, преобразуется в 0-based)
     * @return настроенный объект Pageable
     */
    private Pageable createDefaultPageable(Integer pageNumber) {
        return PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id").descending());
    }

    /**
     * Находит товар по ID или выбрасывает исключение
     *
     * @param id идентификатор товара
     * @return найденный товар
     * @throws ObjectNotFoundException если товар не найден
     */
    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + id));
    }

    /**
     * Обновляет поля товара из DTO
     *
     * @param product сущность товара для обновления
     * @param productDtoFull DTO с новыми значениями полей
     */
    private void updateProductFromDto(Product product, ProductDtoFull productDtoFull) {
        // product.setBrand(new Brand(productDtoRR.getBrand()));
        product.setName(productDtoFull.getName());
        product.setPrice(BigDecimal.valueOf(productDtoFull.getPrice()));
        product.setImageUrl(productDtoFull.getImageUrl());
        product.setFatGrams(BigDecimal.valueOf(productDtoFull.getFatGrams()));
        product.setProteinGrams(BigDecimal.valueOf(productDtoFull.getProteinGrams()));
        product.setCarbohydrateGrams(BigDecimal.valueOf(productDtoFull.getCarbohydrateGrams()));
        product.setEnergyKcalPer100g(BigDecimal.valueOf(productDtoFull.getEnergyKcalPer100g()));
        product.setMinWeight(productDtoFull.getMinWeight());
        product.setMaxWeight(productDtoFull.getMaxWeight());
        product.setStorageTemperatureMin(productDtoFull.getStorageTemperatureMin());
        product.setStorageTemperatureMax(productDtoFull.getStorageTemperatureMax());
        product.setCountryOfOrigin(productDtoFull.getCountryOfOrigin());
    }

}