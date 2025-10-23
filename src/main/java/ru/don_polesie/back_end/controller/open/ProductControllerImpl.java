package ru.don_polesie.back_end.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoSearch;
import ru.don_polesie.back_end.service.WorkerProductService;


@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final WorkerProductService productServiceImpl;

    @Override
    public ResponseEntity<Page<ProductDtoRR>> findProductsPage(Integer pageNumber) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findProductsPage(pageNumber));
    }

    @Override
    public ResponseEntity<ProductDtoRR> findById(Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findById(id));
    }

    @Override
    public ResponseEntity<Page<ProductDtoRR>> findAllByParams(Long id,
                                                              String brand,
                                                              String name,
                                                              Integer pageNumber) {
        ProductDtoSearch productDtoSearch = ProductDtoSearch
                .builder()
                .id(id)
                .brand(brand)
                .name(name)
                .build();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findAllByParams(productDtoSearch, pageNumber));
    }

    @Override
    public ResponseEntity<Page<ProductDtoRR>> findProductsByQuery(String query,
                                                                  Integer pageNumber) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findProductByQuery(query, pageNumber));
    }

}
