package ru.don_polesie.back_end.service.impl;

import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.Product;

import java.math.BigDecimal;

@Service
public class OrderService {
    public BigDecimal calculateProductCost(Product product, int quantityGrams) {
        if (Boolean.TRUE.equals(product.getIsWeighted())) {
            double pricePerKg = product.getPrice();
            double cost = ((double) quantityGrams / 1000) * pricePerKg;
            return BigDecimal.valueOf(cost);
        } else {
            // quantityGrams тут может быть количеством штук
            return BigDecimal.valueOf(product.getPrice() * quantityGrams);
        }
    }

}
