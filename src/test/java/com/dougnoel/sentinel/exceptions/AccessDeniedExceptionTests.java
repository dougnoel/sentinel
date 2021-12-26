package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AccessDeniedExceptionTests {
	private static final File filePath = new File("src/");
	private static final String filePathAsString = filePath.getAbsoluteFile().toString();

	@Test
	public void correctMessageForAccessDeniedException() {
		try {
		throw new AccessDeniedException(filePath);
		}
		catch (AccessDeniedException e) {
			assertTrue("Expecting Default Name.", e.getMessage().contains(filePathAsString));	
		}
	}
	
}