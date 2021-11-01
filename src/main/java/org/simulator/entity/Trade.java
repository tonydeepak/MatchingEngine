package org.simulator.entity;

import java.math.BigDecimal;


public class Trade {
    //marked final as we are not going to amend as per requirements
    private final String orderID;
    private final String symbol;
    private final ActionType actionType;
    private final OrderType orderType;
    private final Side side;
    private final BigDecimal price;
    private final long amount;
    private final BigDecimal fillPrice;
    private final BigDecimal fillQuantity;

    //validation is not happening inside entity
    public Trade(OrderType orderType, ActionType actionType, String orderID, String symbol, BigDecimal price, Side side, long amount, BigDecimal fillPrice, BigDecimal fillQuantity) {
        this.orderType = orderType;
        this.actionType = actionType;
        this.orderID = orderID;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.amount = amount;
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public BigDecimal getFillPrice() {
        return this.fillPrice;
    }

    public BigDecimal getFillQuantity() {
        return this.fillQuantity;
    }

    @Override
    public String toString() {
        return new StringBuilder("ID:=").append(orderID).append("  OrderType:=").append(orderType).append("  ActionType:=").append(actionType).append("  Symbol:=").append(symbol).append("  Side:=").append(side).append("  Amount:=").append(amount).append("  Price:=").append(price).append("  FillPrice:=").append(fillPrice).append("  FillQuantity:=").append(fillQuantity).toString();
    }

    public String toCSV(String delimitter) {
        return new StringBuilder(actionType.getValue()).append(delimitter)
                .append(orderID).append(delimitter)
                .append(symbol).append(delimitter)
                .append((OrderType.MARKET.equals(orderType)) ? orderType.getValue() : price).append(delimitter)
                .append(side.getValue()).append(delimitter)
                .append(amount).append(delimitter)
                .append(fillPrice).append(delimitter)
                .append(fillQuantity).toString();
    }
}
