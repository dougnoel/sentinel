package com.dougnoel.sentinel.assertions;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTable;

public class TableAssertTests {
    private static Table table;

    @BeforeClass
    public static void setUpBeforeClass() {
        Time.reset();
        Configuration.update("timeout", 1);

        BaseSteps.navigateToPage("InternetTablesPage");
        table = getElementAsTable("table 1");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        Time.reset();
        Configuration.update("timeout", 10);
        Driver.quitAllDrivers();
    }

    @Test
    public void assertTrueSuccess() throws Exception {
        TableAssert.assertTrue(table, () -> table.getNumberOfRows() == 4);
    }

    @Test(expected=AssertionError.class)
    public void assertTrueFailure() throws Exception {
        TableAssert.assertTrue(table, () -> table.getNumberOfRows() == 1);
    }

    @Test
    public void assertFalseSuccess() throws Exception {
        TableAssert.assertFalse(table, () -> table.getNumberOfRows() == 3);
    }

    @Test(expected=AssertionError.class)
    public void assertFalseFailure() throws Exception {
        TableAssert.assertFalse(table, () -> table.getNumberOfRows() == 4);
    }

    @Test
    public void assertEqualsSuccess() throws Exception {
        TableAssert.assertEquals(table, 4, table::getNumberOfRows);
    }

    @Test(expected=AssertionError.class)
    public void assertEqualsFailure() throws Exception {
        TableAssert.assertEquals(table, 5, table::getNumberOfRows);
    }

    @Test
    public void assertNotEqualsSuccess() throws Exception {
        TableAssert.assertNotEquals(table, 2, table::getNumberOfRows);
    }

    @Test(expected=AssertionError.class)
    public void assertNotEqualsFailure() throws Exception {
        TableAssert.assertNotEquals(table, 4, table::getNumberOfRows);
    }
}
