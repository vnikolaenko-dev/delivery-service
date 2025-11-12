package ru.don_polesie.back_end.model.basket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.don_polesie.back_end.model.user.User;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Указано имя колонки
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("product ASC")
    private Set<BasketProduct> basketProducts = new HashSet<>();

    public void addProduct(BasketProduct op) {
        basketProducts.add(op);
        op.setBasket(this);
    }

    public void removeProduct(BasketProduct op) {
        basketProducts.remove(op);
        op.setBasket(null);
    }
}
