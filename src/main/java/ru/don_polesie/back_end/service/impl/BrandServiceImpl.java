package ru.don_polesie.back_end.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Brand;
import ru.don_polesie.back_end.repository.BrandRepository;
import ru.don_polesie.back_end.service.inf.product.BrandService;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private BrandRepository brandRepository;

    @Override
    public void save(Brand brand) {
        if (brandRepository.findByName(brand.getName()).isPresent()) {
            throw new RuntimeException("Brand " + brand.getName() + " already exists");
        }
        brandRepository.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }
}
