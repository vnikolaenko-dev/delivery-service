package ru.don_polesie.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.model.basket.Basket;
import ru.don_polesie.back_end.model.order.Order;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUser_PhoneNumber(String phoneNumber);
}
