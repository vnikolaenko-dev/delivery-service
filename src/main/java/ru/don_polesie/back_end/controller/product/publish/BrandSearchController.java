package ru.don_polesie.back_end.controller.product.publish;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.service.product.BrandService;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/brand")
public class BrandSearchController {
    private BrandService brandService;

    @Operation(
            summary = "Получить список брендоа"
    )
    @GetMapping
    public ResponseEntity<List<Brand>> getBrands(){
        return ResponseEntity.ok(brandService.findAll());
    }
}
