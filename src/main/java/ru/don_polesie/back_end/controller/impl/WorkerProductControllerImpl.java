package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.WorkerProductController;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.service.ProductService;

@RestController
@RequiredArgsConstructor
public class WorkerProductControllerImpl implements WorkerProductController {

    private final ProductService productServiceImpl;

    @Override
    public ResponseEntity<ProductDtoRR> update(ProductDtoRR productDtoRR, Long id) {
        return productServiceImpl.update(productDtoRR, id);
    }

    @Override
    public ResponseEntity<ProductDtoRR> save(ProductDtoRR productDtoRR) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productServiceImpl.save(productDtoRR));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        productServiceImpl.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
