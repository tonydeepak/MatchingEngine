package org.simulator.utility;

import org.junit.Assert;
import org.junit.Test;
import org.simulator.entity.OrderType;
import org.simulator.entity.StockOrder;
import org.simulator.utility.utility.OrderUtility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderUtilityTest {
    @Test
    public void testNullOrEmpty() {
        StockOrder result = OrderUtility.parse(null);
        Assert.assertNull(result);

        result = OrderUtility.parse(new ArrayList<>());
        Assert.assertNull(result);

    }

    @Test
    public void testGood() {
        String[] input = {"Order1", "0700.HK", "610", "Sell", "20000"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNotNull(result);
    }

    @Test
    public void testLessRows() {
        String[] input = {"Order1", "0700.HK", "Sell", "20000"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNull(result);
    }

    @Test
    public void testMoreRowsAddedAtEnd() {
        String[] input = {"Order1", "0700.HK", "610", "Sell", "20000", "1234"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNotNull(result);
    }

    @Test
    public void testPriceDataType() {
        String[] input = {"Order1", "0700.HK", "abc", "Sell", "20000"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNull(result);

        String[] input1 = {"Order1", "0700.HK", "MKT", "Sell", "20000"};
        result = OrderUtility.parse(Arrays.asList(input1));
        Assert.assertNotNull(result);
        Assert.assertEquals(OrderType.MARKET, result.getOrderType());
        Assert.assertEquals(BigDecimal.ZERO, result.getPrice());
    }

    @Test
    //TODO change later for side
    public void testSideDataType() {
        String[] input = {"Order1", "0700.HK", "610", "Yell", "20000"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNotNull(result);
    }

    @Test
    public void testAmountDataType() {
        //precision amounts not allowed for now
        String[] input = {"Order1", "0700.HK", "610", "Sell", "20000.01"};
        StockOrder result = OrderUtility.parse(Arrays.asList(input));
        Assert.assertNull(result);
    }

}
