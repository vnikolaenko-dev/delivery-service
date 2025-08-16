package ru.don_polesie.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.don_polesie.back_end.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "orders")
public class Order {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Setter
    @Getter
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Setter
    @Getter
    @Column(name = "address", nullable = false)
    private String address;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Getter
    @Setter
    @Column(nullable = false, length = 64)
    private String guestId;

    @Getter
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Getter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("product.id ASC")
    private Set<OrderProduct> orderProducts = new HashSet<>();

    public Order() {}

    public void addProduct(OrderProduct op) {
        orderProducts.add(op);
        op.setOrder(this);
    }

    public void removeProduct(OrderProduct op) {
        orderProducts.remove(op);
        op.setOrder(null);
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
