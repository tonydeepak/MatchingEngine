package org.simulator.entity;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockOrderTest {
    @Test
    public void testGood() {
        StockOrder result = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000);
        Assert.assertNotNull(result);
        Assert.assertEquals(OrderType.LIMIT, result.getOrderType());
        Assert.assertEquals(20000, result.getAmount());
        Assert.assertEquals(new BigDecimal("610"), result.getPrice());
        Assert.assertEquals(Side.SELL, result.getSide());
        Assert.assertEquals("Order1", result.getOrderID());
        Assert.assertEquals("0700.HK", result.getSymbol());
    }

    @Test
    public void testLimitCanFill() {
        boolean result;
        // S 610 vs B 610
        // S vs B at same price
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000);
        StockOrder order2 = new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("610"), Side.BUY, 20000);

        result = order1.canFill(order2);
        Assert.assertEquals(true, result);

        result = order2.canFill(order1);
        Assert.assertEquals(true, result);

        // S 610 vs B 609
        // S HIGH vs B LOW
        // one way it will fill
        order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000);
        order2 = new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("609"), Side.BUY, 20000);

        result = order1.canFill(order2);
        Assert.assertEquals(true, result);

        result = order2.canFill(order1);
        Assert.assertEquals(true, result);

        // S 609 vs B 610
        // S LOW vs B HIGH
        // cannot fill
        order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("609"), Side.SELL, 20000);
        order2 = new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("610"), Side.BUY, 20000);

        result = order1.canFill(order2);
        Assert.assertEquals(false, result);

        result = order2.canFill(order1);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testMarketCanFill() {
        boolean result;
        //Test Market against Market, thought its allowed, we block when getting price as per sample E
        StockOrder order1 = new StockOrder(OrderType.MARKET, "Order1", "0700.HK", BigDecimal.ZERO, Side.SELL, 20000);
        StockOrder order2 = new StockOrder(OrderType.MARKET, "Order2", "0700.HK", BigDecimal.ZERO, Side.BUY, 20000);

        result = order1.canFill(order2);
        Assert.assertEquals(true, result);

        result = order2.canFill(order1);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testMarketVsLimitCanFill() {
        boolean result;
        // S 610 vs B M
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000);
        StockOrder order2 = new StockOrder(OrderType.MARKET, "Order2", "0700.HK", BigDecimal.ZERO, Side.BUY, 10000);

        //S L VS B M
        //B M Vs S L
        result = order1.canFill(order2);
        Assert.assertEquals(true, result);

        result = order2.canFill(order1);
        Assert.assertEquals(true, result);
//        if (Side.BUY.equals(againstOrder.side)) {
//            if (this.amount > 0 && againstOrder.compareTo(this) >= 0) {
//                return true;
//            }
//        } else {
//            if (this.amount > 0 && this.compareTo(againstOrder) >= 0) {
//                return true;
//            }
//        }

        //S M VS B L
        //B L Vs S M
        // S M vs B 610
        order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("610"), Side.BUY, 20000);
        order2 = new StockOrder(OrderType.MARKET, "Order2", "0700.HK", BigDecimal.ZERO, Side.SELL, 10000);

        result = order1.canFill(order2);
        Assert.assertEquals(true, result);

        result = order2.canFill(order1);
        Assert.assertEquals(true, result);
        //works for this
//        if (Side.BUY.equals(againstOrder.side)) {
//            if (this.amount > 0 && againstOrder.compareTo(this) <= 0) {
//                return true;
//            }
//        } else {
//            if (this.amount > 0 && this.compareTo(againstOrder) <= 0) {
//                return true;
//            }
//        }
    }

    @Test
    public void testSort() {
        //test buy side sorting
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("500"), Side.BUY, 20000);
        StockOrder order2 = new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("600"), Side.BUY, 10000);
        StockOrder order3 = new StockOrder(OrderType.LIMIT, "Order3", "0700.HK", new BigDecimal("700"), Side.BUY, 10000);
        ArrayList<StockOrder> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        List<StockOrder> result = orders.stream().sorted((o1, o2) -> {
            return o1.sort(o2);
        }).collect(Collectors.toList());

        Assert.assertEquals(3, result.size());
        Assert.assertEquals(new BigDecimal("700"), result.get(0).getPrice());
        Assert.assertEquals(new BigDecimal("600"), result.get(1).getPrice());
        Assert.assertEquals(new BigDecimal("500"), result.get(2).getPrice());
    }

    @Test
    public void testReverseSort() {
        //test sell side sorting
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("700"), Side.BUY, 20000);
        StockOrder order2 = new StockOrder(OrderType.LIMIT, "Order2", "0700.HK", new BigDecimal("600"), Side.BUY, 10000);
        StockOrder order3 = new StockOrder(OrderType.LIMIT, "Order3", "0700.HK", new BigDecimal("500"), Side.BUY, 10000);
        ArrayList<StockOrder> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        List<StockOrder> result = orders.stream().sorted((o1, o2) -> {
            return o1.reverseSort(o2);
        }).collect(Collectors.toList());

        Assert.assertEquals(3, result.size());
        Assert.assertEquals(new BigDecimal("500"), result.get(0).getPrice());
        Assert.assertEquals(new BigDecimal("600"), result.get(1).getPrice());
        Assert.assertEquals(new BigDecimal("700"), result.get(2).getPrice());
    }

    @Test
    public void testToString() {
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("700"), Side.BUY, 20000);
        Assert.assertNotNull(order1.toString());
    }

}
