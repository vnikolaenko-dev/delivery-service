package ru.don_polesie.back_end.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    NEW("Новый заказ"),
    PAYING("Ожидает оплаты"),
    MONEY_RESERVAITED("Деньги зарезервированы на сайте партнера, собираем заказ"),
    PAID("Заказ оплачен"),
    READY_FOR_DELIVERY("Заказ готов к доставке"),
    SHIPPED("Доставлен"),
    CANCELED("Отменен"),
    NEEDS_TO_BE_UPDATED("Needs to be updated"),
    ERROR_ON_PAYMENT("Error on payment");
    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromValue(String v) {
        for (OrderStatus s : values()) {
            if (s.value.equals(v)) return s;
        }
        throw new IllegalArgumentException("Unknown status: " + v);
    }
}
