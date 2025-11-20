package ru.don_polesie.back_end.controller.product.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.model.user.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.product.BrandService;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/brand")
@Log4j2
public class BrandController {
    private BrandService brandService;
    private final SecurityUtils securityUtils;

    @Operation(
            summary = "Создать бренд"
    )
    @PostMapping
    public ResponseEntity<Void> createBrand(@RequestParam String name) {
        brandService.save(new Brand(name));

        User user = securityUtils.getCurrentUser();
        log.info("{} created brand {}", user.toString(), name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить брэнд"
    )
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam @Min(1) Integer id, @RequestParam String name) throws BadAttributeValueExpException {
        brandService.update(id, name);

        User user = securityUtils.getCurrentUser();
        log.info("{} edit brand {}", user.toString(), name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить бренд"
    )
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(@RequestParam @Min(1) Integer id) throws BadAttributeValueExpException {
        brandService.remove(id);

        User user = securityUtils.getCurrentUser();
        log.info("{} removed brand {}", user.toString(), id);
        return ResponseEntity.ok().build();
    }
}
