package ru.don_polesie.back_end.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCountDto {
    private Long count;
    private String period;
}
