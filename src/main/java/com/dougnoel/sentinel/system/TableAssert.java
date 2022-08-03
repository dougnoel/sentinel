package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.elements.tables.Table;
import org.junit.Assert;

import java.util.concurrent.Callable;

public class TableAssert {

    public static void assertTrue(Table table, Callable<Boolean> booleanCallable) throws Exception {
        assertTrue(null, table, booleanCallable);
    }

    public static void assertTrue(String message, Table table, Callable<Boolean> booleanCallable) throws Exception {
        if(booleanCallable.call())
            return; //continue test
        table.reset();
        Assert.assertTrue(message, booleanCallable.call());
    }

    public static void assertFalse(Table table, Callable<Boolean> booleanCallable) throws Exception {
        assertFalse(null, table, booleanCallable);
    }

    public static void assertFalse(String message, Table table, Callable<Boolean> booleanCallable) throws Exception {
        if(!booleanCallable.call())
            return; //continue test
        table.reset();
        Assert.assertFalse(message, booleanCallable.call());
    }
}
