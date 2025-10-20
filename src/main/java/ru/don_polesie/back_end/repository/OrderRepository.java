package ru.don_polesie.back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts WHERE o.id = :id")
    Optional<Order> findById(@Param("id") Long id);

    void deleteById(Long integer);

    // Количество продаж за день выбранного товара
    @Query("SELECT COALESCE(SUM(CAST(op.quantity AS long)), 0) " +
            "FROM OrderProduct op " +
            "WHERE op.product.id = :productId " +
            "AND FUNCTION('DATE', op.order.createdAt) = FUNCTION('DATE', :date)")
    Long getTotalSalesForProductByDate(@Param("productId") Long productId, @Param("date") Instant date);

    // Количество заказов за день, содержащих выбранный товар
    @Query("SELECT COUNT(DISTINCT op.order.id) " +
            "FROM OrderProduct op " +
            "WHERE op.product.id = :productId " +
            "AND FUNCTION('DATE', op.order.createdAt) = FUNCTION('DATE', :date)")
    Long getOrderCountForProductByDate(@Param("productId") Long productId, @Param("date") Instant date);

    Page<Order> findByUserUsername(String username, Pageable pageable);

    List<Order> findByStatusAndPaymentIdIsNotNull(OrderStatus status);

    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    // Можно обернуть в удобный метод для "PAID"
    default Page<Order> findAllPaid(Pageable pageable) {
        return findAllByStatus(OrderStatus.PAID, pageable);
    }

    default Page<Order> findAllMoneyRESERVAITED(PageRequest pageable){
        return findAllByStatus(OrderStatus.MONEY_RESERVAITED, pageable);
    }
}
