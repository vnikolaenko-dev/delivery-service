package ru.don_polesie.back_end.service.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.repository.BrandRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandService {
    private BrandRepository brandRepository;

    public void save(Brand brand) {
        if (brandRepository.findByName(brand.getName()).isPresent()) {
            throw new RuntimeException("Brand " + brand.getName() + " already exists");
        }
        brandRepository.save(brand);
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }
}
