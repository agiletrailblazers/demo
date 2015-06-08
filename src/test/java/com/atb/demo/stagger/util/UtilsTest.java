package com.atb.demo.stagger.util;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class UtilsTest {

    @Test
    public void testNullAllowedValuesReturnsNull() throws Exception {
        assertNull(Utils.allowableValuesToString(null));
    }

}
