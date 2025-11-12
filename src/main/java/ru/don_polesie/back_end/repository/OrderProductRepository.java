package ru.don_polesie.back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.dto.order.response.PopularProductDtoResponse;
import ru.don_polesie.back_end.model.order.OrderProduct;
import ru.don_polesie.back_end.model.order.OrderProductId;

import java.time.Instant;
import java.time.LocalDateTime;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {

    @Query("""
        SELECT new ru.don_polesie.back_end.dto.order.response.PopularProductDtoResponse(
            p.id,
            p.name,
            SUM(op.quantity),
            SUM(op.quantity * p.price)
        )
        FROM OrderProduct op
        JOIN op.product p
        JOIN op.order o
        WHERE o.createdAt BETWEEN :start AND :end
          AND o.status = ru.don_polesie.back_end.model.enums.OrderStatus.SHIPPED
        GROUP BY p.id, p.name
        ORDER BY SUM(op.quantity) DESC
    """)
    Page<PopularProductDtoResponse> findPopularProductsByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
}
