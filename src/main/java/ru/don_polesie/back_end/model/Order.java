package ru.don_polesie.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.don_polesie.back_end.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Setter
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Указано имя колонки
    private User user;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("product ASC")
    private Set<OrderProduct> orderProducts = new HashSet<>();

    @Setter
    @Column(unique = true)
    private String paymentId;

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
