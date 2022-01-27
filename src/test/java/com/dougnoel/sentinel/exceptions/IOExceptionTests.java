package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.dougnoel.sentinel.strings.SentinelStringUtils;

public class IOExceptionTests {
	private static final String TEST_MESSAGE = "My test message.";
	private static final Throwable CAUSE = new Throwable(TEST_MESSAGE);

	@Test(expected = IOException.class)
	public void throwsAsIoException() {
		IOException e = new IOException();
		throw e;
	}

	@Test(expected = RuntimeException.class)
	public void throwsAsRuntime() throws Exception {
		RuntimeException e = new IOException();
		throw e;
	}

	@Test
	public void correctMessageForIoException() {
		try {
			throw new IOException();
		}
		catch (IOException e) {
			assertEquals("Expecting exception message to be null by default.", null, e.getMessage());
		}
	}

	@Test
	public void passedCauseIsRetained() {
		try {
			throw new IOException(CAUSE);
		}
		catch(IOException e) {
			assertEquals("Expecting exception cause set to be returned.", CAUSE, e.getCause());
		}
	}

	@Test
	public void passedMessageIsRetained() {
		String exceptionMessage = SentinelStringUtils.format("This is the message to return {}",TEST_MESSAGE);
		try {
			throw new IOException(exceptionMessage);
		}
		catch(IOException e) {
			assertEquals("Expecting exception message set to be returned.", exceptionMessage, e.getMessage());
		}
	}

	@Test
	public void passedCauseAndMessageIsRetained() {
		String exceptionMessage = SentinelStringUtils.format("This is the message to return {}",TEST_MESSAGE);
		try {
			throw new IOException(exceptionMessage, CAUSE);
		}
		catch(IOException e) {
			assertEquals("Expecting exception message set to be returned.", exceptionMessage, e.getMessage());
			assertEquals("Expecting exception cause set to be returned.", CAUSE, e.getCause());
		}
	}

	@Test
	public void asRuntimeExceptionIorrectMessageForIoException() {
		try {
			throw new IOException();
		}
		catch (RuntimeException e) {
			assertEquals("Expecting exception message to be null by default.", null, e.getMessage());
		}
	}

	@Test
	public void asRuntimeExceptionPassedCauseIsRetained() {
		try {
			throw new IOException(CAUSE);
		}
		catch(RuntimeException e) {
			assertEquals("Expecting exception cause set to be returned.", CAUSE, e.getCause());
		}
	}

	@Test
	public void asRuntimeExceptionPassedMessageIsRetained() {
		String exceptionMessage = SentinelStringUtils.format("This is the message to return {}",TEST_MESSAGE);
		try {
			throw new IOException(exceptionMessage);
		}
		catch(RuntimeException e) {
			assertEquals("Expecting exception message set to be returned.", exceptionMessage, e.getMessage());
		}
	}

	@Test
	public void asRuntimeExceptionPassedCauseAndMessageIsRetained() {
		String exceptionMessage = SentinelStringUtils.format("This is the message to return {}",TEST_MESSAGE);
		try {
			throw new IOException(exceptionMessage, CAUSE);
		}
		catch(RuntimeException e) {
			assertEquals("Expecting exception message set to be returned.", exceptionMessage, e.getMessage());
			assertEquals("Expecting exception cause set to be returned.", CAUSE, e.getCause());
		}
	}
}