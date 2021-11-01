package org.simulator.validator;

import org.junit.Assert;
import org.junit.Test;
import org.simulator.entity.OrderType;
import org.simulator.entity.Side;
import org.simulator.entity.StockOrder;
import org.simulator.utility.validator.OrderValidator;

import java.math.BigDecimal;

public class OrderValidatorTest {
    @Test
    public void testGood() {
        boolean result;
        StockOrder order1 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("500"), Side.BUY, 20000);
        StockOrder order2 = new StockOrder(OrderType.LIMIT, "Order1", "0700.HK", new BigDecimal("500.01"), Side.BUY, 1000001);
        
        result = OrderValidator.validateOrder(null);
        Assert.assertEquals(false, result);

        result = OrderValidator.validateOrder(order1);
        Assert.assertEquals(true, result);

        result = OrderValidator.validateOrder(order2);
        Assert.assertEquals(false, result);

    }
}
