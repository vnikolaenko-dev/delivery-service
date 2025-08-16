package ru.don_polesie.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.model.OrderProduct;
import ru.don_polesie.back_end.model.OrderProductId;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {

}
