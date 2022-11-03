package com.dougnoel.sentinel.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberFormatExceptionTests {
    private static final String TEST_MESSAGE = "Number Format Exception Test Message";
    private static final java.lang.NumberFormatException CAUSE = new java.lang.NumberFormatException();

    @Test(expected = NumberFormatException.class)
    public void ExceptionThrownWithoutParameters() {
        throw new NumberFormatException();
    }

    @Test
    public void WrappingJavaException() {
        try {
            throw new NumberFormatException(CAUSE);
        } catch (NumberFormatException e) {
            assertEquals("Expecting exception cause to be java.net.NumberFormatException", CAUSE, e.getCause());
        }
    }

    @Test
    public void ExceptionThrownWithMessage() {
        try {
            throw new NumberFormatException(TEST_MESSAGE);
        } catch (NumberFormatException e) {
            assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void ExceptionThrownWithMessageAndCause() {
        try {
            throw new NumberFormatException(TEST_MESSAGE, CAUSE);
        } catch (NumberFormatException e) {
            assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
            assertEquals("Expecting exception cause to be java.net.NumberFormatException", CAUSE, e.getCause());
        }
    }
}