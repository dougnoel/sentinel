package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IOExceptionTests {
	private static final String TEST_MESSAGE = "My test message.";
	private static final java.io.IOException CAUSE = new java.io.IOException();
		
	@Test(expected = IOException.class)
	public void ExceptionThrownWithoutParameters() {
		throw new IOException();
	}
	
	@Test
	public void WrappingJavaException() {
		try {
			throw new IOException(CAUSE);
		} catch (IOException e) {
			assertEquals("Expecting exception cause to be java.net.MalformedURLException", CAUSE, e.getCause());
		}
	}

	@Test
	public void ExceptionThrownWithMessage() {
		try {
			throw new IOException(TEST_MESSAGE);
		} catch (IOException e) {
			assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
		}
	}

	@Test
	public void ExceptionThrownWithMessageAndCause() {
		try {
			throw new IOException(TEST_MESSAGE, CAUSE);
		} catch (IOException e) {
			assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
			assertEquals("Expecting exception cause to be java.net.MalformedURLException", CAUSE, e.getCause());
		}
	}
	
}