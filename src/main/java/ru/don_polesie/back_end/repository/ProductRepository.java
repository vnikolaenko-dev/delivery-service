package ru.don_polesie.back_end.repository;


import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.don_polesie.back_end.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @NonNull
    Optional<Product> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);

    Page<Product> findAllByAmountGreaterThan(int amount, Pageable pageable);

    Optional<Product> findByBrandAndName(String brand, String name);


    @Query("SELECT p FROM Product p " +
            "WHERE (:brand IS NULL OR (LOWER(p.brand) LIKE CONCAT('%', LOWER(CAST(:brand AS string)), '%')))" +
            "AND (:id IS NULL OR p.id = :id)" +
            "AND  (:name IS NULL OR (LOWER(p.name) LIKE CONCAT('%', LOWER(CAST(:name AS string)), '%')))" +
            "ORDER BY p.id"
    )

    Page<Product> findProductsByParams(
            @Param("id") Long id,
            @Param("brand") String brand,
            @Param("name") String name,
            Pageable pageable
    );


    @Query("SELECT p FROM Product p WHERE " +
            "CAST(p.id AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchProductsByQuery(@Param("query") String query, Pageable pageable);
}
