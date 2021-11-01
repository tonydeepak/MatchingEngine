package org.simulator.entity;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
    ACK("ACK"),
    FILL("FILL"),
    REJECT("REJECT"),
    UNKNOWN("U");

    private final String value;

    private static Map<String, ActionType> lookup = new HashMap<String, ActionType>();

    static {
        for (ActionType value : ActionType.values()) {
            lookup.put(value.value, value);
        }
    }

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ActionType parse(String input) {
        if (input != null && !input.isEmpty()) {
            return lookup.get(input.toUpperCase());
        }
        return ActionType.UNKNOWN;
    }
}
