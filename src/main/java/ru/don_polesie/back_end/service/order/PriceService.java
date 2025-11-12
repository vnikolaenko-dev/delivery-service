package ru.don_polesie.back_end.service.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.model.product.Product;

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
            BigDecimal pricePerKg = product.getPrice();
            cost = BigDecimal.valueOf(quantity / 1000).multiply(pricePerKg);
        } else cost = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        // Округление до 2 знаков после запятой
        return cost.setScale(2, RoundingMode.HALF_UP);
    }
}
