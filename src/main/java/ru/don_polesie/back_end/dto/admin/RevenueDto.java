package ru.don_polesie.back_end.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueDto {
    private Long revenue;
    private String period;
}