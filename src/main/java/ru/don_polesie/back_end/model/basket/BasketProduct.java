package ru.don_polesie.back_end.model.basket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.don_polesie.back_end.model.order.OrderProductId;
import ru.don_polesie.back_end.model.product.Product;

@Getter
@Setter
@Entity
@Table(name = "basket_products")
@NoArgsConstructor
public class BasketProduct {
    @EmbeddedId
    private BasketProductId id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("basketId")
    @JoinColumn(name = "basket_id")
    @JsonIgnore
    private Basket basket;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public BasketProduct(Basket basket, Product product, Integer quantity) {
        this.basket = basket;
        this.product = product;
        this.quantity = quantity;
        this.id = new BasketProductId();
    }
}
