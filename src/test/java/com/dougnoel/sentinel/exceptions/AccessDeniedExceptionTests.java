package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AccessDeniedExceptionTests {
	private static final File FILE = new File("src/");
	private static final String FILEPATH = FILE.getAbsoluteFile().toString();
	private static final String TEST_MESSAGE = "My test message.";
	private static final Throwable CAUSE = new Throwable("My throwable message.");
		
	@Test
	public void correctMessageForAccessDeniedException() {
		try {
		throw new AccessDeniedException(FILE);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH));	
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithCause() {
		try {
		throw new AccessDeniedException(CAUSE, FILE);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH));
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithMessage() {
		try {
		throw new AccessDeniedException(TEST_MESSAGE, FILE);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(TEST_MESSAGE));	
		}
	}
	
	@Test
	public void correctMessageForAccessDeniedExceptionWithMessageAndCause() {
		try {
		throw new AccessDeniedException(TEST_MESSAGE, CAUSE, FILE);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(TEST_MESSAGE));
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void getMessageForAccessDeniedException() {
		try {
		throw new AccessDeniedException(FILE);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting exception message to contain the full message.", e.getMessage().contains(" Access denied for file path: " + FILEPATH));	
		}
	}
	
	@Test
	public void filePathCorrectForAccessDeniedException() {
		try {
		throw new AccessDeniedException(FILE);
		}
		catch (AccessDeniedException e) {
			assertEquals("Expecting to see the same file path.", FILE, e.getFile());	
		}
	}

}