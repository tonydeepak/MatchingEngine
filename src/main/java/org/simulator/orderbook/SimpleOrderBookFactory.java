package org.simulator.orderbook;

import java.util.HashMap;
import java.util.Map;

public class SimpleOrderBookFactory implements OrderBookFactory {
    private final Map<String, OrderBook> orderBooks;

    public SimpleOrderBookFactory() {
        this.orderBooks = new HashMap<>();
    }

    @Override
    public OrderBook getOrderBook(String symbol) {
        OrderBook result = orderBooks.get(symbol);
        if (result == null) {
            result = new SimpleOrderBook(symbol);
        }
        orderBooks.put(symbol, result);
        return result;
    }

    @Override
    public Map<String, OrderBook> getOrderBooks() {
        return orderBooks;
    }
}
