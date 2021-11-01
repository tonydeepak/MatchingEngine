package org.simulator.utility.validator;

import org.simulator.entity.StockOrder;

public class OrderValidator {
    public static boolean validateOrder(StockOrder order) {
        if (order == null) {
            return false;
        }
        //check for size
        if (order.getAmount() >= 1000_000) {
            return false;
        } else {
            return true;
        }
    }
}
