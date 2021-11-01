package org.simulator.monitor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simulator.entity.*;
import org.simulator.orderbook.OrderBook;
import org.simulator.orderbook.SimpleOrderBookFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimulationOrderMonitorTest {
    SimpleOrderBookFactory simpleOrderBookFactory = new SimpleOrderBookFactory();
    String symbol = "0700.HK";
    List<StockOrder> result;
    List<StockOrder> forComparing;
    List<StockOrder> forExecutions;
    OrderBook simpleOrderBook;

    @Before
    public void init() {
        forComparing = new ArrayList<>();
        forExecutions = new ArrayList<>();
        simpleOrderBook = simpleOrderBookFactory.getOrderBook(symbol);
    }

    @Test
    public void testRejections() {
        List<Trade> executionResult;
        executionResult = simpleOrderBook.addOrder(null);
        Assert.assertEquals(1, executionResult.size());
        Assert.assertEquals(ActionType.REJECT, executionResult.get(0).getActionType());
        Assert.assertEquals(null, executionResult.get(0).getOrderID());
        Assert.assertEquals(null, executionResult.get(0).getSymbol());
        Assert.assertEquals(null, executionResult.get(0).getFillPrice());
        Assert.assertEquals(null, executionResult.get(0).getFillQuantity());

        executionResult = simpleOrderBook.addOrder(new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 2000000));
        Assert.assertEquals(2, executionResult.size());
        Assert.assertEquals(ActionType.REJECT, executionResult.get(1).getActionType());
        Assert.assertEquals("Order1", executionResult.get(1).getOrderID());
        Assert.assertEquals("0700.HK", executionResult.get(1).getSymbol());
        Assert.assertEquals(null, executionResult.get(1).getFillPrice());
        Assert.assertEquals(null, executionResult.get(1).getFillQuantity());
        executionResult = simpleOrderBook.addOrder(new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("610"), Side.SELL, 1000000));
        Assert.assertEquals(3, executionResult.size());
        Assert.assertEquals(ActionType.REJECT, executionResult.get(2).getActionType());
        Assert.assertEquals("Order2", executionResult.get(2).getOrderID());
        Assert.assertEquals("0700.HK", executionResult.get(2).getSymbol());
        Assert.assertEquals(null, executionResult.get(2).getFillPrice());
        Assert.assertEquals(null, executionResult.get(2).getFillQuantity());
    }

    //        #OrderID,Symbol,Price,Side,OrderQuantity
    //        Order1,0700.HK,610,Sell,20000
    //        Order2,0700.HK,610,Sell,10000
    //        Order3,0700.HK,610,Buy,10000
    @Test
    public void testMatchingScenario1() {
        List<Trade> executionResult;
        executionResult = simpleOrderBook.addOrder(new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000));
        Assert.assertEquals(1, executionResult.size());
        Assert.assertEquals(ActionType.ACK, executionResult.get(0).getActionType());
        Assert.assertEquals("Order1", executionResult.get(0).getOrderID());
        Assert.assertEquals("0700.HK", executionResult.get(0).getSymbol());
        Assert.assertEquals(null, executionResult.get(0).getFillPrice());
        Assert.assertEquals(null, executionResult.get(0).getFillQuantity());
        simpleOrderBook.addOrder(new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("610"), Side.SELL, 10000));
        simpleOrderBook.addOrder(new StockOrder(OrderType.LIMIT, "Order3", "0700.HK", new BigDecimal("610"), Side.SELL, 10000));
    }

//    @Test
//    public void testMatchingScenario1() {
//        simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 100, new BigDecimal("9.9")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 1000, new BigDecimal("10")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 100, new BigDecimal("10.1")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 50, new BigDecimal("9.9")));
//    }
//
//    @Test
//    public void testDoubleOrderAtSamePrice() {
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 50, new BigDecimal("10")));
//        Assert.assertEquals(0, result.size());
//        Assert.assertEquals(1, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 50, new BigDecimal("10")));
//        Assert.assertEquals(0, result.size());
//        Assert.assertEquals(2, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//    }
//
//    @Test
//    public void testDoubleOrderAtSamePriceFails() {
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 100, new BigDecimal("101")));
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 100, new BigDecimal("102")));
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 100, new BigDecimal("100")));
//    }
//
//
//    @Test
//    public void testPriceTimeOrder() {
//        result = simulationOrderMonitor.addOrder(stockOrder3_1);
//        forComparing.add(stockOrder3_1);
//        Assert.assertEquals(0, result.size());
//        Assert.assertEquals(1, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//        Assert.assertEquals(forComparing, simulationOrderMonitor.getOrders(Side.BUY));
//
//        result = simulationOrderMonitor.addOrder(stockOrder3_2);
//        forComparing.add(stockOrder3_2);
//        Assert.assertEquals(0, result.size());
//        Assert.assertEquals(2, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//        Assert.assertEquals(forComparing, simulationOrderMonitor.getOrders(Side.BUY));
//
//        result = simulationOrderMonitor.addOrder(stockOrder3_3);
//        forComparing.add(stockOrder3_3);
//        Assert.assertEquals(0, result.size());
//        Assert.assertEquals(3, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//        Assert.assertEquals(forComparing, simulationOrderMonitor.getOrders(Side.BUY));
//
//        forExecutions.add(stockOrder3_exec1);
//        forExecutions.add(stockOrder3_exec2);
//        forExecutions.add(stockOrder3_exec3);
//        result = simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 200, new BigDecimal("9.9")));
//        Assert.assertEquals(3, result.size());
//        //Assert.assertEquals(result, forExecutions);
//        Assert.assertEquals(1, simulationOrderMonitor.getOrders(Side.BUY).size());
//        Assert.assertEquals(0, simulationOrderMonitor.getOrders(Side.SELL).size());
//    }
//
//    @Test
//    public void testPriceTimeOrder2() {
//        simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 100, new BigDecimal("10")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 80, new BigDecimal("10")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.BUY, 100, new BigDecimal("9.9")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 200, new BigDecimal("9.9")));
//        simulationOrderMonitor.addOrder(new StockOrder(Side.SELL, 200, new BigDecimal("9.8")));
//    }
//
//    @Test
//    public void dummyData() {
//        simulationOrderMonitor.addOrder(null);
//    }
}