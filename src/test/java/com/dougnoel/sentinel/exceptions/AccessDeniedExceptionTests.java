package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AccessDeniedExceptionTests {
	private static final File filePath = new File("src/");
	private static final String filePathAsString = filePath.getAbsoluteFile().toString();
	private static final String testMessage = "My test message.";
	private static final String causeMessage = "My throwable message.";
	private static final Throwable cause = new Throwable(causeMessage);
		
	@Test
	public void correctMessageForAccessDeniedException() {
		try {
		throw new AccessDeniedException(filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(filePathAsString));	
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithCause() {
		try {
		throw new AccessDeniedException(cause, filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(filePathAsString));
			assertEquals("Expecting exception cause to be what was passed", cause, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithMessage() {
		try {
		throw new AccessDeniedException(testMessage, filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(filePathAsString) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(testMessage));	
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithMessageAndCause() {
		try {
		throw new AccessDeniedException(testMessage, cause, filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(filePathAsString) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(testMessage));
			assertEquals("Expecting exception cause to be what was passed", cause, e.getCause());
		}
	}
	
	@Test
	public void getMessageForAccessDeniedException() {
		try {
		throw new AccessDeniedException(filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full message.", e.getMessage().contains(" Access denied for file path: " + filePathAsString));	
		}
	}
	
	@Test
	public void filePathCorrectForAccessDeniedException() {
		try {
		throw new AccessDeniedException(filePath);
		}
		catch (AccessDeniedException e) {
			assertEquals("Expecting to see the same file path.", filePath, e.getFilePath());	
		}
	}

}