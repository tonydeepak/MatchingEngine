package org.simulator.orderbook;

import org.simulator.entity.OrderType;
import org.simulator.entity.Side;
import org.simulator.entity.StockOrder;
import org.simulator.entity.Trade;
import org.simulator.utility.utility.Logger;
import org.simulator.utility.utility.OrderUtility;
import org.simulator.utility.validator.OrderValidator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleOrderBook implements OrderBook {
    private final String symbol;
    private final NavigableMap<String, StockOrder> buyOrders = new TreeMap<>();
    private final NavigableMap<String, StockOrder> sellOrders = new TreeMap<>();
    private List<Trade> tradeExecutions = new ArrayList<>();
    private Iterator<StockOrder> orderIterator;

    public SimpleOrderBook(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    //support only adding of orders. amend/cancel not supported due to requirements
    public List<Trade> addOrder(StockOrder order) {
        if (OrderValidator.validateOrder(order)) {
            ackTrade(order);
            if (Side.BUY.equals(order.getSide())) {
                buyOrders.put(order.getOrderID(), order);
            } else {
                sellOrders.put(order.getOrderID(), order);
            }
        } else {
            rejectTrade(order);
        }
        return tradeExecutions;
    }

    public List<Trade> getExecutions() {
        return tradeExecutions;
    }

    public NavigableMap<String, StockOrder> getOrders(Side side) {
        if (Side.BUY.equals(side)) {
            return buyOrders;
        } else {
            return sellOrders;
        }
    }

    public void doInternalMatch() {
        for (Map.Entry<String, StockOrder> entry : buyOrders.entrySet()) {
            match(sellOrders.values().stream().sorted((o1, o2) -> {
                return o1.sort(o2);
            }).collect(Collectors.toList()), entry.getValue());
        }
        for (Map.Entry<String, StockOrder> entry : sellOrders.entrySet()) {
            match(buyOrders.values().stream().sorted((o1, o2) -> {
                return o2.reverseSort(o1);
            }).collect(Collectors.toList()), entry.getValue());
        }
    }

    private void match(List<StockOrder> orderList, StockOrder newOrder) {
        if (orderList == null || newOrder == null) {
            Logger.log("WARN: unexpected. recheck the parameters");
            return;
        }

        orderIterator = orderList.iterator();

        while (orderIterator.hasNext()) {
            StockOrder order = orderIterator.next();
            BigDecimal fillPrice = BigDecimal.ZERO;
            //if else is faster than switch case in my experience so using that
            if (newOrder.canFill(order)) {
                //though sides and price match - no other Limit order exists
                fillPrice = getPrice(order, newOrder);
                if (fillPrice.compareTo(BigDecimal.ZERO) >= 1) {
                    if (newOrder.getAmount() > 0) {// && newOrder.compareTo(order) >= 0) {
                        //partial fill case of new order
                        if (newOrder.getAmount() > order.getAmount()) {
                            generateTrade(order, order.getAmount(), fillPrice);
                            generateTrade(newOrder, order.getAmount(), fillPrice);
                            newOrder.reduceAmount(order.getAmount());
                            orderIterator.remove();
                        }
                        //full fill case of new order/partial fill of existing
                        else if (newOrder.getAmount() < order.getAmount()) {
                            generateTrade(order, newOrder.getAmount(), fillPrice);
                            generateTrade(newOrder, newOrder.getAmount(), fillPrice);
                            order.reduceAmount(newOrder.getAmount());
                            newOrder.reduceAmount(newOrder.getAmount());
                        }
                        //exact match
                        else {
                            generateTrade(order, newOrder.getAmount(), fillPrice);
                            generateTrade(newOrder, newOrder.getAmount(), fillPrice);
                            newOrder.reduceAmount(newOrder.getAmount());
                            orderIterator.remove();
                        }
                    }
                }
            }
        }
    }

    private BigDecimal getPrice(StockOrder o1, StockOrder o2) {
        if (OrderType.MARKET.equals(o1.getOrderType()) && OrderType.MARKET.equals(o2.getOrderType())) {
            List<StockOrder> limitOrders;
            if (Side.SELL.equals(o1.getSide())) {
                return getTopOfBookPrice(buyOrders);
            } else {
                return getTopOfBookPrice(sellOrders);
            }
        }
        return (o1.getPrice().compareTo(BigDecimal.ZERO) > 1) ? o1.getPrice() : o2.getPrice();
    }

    private BigDecimal getTopOfBookPrice(NavigableMap<String, StockOrder> orders) {
        List<StockOrder> limitOrders = orders.values().stream().filter(p -> p.getOrderType().equals(OrderType.LIMIT)).collect(Collectors.toList());
        if (limitOrders != null && !limitOrders.isEmpty()) {
            return limitOrders.get(0).getPrice();
        }
        return BigDecimal.ZERO;
    }

    //TODO Executions have to be separate entity and with no side
    private void generateTrade(StockOrder order, long amount, BigDecimal price) {
        if (amount > 0) {
            tradeExecutions.add(OrderUtility.buildFill(order, price, amount));
        }
    }

    private void rejectTrade(StockOrder order) {
        tradeExecutions.add(OrderUtility.buildRejectedTrade(order));
    }

    private void ackTrade(StockOrder order) {
        tradeExecutions.add(OrderUtility.buildAck(order));
    }
}

