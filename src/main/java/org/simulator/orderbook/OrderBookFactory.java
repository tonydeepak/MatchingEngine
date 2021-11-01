package org.simulator.orderbook;

import java.util.Map;

public interface OrderBookFactory {
    OrderBook getOrderBook(String symbol);

    Map<String, OrderBook> getOrderBooks();
}
