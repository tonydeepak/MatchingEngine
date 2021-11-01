package org.simulator.orderbook;

import org.simulator.entity.Side;
import org.simulator.entity.StockOrder;
import org.simulator.entity.Trade;

import java.util.List;
import java.util.NavigableMap;

public interface OrderBook {
    List<Trade> addOrder(StockOrder order);

    NavigableMap<String, StockOrder> getOrders(Side side);

    void doInternalMatch();

    List<Trade> getExecutions();

    String getSymbol();
}
