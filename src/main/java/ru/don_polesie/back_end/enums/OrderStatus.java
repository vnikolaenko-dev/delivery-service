package ru.don_polesie.back_end.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    NEW("New"),
    READY_FOR_DELIVERY("READY_FOR_DELIVERY"),
    NEEDS_TO_BE_UPDATED("Needs to be updated"),
    PAYING("On payment"),
    PAID("Paid"),
    MONEY_RESERVAITED("MONEY_RESERVAITED"),
    SHIPPED("Shipped"),
    ERROR_ON_PAYMENT("Error on payment"),
    CANCELED("Canceled");

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
