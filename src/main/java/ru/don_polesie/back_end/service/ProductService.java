package ru.don_polesie.back_end.service;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoSearch;

import java.util.List;

public interface ProductService {

    Page<ProductDtoRR> findProductsPage(@RequestParam Integer pageNumber);

    ProductDtoRR findById(Long id);

    Page<ProductDtoRR> findAllByParams(ProductDtoSearch productDtoSearch, Integer pageNumber);

    Page<ProductDtoRR> findProductByQuery(String query, Integer pageNumber);

    ResponseEntity<ProductDtoRR> update(ProductDtoRR productDtoRR, Long id);

    void deleteById(Long id);

    ProductDtoRR save(ProductDtoRR productDtoRR);
}
