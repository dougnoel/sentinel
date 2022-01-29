package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MalformedURLExceptionTests {
	private static final String TEST_MESSAGE = "My test message.";
	private static final java.net.MalformedURLException CAUSE = new java.net.MalformedURLException();
		
	@Test(expected = MalformedURLException.class)
	public void ExceptionThrownWithoutParameters() {
		throw new MalformedURLException();
	}
	
	@Test
	public void WrappingJavaException() {
		try {
			throw new MalformedURLException(CAUSE);
		} catch (MalformedURLException e) {
			assertEquals("Expecting exception cause to be java.net.MalformedURLException", CAUSE, e.getCause());
		}
	}

	@Test
	public void ExceptionThrownWithMessage() {
		try {
			throw new MalformedURLException(TEST_MESSAGE);
		} catch (MalformedURLException e) {
			assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
		}
	}

	@Test
	public void ExceptionThrownWithMessageAndCause() {
		try {
			throw new MalformedURLException(TEST_MESSAGE, CAUSE);
		} catch (MalformedURLException e) {
			assertEquals("Expecting custom exception message.", TEST_MESSAGE, e.getMessage());
			assertEquals("Expecting exception cause to be java.net.MalformedURLException", CAUSE, e.getCause());
		}
	}
	
}