package ru.don_polesie.back_end.controller.product.staff;

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
@RequestMapping("/api/staff/brand")
public class BrandController {
    private BrandService brandService;

    @GetMapping("/create")
    public ResponseEntity<Void> createBrand(@RequestParam String name) {
        brandService.save(new Brand(name));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Brand>> getBrands(){
        return ResponseEntity.ok(brandService.findAll());
    }
}
