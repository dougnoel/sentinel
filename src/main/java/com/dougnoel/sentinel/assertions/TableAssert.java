package com.dougnoel.sentinel.assertions;

import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.junit.Assert;

import java.util.concurrent.Callable;

public class TableAssert {
    /**
     * protected constructor since this is a static class
     */
    protected TableAssert(){

    }

    /**
     * Assert a table method returns true.
     * @param table Table the table object.
     * @param booleanCallable Callable&lt;Boolean&gt; A callable function (lambda) that returns true or false.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertTrue(Table table, Callable<Boolean> booleanCallable) throws Exception {
        assertTrue(null, table, booleanCallable);
    }

    /**
     * Assert a table method returns true.
     * @param message  String message to print out if the assertion fails.
     * @param table Table the table object.
     * @param booleanCallable Callable&lt;Boolean&gt; A callable function (lambda) that returns true or false.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertTrue(String message, Table table, Callable<Boolean> booleanCallable) throws Exception {
        if(Boolean.TRUE.equals(booleanCallable.call()))
            return; //continue test
        table.reset();
        Assert.assertTrue(message, booleanCallable.call());
    }

    /**
     * Assert a table method returns false.
     * @param table Table the table object.
     * @param booleanCallable Callable&lt;Boolean&gt; A callable function (lambda) that returns true or false.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertFalse(Table table, Callable<Boolean> booleanCallable) throws Exception {
        assertFalse(null, table, booleanCallable);
    }

    /**
     * Assert a table method returns false.
     * @param message  String message to print out if the assertion fails.
     * @param table Table the table object.
     * @param booleanCallable Callable&lt;Boolean&gt; A callable function (lambda) that returns true or false.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertFalse(String message, Table table, Callable<Boolean> booleanCallable) throws Exception {
        if(Boolean.FALSE.equals(booleanCallable.call()))
            return; //continue test
        table.reset();
        Assert.assertFalse(message, booleanCallable.call());
    }

    /**
     * Assert a table method returns an object equal to the expected object.
     * @param message String message to print out if the assertion fails.
     * @param table Table the table object.
     * @param expected Object the expected object to compare against
     * @param objectCallable Callable&lt;Object&gt; A callable function (lambda) that returns an object.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertEquals(String message, Table table, Object expected, Callable<Object> objectCallable) throws Exception {
        try{
            Assert.assertEquals(expected, objectCallable.call());
        }
        catch(AssertionError ae){
            table.reset();
            var actualResult = objectCallable.call();
            Assert.assertEquals(SentinelStringUtils.format("{} Found {}", message, actualResult), expected, actualResult);
        }
    }

    /**
     * Assert a table method returns an object equal to the expected object.
     * @param table Table the table object.
     * @param expected Object the expected object to compare against
     * @param objectCallable Callable&lt;Object&gt; A callable function (lambda) that returns an object.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertEquals(Table table, Object expected, Callable<Object> objectCallable) throws Exception{
        assertEquals(SentinelStringUtils.format("Expected {}.", expected.toString()), table, expected, objectCallable);
    }

    /**
     * Assert a table method returns an object unequal to the notExpected object.
     * @param message String message to print out if the assertion fails.
     * @param table Table the table object.
     * @param notExpected Object the not-expected object to compare against.
     * @param objectCallable Callable&lt;Object&gt; A callable function (lambda) that returns an object.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertNotEquals(String message, Table table, Object notExpected, Callable<Object> objectCallable) throws Exception {
        try{
            Assert.assertNotEquals(notExpected, objectCallable.call());
        }
        catch(AssertionError ae){
            table.reset();
            var actualResult = objectCallable.call();
            Assert.assertNotEquals(SentinelStringUtils.format("{} Found {}", message, actualResult), notExpected, actualResult);
        }
    }

    /**
     * Assert a table method returns an object unequal to the notExpected object.
     * @param table Table the table object.
     * @param notExpected Object the not-expected object to compare against.
     * @param objectCallable Callable&lt;Object&gt; A callable function (lambda) that returns an object.
     * @throws Exception if the assertion fails or the table method throws an exception.
     */
    public static void assertNotEquals(Table table, Object notExpected, Callable<Object> objectCallable) throws Exception{
        assertNotEquals(SentinelStringUtils.format("Expected to not find {}.", notExpected.toString()), table, notExpected, objectCallable);
    }
}
