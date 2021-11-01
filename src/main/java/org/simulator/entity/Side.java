package org.simulator.entity;

import java.util.HashMap;
import java.util.Map;

public enum Side {
    BUY("BUY"),
    SELL("SELL"),
    UNKNOWN("U");

    private final String value;

    private static Map<String, Side> lookup = new HashMap<String, Side>();

    static {
        for (Side value : Side.values()) {
            lookup.put(value.value, value);
        }
    }

    Side(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Side parse(String input) {
        if (input != null && !input.isEmpty()) {
            return lookup.get(input.toUpperCase());
        }
        return Side.UNKNOWN;
    }
}
