package com.atb.demo.stagger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataTypeFilterTest {

    DataTypeFilter dataTypeFilter;

    @Before
    public void setUp() {
        dataTypeFilter = new DataTypeFilter();
    }

    @Test
    public void validBasicDataTypeReturnsType() throws Exception {
        String ret = dataTypeFilter.filterBasicTypes("int");
        assertNull(ret);
    }

    @Test
    public void invalidBasicDataTypeReturnsNull() throws Exception {
        String ret = dataTypeFilter.filterBasicTypes("foo");
        assertNotNull(ret);
    }
}