package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.WorkerProductController;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.service.WorkerProductService;

@RestController
@RequiredArgsConstructor
public class WorkerProductControllerImpl implements WorkerProductController {

    private final WorkerProductService productServiceImpl;

    @Override
    public ResponseEntity<ProductDtoRR> update(ProductDtoRR productDtoRR, Long id) {
        ProductDtoRR productDtoRRUpdated = productServiceImpl.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productDtoRRUpdated);
    }

    @Override
    public ResponseEntity<ProductDtoRR> save(ProductDtoRR productDtoRR) {
        ProductDtoRR newProductDtoRR = productServiceImpl.save(productDtoRR);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProductDtoRR);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        productServiceImpl.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
