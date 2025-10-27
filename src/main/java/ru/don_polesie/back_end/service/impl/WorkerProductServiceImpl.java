package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoSearch;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.ProductMapper;
import ru.don_polesie.back_end.model.product.Product;
import ru.don_polesie.back_end.repository.ProductRepository;
import ru.don_polesie.back_end.service.inf.WorkerProductService;
import ru.don_polesie.back_end.service.inf.product.BrandService;
import ru.don_polesie.back_end.service.inf.product.CategoryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkerProductServiceImpl implements WorkerProductService {

    private static final int PAGE_SIZE = 10;

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
    @Override
    public Page<ProductDtoRR> findProductsPage(Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        return productRepository.findAllByAmountGreaterThan(0, pageable)
                .map(productMapper::toProductDtoRR);
    }

    /**
     * Находит товар по идентификатору
     *
     * @param id идентификатор товара
     * @return DTO товара
     * @throws ObjectNotFoundException если товар не найден
     */
    @Override
    public ProductDtoRR findById(Long id) {
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
    @Override
    public Page<ProductDtoRR> findAllByParams(ProductDtoSearch productDtoSearch, Integer pageNumber) {
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
    @Override
    public Page<ProductDtoRR> findProductByQuery(String query, Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        return productRepository.searchProductsByQuery(query, pageable)
                .map(productMapper::toProductDtoRR);
    }

    /**
     * Обновляет данные товара
     *
     * @param productDtoRR новые данные товара
     * @param id идентификатор обновляемого товара
     * @return обновленный DTO товара
     * @throws ObjectNotFoundException если товар не найден
     */
    @Override
    @Transactional
    public ProductDtoRR update(ProductDtoRR productDtoRR, Long id) {
        Product existingProduct = getProductById(id);
        checkBrandAndCategory(existingProduct);
        updateProductFromDto(existingProduct, productDtoRR);
        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toProductDtoRR(savedProduct);
    }

    /**
     * Удаляет товар по идентификатору
     *
     * @param id идентификатор товара для удаления
     * @throws ObjectNotFoundException если товар не найден
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ObjectNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Сохраняет новый товар
     *
     * @param productDtoRR DTO с данными нового товара
     * @return сохраненный DTO товара
     */
    @Override
    @Transactional
    public ProductDtoRR save(ProductDtoRR productDtoRR) {
        Product newProduct = productMapper.productDtoRRtoProduct(productDtoRR);
        checkBrandAndCategory(newProduct);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toProductDtoRR(savedProduct);
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
        return PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("id").descending());
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
     * @param productDtoRR DTO с новыми значениями полей
     */
    private void updateProductFromDto(Product product, ProductDtoRR productDtoRR) {
        // product.setBrand(new Brand(productDtoRR.getBrand()));
        product.setName(productDtoRR.getName());
        product.setPrice(productDtoRR.getPrice());
        product.setImageUrl(productDtoRR.getImageUrl());
        product.setFatGrams(productDtoRR.getFatGrams());
        product.setProteinGrams(productDtoRR.getProteinGrams());
        product.setCarbohydrateGrams(productDtoRR.getCarbohydrateGrams());
        product.setEnergyKcalPer100g(productDtoRR.getEnergyKcalPer100g());
        product.setMinWeight(productDtoRR.getMinWeight());
        product.setMaxWeight(productDtoRR.getMaxWeight());
        product.setStorageTemperatureMin(productDtoRR.getStorageTemperatureMin());
        product.setStorageTemperatureMax(productDtoRR.getStorageTemperatureMax());
        product.setCountryOfOrigin(productDtoRR.getCountryOfOrigin());
    }
}