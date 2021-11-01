package org.simulator.utility;

import org.junit.Assert;
import org.junit.Test;
import org.simulator.utility.utility.StringUtil;

public class StringUtilTest {
    @Test
    public void testEmpty() {
        String input = null;
        boolean result;
        result = StringUtil.isNullOrEmpty(input);
        Assert.assertEquals(true, result);

        input = "";
        result = StringUtil.isNullOrEmpty(input);
        Assert.assertEquals(true, result);


        input = "    ";
        result = StringUtil.isNullOrEmpty(input);
        Assert.assertEquals(false, result);

        input = "hi there";
        result = StringUtil.isNullOrEmpty(input);
        Assert.assertEquals(false, result);

    }
}
