package ru.don_polesie.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RevenueDto {
    private Long revenue;
    private String period;
}