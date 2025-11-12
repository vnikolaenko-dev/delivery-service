package ru.don_polesie.back_end.dto.order.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreatedDtoResponse {
    private OrderDtoResponse order;
    private JsonNode payment;
}

