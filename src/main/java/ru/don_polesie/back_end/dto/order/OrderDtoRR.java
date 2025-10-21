package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoRR {
    private Long id;
    private String phoneNumber;
    private AddressDTO address;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus status;
    private List<OrderItemDto> items;
}
