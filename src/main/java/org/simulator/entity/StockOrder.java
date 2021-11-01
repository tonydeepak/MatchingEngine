package org.simulator.entity;

import java.math.BigDecimal;


public class StockOrder implements Comparable<StockOrder> {
    protected String orderID;
    protected String symbol;
    protected Side side;
    protected BigDecimal price;
    protected long amount;
    protected OrderType orderType;

    //entity validation is not happening inside entity
    public StockOrder(OrderType orderType, String orderID, String symbol, BigDecimal price, Side side, long amount) {
        this.orderType = orderType;
        this.orderID = orderID;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.amount = amount;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Side getSide() {
        return this.side;
    }

    public long getAmount() {
        return this.amount;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public void reduceAmount(long input) {
        this.amount = this.amount - input;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public boolean canFill(StockOrder againstOrder) {
        //Market orders cannot be matched
//        if (this.orderType.equals(OrderType.MARKET) && againstOrder.getOrderType().equals(OrderType.MARKET)) {
//            return false;
//        }

        //S L VS B M
        //B M Vs S L
        if ((Side.SELL.equals(side) && OrderType.LIMIT.equals(orderType) && Side.BUY.equals(againstOrder.getSide()) && OrderType.MARKET.equals(againstOrder.getOrderType())) ||
                (Side.BUY.equals(side) && OrderType.MARKET.equals(orderType) && Side.SELL.equals(againstOrder.getSide()) && OrderType.LIMIT.equals(againstOrder.getOrderType()))) {
            if (Side.BUY.equals(againstOrder.side)) {
                if (this.amount > 0 && againstOrder.compareTo(this) >= 0) {
                    return true;
                }
            } else {
                if (this.amount > 0 && this.compareTo(againstOrder) >= 0) {
                    return true;
                }
            }
        }

        //S M VS B L
        //B L Vs S M
        if ((Side.SELL.equals(side) && OrderType.MARKET.equals(orderType) && Side.BUY.equals(againstOrder.getSide()) && OrderType.LIMIT.equals(againstOrder.getOrderType())) ||
                (Side.BUY.equals(side) && OrderType.LIMIT.equals(orderType) && Side.SELL.equals(againstOrder.getSide()) && OrderType.MARKET.equals(againstOrder.getOrderType()))) {
            if (Side.BUY.equals(againstOrder.side)) {
                if (this.amount > 0 && againstOrder.compareTo(this) <= 0) {
                    return true;
                }
            } else {
                if (this.amount > 0 && this.compareTo(againstOrder) <= 0) {
                    return true;
                }
            }
        }

        //L Vs L
        if (Side.BUY.equals(againstOrder.side)) {
            if (this.amount > 0 && againstOrder.compareTo(this) >= 0) {
                return true;
            }
        } else {
            if (this.amount > 0 && this.compareTo(againstOrder) >= 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int hash = 31;
        int result = 1;
        result = hash * result + side.hashCode();
        result = hash * result + price.hashCode();
        result = hash * result + Long.hashCode(amount);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StockOrder other = (StockOrder) obj;

        if (this.getSide() != other.getSide()) {
            return false;
        }
        if (this.getPrice() != other.getPrice()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(StockOrder input) {
//        if (Side.BUY.equals(input.getSide())) {
//            if (this.orderType.equals(OrderType.MARKET)) {
//                return -1;
//            }
//            if (input.getOrderType().equals(OrderType.MARKET)) {
//                return 1;
//            }
//        } else {
//            if (input.getOrderType().equals(OrderType.MARKET)) {
//                return -1;
//            }
//            if (this.orderType.equals(OrderType.MARKET)) {
//                return 1;
//            }
//        }
        return input.getPrice().compareTo(this.getPrice());
    }

    public int sort(StockOrder input) {
        if (this.orderType.equals(OrderType.MARKET)) {
            return -1;
        }
        if (input.getOrderType().equals(OrderType.MARKET)) {
            return 1;
        }
        return input.getPrice().compareTo(this.getPrice());
    }

    public int reverseSort(StockOrder input) {
        if (this.orderType.equals(OrderType.MARKET)) {
            return 1;
        }
        if (input.getOrderType().equals(OrderType.MARKET)) {
            return -1;
        }
        return this.getPrice().compareTo(input.getPrice());
    }

    @Override
    public String toString() {
        return new StringBuilder("ID:=").append(orderID).append("Symbol:=").append(symbol).append("Side:=").append(side).append("Amount:=").append(amount).append("Price:=").append(price).toString();
    }
}
