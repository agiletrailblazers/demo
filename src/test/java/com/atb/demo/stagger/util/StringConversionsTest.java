package com.atb.demo.stagger.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class StringConversionsTest {

    StringConversions stringConversions;

    @Before
    public void setUp() {
        stringConversions = new StringConversions();
    }

    @Test
    public void testNullAllowedValuesReturnsNull() throws Exception {
        assertNull(stringConversions.allowableValuesToString(null));
    }

}
