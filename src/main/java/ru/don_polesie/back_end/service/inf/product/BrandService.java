package ru.don_polesie.back_end.service.inf.product;

import ru.don_polesie.back_end.model.product.Brand;

import java.util.List;

public interface BrandService {
    void save(Brand brand);
    List<Brand> findAll();
    Brand findByName(String name);
}
