package org.simulator.orderbook;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simulator.entity.ActionType;
import org.simulator.entity.OrderType;
import org.simulator.entity.Side;
import org.simulator.entity.StockOrder;

import java.math.BigDecimal;
import java.util.Map;

public class SimpleOrderBookTest {
    SimpleOrderBook simpleOrderBook;
    private static final String symbol = "Symbol1";
    private StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", symbol, new BigDecimal("610"), Side.SELL, 20000);
    private StockOrder order2 = new StockOrder(OrderType.LIMIT, "Order2", symbol, new BigDecimal("610"), Side.BUY, 20000);
    private StockOrder order3 = new StockOrder(OrderType.LIMIT, "Order3", symbol, new BigDecimal("640"), Side.BUY, 20000);
    private StockOrder marketOrder1 = new StockOrder(OrderType.MARKET, "Order1", symbol, new BigDecimal("610"), Side.SELL, 20000);
    private StockOrder marketOrder2 = new StockOrder(OrderType.MARKET, "Order2", symbol, new BigDecimal("610"), Side.BUY, 20000);

    @Before
    public void initialize() {
        simpleOrderBook = new SimpleOrderBook(symbol);
    }

    @Test
    public void testInit() {
        Assert.assertNotNull(simpleOrderBook);
        simpleOrderBook.addOrder(order1);
        Assert.assertEquals(1, simpleOrderBook.getExecutions().size());
        Assert.assertEquals(ActionType.ACK, simpleOrderBook.getExecutions().get(0).getActionType());
        Assert.assertEquals("Order1", simpleOrderBook.getExecutions().get(0).getOrderID());
        Assert.assertEquals(1, simpleOrderBook.getOrders(Side.SELL).size());
        Assert.assertEquals(0, simpleOrderBook.getOrders(Side.BUY).size());

        simpleOrderBook.addOrder(order2);
        Assert.assertEquals(2, simpleOrderBook.getExecutions().size());
        Assert.assertEquals(ActionType.ACK, simpleOrderBook.getExecutions().get(1).getActionType());
        Assert.assertEquals("Order2", simpleOrderBook.getExecutions().get(1).getOrderID());
        Assert.assertEquals(1, simpleOrderBook.getOrders(Side.SELL).size());
        Assert.assertEquals(1, simpleOrderBook.getOrders(Side.BUY).size());

        simpleOrderBook.addOrder(order3);
        Assert.assertEquals(3, simpleOrderBook.getExecutions().size());
        Assert.assertEquals(ActionType.ACK, simpleOrderBook.getExecutions().get(2).getActionType());
        Assert.assertEquals("Order3", simpleOrderBook.getExecutions().get(2).getOrderID());
        Assert.assertEquals(1, simpleOrderBook.getOrders(Side.SELL).size());
        Assert.assertEquals(2, simpleOrderBook.getOrders(Side.BUY).size());
    }

    @Test
    public void testDoMatch() {
        simpleOrderBook.addOrder(order1);
        simpleOrderBook.addOrder(order2);
        simpleOrderBook.addOrder(order3);
        simpleOrderBook.doInternalMatch();
        Assert.assertEquals(5, simpleOrderBook.getExecutions().size());
    }

    @Test
    public void testMarketMatch() {
        simpleOrderBook.addOrder(marketOrder1);
        simpleOrderBook.addOrder(marketOrder2);
        Assert.assertEquals(2, simpleOrderBook.getExecutions().size());
        simpleOrderBook.doInternalMatch();
        Assert.assertEquals(2, simpleOrderBook.getExecutions().size());
    }
}


