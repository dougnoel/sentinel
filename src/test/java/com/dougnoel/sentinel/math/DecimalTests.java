package com.dougnoel.sentinel.math;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class DecimalTests {
    @Test
    public void returnFormattedInteger() {
        int testValue = Integer.MAX_VALUE;
        String output = Decimal.formatDecimal(testValue, 0, true);
        Assert.assertEquals(Integer.toString(testValue), output);
        output = Decimal.formatDecimal(testValue, 0, false);
        Assert.assertEquals(Integer.toString(testValue), output);
    }

    @Test
    public void returnFormattedIntegerPadded() {
        int testValue = Integer.MAX_VALUE;
        String output = Decimal.formatDecimal(testValue, 2, true);
        Assert.assertEquals(testValue + ".00", output);
        output = Decimal.formatDecimal(testValue, 2, false);
        Assert.assertEquals(testValue + ".00", output);
    }

    @Test
    public void returnFormattedDoubleRounded() {
        double testValue = 2.8005;
        String output = Decimal.formatDecimal(testValue, 3, true);
        Assert.assertEquals("2.801", output);
    }

    @Test
    public void returnFormattedDoubleTruncated() {
        double testValue = 2.8005;
        String output = Decimal.formatDecimal(testValue, 3, false);
        Assert.assertEquals("2.800", output);
    }

    @Test
    public void returnFormattedDoubleWholeNumber() {
        double testValue = 2.8005;
        String output = Decimal.formatDecimal(testValue, 0, true);
        Assert.assertEquals("3", output);
        output = Decimal.formatDecimal(testValue, 0, false);
        Assert.assertEquals("2", output);
    }

    @Test
    public void returnFormattedBigDecimalRounded() {
        BigDecimal testValue = BigDecimal.valueOf(2.8005);
        String output = Decimal.formatDecimal(testValue, 3, true);
        Assert.assertEquals("2.801", output);
    }

    @Test
    public void returnFormattedBigDecimalTruncated() {
        BigDecimal testValue = BigDecimal.valueOf(2.8005);
        String output = Decimal.formatDecimal(testValue, 3, false);
        Assert.assertEquals("2.800", output);
    }

    @Test
    public void returnFormattedBigDecimalWholeNumber() {
        BigDecimal testValue = BigDecimal.valueOf(2.8005);
        String output = Decimal.formatDecimal(testValue, 0, true);
        Assert.assertEquals("3", output);
        output = Decimal.formatDecimal(testValue, 0, false);
        Assert.assertEquals("2", output);
    }

    @Test
    public void returnFormattedLongRounded() {
        long testValue = Long.MAX_VALUE;
        String output = Decimal.formatDecimal(testValue, 3, true);
        Assert.assertEquals(testValue + ".000", output);
    }

    @Test
    public void returnFormattedLongTruncated() {
        long testValue = Long.MAX_VALUE;
        String output = Decimal.formatDecimal(testValue, 3, false);
        Assert.assertEquals(testValue + ".000", output);
    }

    @Test
    public void returnFormattedLongWholeNumber() {
        long testValue = Long.MAX_VALUE;
        String output = Decimal.formatDecimal(testValue, 0, true);
        Assert.assertEquals(Long.toString(testValue), output);
        output = Decimal.formatDecimal(testValue, 0, false);
        Assert.assertEquals(Long.toString(testValue), output);
    }
}