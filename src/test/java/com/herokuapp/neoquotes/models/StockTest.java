package com.herokuapp.neoquotes.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.AbstractUnitTest;

@Transactional
public class StockTest extends AbstractUnitTest {

    @Test
    public void testNameNotNullUsingConstructor() {
        Stock se = new Stock("NASDAQ");
        assertNotNull(se.getName());
    }

    @Test
    public void testAddListedCompanies() {
        Stock se = new Stock("NASDAQ");
        se.addCompany(new Company("Apple Computers"));
        se.addCompany(new Company("Microsoft"));
        se.addCompany(new Company("Intel Corp."));
        assertEquals(3, se.getCompanies().size());
    }
  
}
