package ru.don_polesie.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.model.enums.OrderStatus;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {
    private List<OrderItemDto> items;
}
