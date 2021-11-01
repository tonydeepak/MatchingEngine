package org.simulator.entity;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TradeTest {
    @Test
    public void testGood() {
        Trade result = new Trade(OrderType.LIMIT, ActionType.FILL, "Order1", "0700.HK", new BigDecimal("610"), Side.SELL, 20000, new BigDecimal("1000"), new BigDecimal("1000"));
        Assert.assertNotNull(result);
        Assert.assertEquals(OrderType.LIMIT, result.getOrderType());
        Assert.assertEquals(ActionType.FILL, result.getActionType());
        Assert.assertEquals(new BigDecimal("1000"), result.getFillPrice());
        Assert.assertEquals(new BigDecimal("1000"), result.getFillQuantity());
        Assert.assertEquals("Order1", result.getOrderID());
        Assert.assertEquals("0700.HK", result.getSymbol());
        Assert.assertNotNull(result.toString());
        String csv = result.toCSV(",");
        Assert.assertNotNull(csv);
        String[] csvColumns = csv.split(",");
        Assert.assertNotNull(csvColumns);
        Assert.assertEquals(8, csvColumns.length);
    }
}
