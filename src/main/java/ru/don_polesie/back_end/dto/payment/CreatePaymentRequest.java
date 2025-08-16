package ru.don_polesie.back_end.dto.payment;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String orderId;
}
