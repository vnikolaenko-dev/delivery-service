package ru.don_polesie.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.Product;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(Long integer);

    boolean existsByIdAndGuestId(Long id, String guestId);


    void deleteById(Long integer);
}
