package ru.don_polesie.back_end.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    NEW("New"),
    PROCESSING("Processing"),
    NEEDS_TO_BE_UPDATED("Needs to be updated"),
    PAID("Paid"),
    SHIPPED("Shipped"),
    PAYING("On payment"),
    CANCELLED("Cancelled");

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
