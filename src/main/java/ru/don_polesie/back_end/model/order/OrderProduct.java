package ru.don_polesie.back_end.model.order;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.don_polesie.back_end.model.product.Product;

@Getter
@Setter
@Entity
@Table(name = "order_products")
@NoArgsConstructor
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderProduct(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderProductId();
    }

}

