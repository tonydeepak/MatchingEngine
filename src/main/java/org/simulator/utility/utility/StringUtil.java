package org.simulator.utility.utility;

public class StringUtil {
    public static boolean isNullOrEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        return false;
    }
}
