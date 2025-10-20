package ru.don_polesie.back_end.dto.order;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreateResponse {
    private OrderDtoRR order;
    private JsonNode payment;
}

