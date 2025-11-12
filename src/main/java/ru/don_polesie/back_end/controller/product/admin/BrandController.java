package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.service.product.BrandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/brand")
public class BrandController {
    private BrandService brandService;

    @Operation(
            summary = "Создать бренд"
    )
    @GetMapping("/create")
    public ResponseEntity<Void> createBrand(@RequestParam String name) {
        brandService.save(new Brand(name));
        return ResponseEntity.ok().build();
    }
}
