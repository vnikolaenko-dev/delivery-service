package ru.don_polesie.back_end.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.don_polesie.back_end.dto.order.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
}
