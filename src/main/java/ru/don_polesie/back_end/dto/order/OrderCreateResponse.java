package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreateResponse {
    private OrderDtoRR order;
    private String guestToken;
}

