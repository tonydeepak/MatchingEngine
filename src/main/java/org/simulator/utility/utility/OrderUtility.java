package org.simulator.utility.utility;

import org.simulator.Constants;
import org.simulator.entity.*;

import java.math.BigDecimal;
import java.util.List;

public class OrderUtility {
    public static Trade buildRejectedTrade(StockOrder order) {
        if (order != null) {
            return new Trade(order.getOrderType(), ActionType.REJECT, order.getOrderID(), order.getSymbol(), order.getPrice(), order.getSide(), order.getAmount(), null, null);
        } else {
            return new Trade(OrderType.UNKNOWN, ActionType.REJECT, null, null, null, null, 0, null, null);
        }
    }

    public static Trade buildAck(StockOrder order) {
        if (order != null) {
            return new Trade(order.getOrderType(), ActionType.ACK, order.getOrderID(), order.getSymbol(), order.getPrice(), order.getSide(), order.getAmount(), null, null);
        }
        return null;
    }

    public static Trade buildFill(StockOrder order, BigDecimal fillPrice, long fillQuantity) {
        if (order != null) {
            return new Trade(order.getOrderType(), ActionType.FILL, order.getOrderID(), order.getSymbol(), order.getPrice(), order.getSide(), order.getAmount(), fillPrice, new BigDecimal(fillQuantity));
        }
        return null;
    }

    public static StockOrder parse(List<String> splits) {
        if (splits == null || splits.size() == 0) {
            return logErrorPattern();
        }
        try {
            String id = splits.get(0);
            String symbol = splits.get(1);
            String price = splits.get(2);
            String side = splits.get(3);
            String amt = splits.get(4);
            OrderType orderType = OrderType.LIMIT;
            Side parsedSide = Side.UNKNOWN;
            BigDecimal parsedPrice = BigDecimal.ZERO;

            if (StringUtil.isNullOrEmpty(id)) {
                Logger.log("id has problem");
                return logErrorPattern();
            } else {
                id = id.trim();
            }

            if (StringUtil.isNullOrEmpty(symbol)) {
                Logger.log("symbol has problem");
                return logErrorPattern();
            } else {
                symbol = symbol.trim();
            }

            try {
                if (!StringUtil.isNullOrEmpty(price) && Constants.MKT.equals(price.trim())) {
                    orderType = OrderType.MARKET;
                } else {
                    parsedPrice = new BigDecimal(price.trim());
                }
            } catch (Exception ex) {
                Logger.log("price has problem: " + ex);
                return logErrorPattern();
            }

            if (StringUtil.isNullOrEmpty(side)) {
                Logger.log("side has problem");
                return logErrorPattern();
            }

            try {
                parsedSide = Side.parse(side.trim());
            } catch (Exception ex) {
                Logger.log("side has problem");
                return logErrorPattern();
            }

            if (StringUtil.isNullOrEmpty(amt) && !(Long.valueOf(amt.trim()) > 0L)) {
                Logger.log("amount has problem");
                return logErrorPattern();
            }
            return new StockOrder(orderType, id, symbol, parsedPrice, parsedSide, Long.valueOf(amt));
        } catch (Exception ex) {
            Logger.log("Exception in parsing:" + ex);
            return logErrorPattern();
        }
    }

    public static StockOrder logErrorPattern() {
        Logger.log("CSV file definition does not match");
        Logger.log("#OrderID,Symbol,Price,Side,OrderQuantity");
        Logger.log("Order1,0700.HK,610,Sell,20000");
        return null;
    }
}
