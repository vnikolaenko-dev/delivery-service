package ru.don_polesie.back_end.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoSearch;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.ProductMapper;
import ru.don_polesie.back_end.model.Product;
import ru.don_polesie.back_end.repository.ProductRepository;
import ru.don_polesie.back_end.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public Page<ProductDtoRR> findProductsPage(Integer pageNumber) {
        var pageable =
                PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Product> productPage = productRepository.findAllByAmountGreaterThan(0, pageable);
        return productPage
                .map(productMapper::toProductDtoRR);
    }

    @Override
    public ProductDtoRR findById(Long id) {
        return productMapper
                .toProductDtoRR(
                        productRepository
                                .findById(id)
                                .orElseThrow(() -> new ObjectNotFoundException(""))
                );
    }

    @Override
    public Page<ProductDtoRR> findAllByParams(ProductDtoSearch productDtoSearch,
                                              Integer pageNumber) {
        // по 20 элементов на страницу
        Pageable pageable = PageRequest.of(pageNumber, 20);

        return productRepository
                .findProductsByParams(
                        productDtoSearch.getId(),
                        productDtoSearch.getBrand(),
                        productDtoSearch.getName(),
                        pageable
                )
                .map(productMapper::toProductDtoRR);
    }

    @Override
    public Page<ProductDtoRR> findProductByQuery(String query, Integer pageNumber) {
        var pageable =
                PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Product> productPage = productRepository.searchProductsByQuery(query, pageable);
        return productPage.map(productMapper::toProductDtoRR);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductDtoRR> update(ProductDtoRR productDtoRR, Long id) {
        // Ищем существующий товар
        var productOld = productRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Product not found with id = " + productDtoRR.getId()
                ));
        // Переносим все обновляемые поля из DTO в сущность
        productOld.setBrand(productDtoRR.getBrand());
        productOld.setName(productDtoRR.getName());
        productOld.setPrice(productDtoRR.getPrice());
        productOld.setImageUrl(productDtoRR.getImageUrl());
        productOld.setFatGrams(productDtoRR.getFatGrams());
        productOld.setProteinGrams(productDtoRR.getProteinGrams());
        productOld.setCarbohydrateGrams(productDtoRR.getCarbohydrateGrams());
        productOld.setEnergyKcalPer100g(productDtoRR.getEnergyKcalPer100g());
        productOld.setVolume(productDtoRR.getVolume());
        productOld.setStorageTemperatureMin(productDtoRR.getStorageTemperatureMin());
        productOld.setStorageTemperatureMax(productDtoRR.getStorageTemperatureMax());
        productOld.setCountryOfOrigin(productDtoRR.getCountryOfOrigin());

        // Сохраняем изменения
        productRepository.save(productOld);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toProductDtoRR(productOld));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDtoRR save(ProductDtoRR productDtoRR) {
        var productNew = productMapper.productDtoRRtoProduct(productDtoRR);
        return productMapper.toProductDtoRR(productRepository.save(productNew));
    }


}
