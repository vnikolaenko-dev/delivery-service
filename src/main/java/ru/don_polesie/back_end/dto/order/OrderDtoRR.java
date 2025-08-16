package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.don_polesie.back_end.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoRR {
    private Long id;
    private Long totalAmount;
    private String phoneNumber;
    private String address;
    private OrderStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderItemDto> items;
}
