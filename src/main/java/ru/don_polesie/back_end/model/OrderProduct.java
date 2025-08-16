package ru.don_polesie.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_products")
public class OrderProduct {
    @Getter
    @EmbeddedId
    private OrderProductId id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Getter
    @Setter
    @Column(name = "quantity", nullable = false)
    private String quantity = "1";


    public OrderProduct() {}


    public OrderProduct(Order order, Product product, String quantity) {
        this.order = order;
        this.product = product;
        this.id = new OrderProductId(order.getId(), product.getId());
        this.quantity = quantity;
    }


}

