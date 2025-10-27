package ru.don_polesie.back_end.model.basket;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class BasketProductId {
    private Long basketId;
    private Long productId;

    public BasketProductId() {}

    public BasketProductId(Long orderId, Long productId) {
        this.basketId = orderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketProductId that = (BasketProductId) o;
        return Objects.equals(basketId, that.basketId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basketId, productId);
    }
}
