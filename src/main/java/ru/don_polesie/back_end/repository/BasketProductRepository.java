package ru.don_polesie.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.model.basket.BasketProduct;
import ru.don_polesie.back_end.model.basket.BasketProductId;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, BasketProductId> {
}
