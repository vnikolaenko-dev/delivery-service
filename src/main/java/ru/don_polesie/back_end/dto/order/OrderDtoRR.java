package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.don_polesie.back_end.dto.user.AddressDTO;
import ru.don_polesie.back_end.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoRR {
    private Long id;
    private String phoneNumber;
    private AddressDTO address;
    private LocalDateTime  createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private List<OrderItemDto> items;
}
