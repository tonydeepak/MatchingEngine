package org.simulator.orderbook;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simulator.entity.OrderType;
import org.simulator.entity.Side;
import org.simulator.entity.StockOrder;

import java.math.BigDecimal;
import java.util.Map;

public class SimpleOrderBookFactoryTest {
    SimpleOrderBookFactory simpleOrderBookFactory;

    @Before
    public void initialize() {
        simpleOrderBookFactory = new SimpleOrderBookFactory();
    }

    @Test
    public void testInit() {
        OrderBook symbol1Book = simpleOrderBookFactory.getOrderBook("Symbol1");
        Assert.assertNotNull(symbol1Book);
        Assert.assertEquals("Symbol1", symbol1Book.getSymbol());
        Assert.assertEquals(0, symbol1Book.getExecutions().size());
        Assert.assertEquals(0, symbol1Book.getOrders(Side.BUY).size());
        Assert.assertEquals(0, symbol1Book.getOrders(Side.SELL).size());
        OrderBook symbol2Book = simpleOrderBookFactory.getOrderBook("Symbol2");
        Assert.assertEquals("Symbol2", symbol2Book.getSymbol());
        Assert.assertEquals(0, symbol2Book.getExecutions().size());
        Assert.assertEquals(0, symbol2Book.getOrders(Side.BUY).size());
        Assert.assertEquals(0, symbol2Book.getOrders(Side.SELL).size());
        OrderBook symbol3Book = simpleOrderBookFactory.getOrderBook("Symbol3");
        Assert.assertEquals("Symbol3", symbol3Book.getSymbol());
        Assert.assertEquals(0, symbol3Book.getExecutions().size());
        Assert.assertEquals(0, symbol3Book.getOrders(Side.BUY).size());
        Assert.assertEquals(0, symbol3Book.getOrders(Side.SELL).size());
    }

    @Test
    public void testGetBook() {
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "Symbol1", new BigDecimal("610"), Side.SELL, 20000);
        OrderBook symbol1Book = simpleOrderBookFactory.getOrderBook("Symbol1");
        symbol1Book.addOrder(order1);
        Assert.assertEquals(1, symbol1Book.getOrders(Side.SELL).size());
        symbol1Book = simpleOrderBookFactory.getOrderBook("Symbol1");
        Assert.assertEquals(1, symbol1Book.getOrders(Side.SELL).size());
    }

    @Test
    public void testGetBooks() {
        simpleOrderBookFactory.getOrderBook("Symbol1");
        simpleOrderBookFactory.getOrderBook("Symbol2");
        simpleOrderBookFactory.getOrderBook("Symbol3");
        Map<String, OrderBook> result = simpleOrderBookFactory.getOrderBooks();
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testGetBooksOnStartUp() {
        Map<String, OrderBook> result = simpleOrderBookFactory.getOrderBooks();
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }
}
