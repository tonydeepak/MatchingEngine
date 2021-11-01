package org.simulator.entity;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
    LIMIT("L"),
    MARKET("MKT"),
    UNKNOWN("U");

    private final String value;

    private static Map<String, OrderType> lookup = new HashMap<String, OrderType>();

    static {
        for (OrderType value : OrderType.values()) {
            lookup.put(value.value, value);
        }
    }

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderType parse(String input) {
        if (input != null && !input.isEmpty()) {
            return lookup.get(input.toUpperCase());
        }
        return OrderType.UNKNOWN;
    }
}
