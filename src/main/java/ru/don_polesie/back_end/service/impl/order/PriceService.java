package ru.don_polesie.back_end.service.impl.order;

import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceService {

    /**
     * Рассчитывает стоимость продукта в зависимости от типа товара
     *
     * @param product       товар для расчета стоимости
     * @param quantity количество товара в граммах (для весового)
     *                      или штуках (для штучного товара)
     * @return стоимость товара, округленная до 2 знаков после запятой
     */
    public BigDecimal calculateProductCost(Product product, int quantity) {
        BigDecimal cost;

        if (Boolean.TRUE.equals(product.getIsWeighted())) {
            // Для весового товара: (количество_грамм / 1000) * цена_за_кг
            double pricePerKg = product.getPrice();
            double costValue = ((double) quantity / 1000) * pricePerKg;
            cost = BigDecimal.valueOf(costValue);
        } else {
            // Для штучного товара: цена * количество
            cost = BigDecimal.valueOf(product.getPrice() * quantity);
        }

        // Округление до 2 знаков после запятой
        return cost.setScale(2, RoundingMode.HALF_UP);
    }
}
