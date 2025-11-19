package ru.don_polesie.back_end.service.system;

import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceService {

    /**
     * Рассчитывает стоимость продукта в зависимости от типа товара
     *
     * @param product  товар для расчета стоимости
     * @param quantity количество товара в граммах (для весового)
     *                 или штуках (для штучного товара)
     * @return стоимость товара, округленная до 2 знаков после запятой
     */
    public BigDecimal calculateProductCost(Product product, int quantity) {
        BigDecimal cost;

        if (Boolean.TRUE.equals(product.getIsWeighted())) {
            // Для весового товара: (количество_грамм / 1000.0) * цена_за_кг
            BigDecimal weightInKg = BigDecimal.valueOf(quantity).divide(BigDecimal.valueOf(1000), 10, RoundingMode.HALF_UP);
            cost = weightInKg.multiply(product.getPrice());
        } else {
            // Для штучного товара: цена * количество
            cost = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }

        // Применение скидки
        if (product.getSale() != null && product.getSale() > 0) {
            BigDecimal discountMultiplier = BigDecimal.ONE
                    .subtract(BigDecimal.valueOf(product.getSale()).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
            cost = cost.multiply(discountMultiplier);
        }

        // Округление до 2 знаков после запятой
        return cost.setScale(2, RoundingMode.HALF_UP);
    }
}