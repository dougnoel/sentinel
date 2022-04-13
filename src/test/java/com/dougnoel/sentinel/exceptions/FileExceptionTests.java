package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;

import org.junit.Test;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FileExceptionTests {
	private static final File FILE = new File("conf/sentinel.yml");
	private static final String FILEPATH = FILE.getAbsoluteFile().toString();
	private static final String FILENAME = FILE.getName();
	private static final String TEST_MESSAGE = "My test message.";
	private static final Throwable CAUSE = new Throwable(TEST_MESSAGE);
		
	@Test
	public void correctMessageForFileException() {
		try {
		throw new FileException(FILE);
		}
		catch (FileException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH));	
		}
	}

	@Test
	public void NoSuchMethodExceptionTest() {
		NoSuchMethodException exception = new NoSuchMethodException();
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} could not find suitable method (constructor, most likely) for element in this file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be NoSuchMethodException", exception, e.getCause());
		}
	}

	@Test
	public void InvocationTargetExceptionTest() {
		InvocationTargetException exception = new InvocationTargetException(new Exception(), "Test");
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} target invocation failure in this file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be InvocationTargetException", exception, e.getCause());
		}
	}

	@Test
	public void IllegalAccessExceptionTest() {
		IllegalAccessException exception = new IllegalAccessException();
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} illegal access failure in this file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be IllegalAccessException", exception, e.getCause());
		}
	}

	@Test
	public void InstantiationExceptionTest() {
		InstantiationException exception = new InstantiationException();
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} could not instantiate element in this file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be InstantiationException", exception, e.getCause());
		}
	}

	@Test
	public void ClassNotFoundExceptionTest() {
		ClassNotFoundException exception = new ClassNotFoundException();
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} could not find suitable class for element in this file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be ClassNotFoundException", exception, e.getCause());
		}
	}
	
	@Test
	public void JsonParseExceptionTest() throws JsonParseException, IOException {
		String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
		JsonFactory factory = new JsonFactory();
		JsonParser parser  = factory.createParser(carJson);
		JsonParseException exception = new JsonParseException(parser, FILEPATH);
		
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} is not a valid YAML file. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be JsonParseException", exception, e.getCause());
		}
	}
	
	@Test
	public void JsonMappingExceptionTest() throws JsonParseException, IOException {
		String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
		JsonFactory factory = new JsonFactory();
		JsonParser parser  = factory.createParser(carJson);
		JsonMappingException exception = new JsonMappingException(parser, FILEPATH);
		
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} has incorrect formatting and cannot be read. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be JsonMappingException", exception, e.getCause());
		}
	}
	
	@Test
	public void FileNotFoundExceptionTest() {
		FileNotFoundException exception = new FileNotFoundException(FILEPATH);
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} cannot be found in the specified location. {}",
					FILENAME, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be FileNotFoundException", exception, e.getCause());
		}
	}
	
	@Test
	public void AccessDeniedException() {
		AccessDeniedException exception = new AccessDeniedException(FILEPATH);
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} could not be accessed. Please ensure the file can be read by the current user and is not password protected. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be IOException", exception, e.getCause());
		}
	}
	
	@Test
	public void IOExceptionTest() {
		IOException exception = new IOException();
		try {
			throw new FileException(TEST_MESSAGE, exception, FILE);
		} catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} cannot be opened. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be IOException", exception, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForFileExceptionWithCause() {
		try {
		throw new FileException(CAUSE, FILE);
		}
		catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} java.lang.Throwable: {}", FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForFileExceptionWithMessage() {
		try {
		throw new FileException(TEST_MESSAGE, FILE);
		}
		catch (FileException e) {
			String expectedMessage = SentinelStringUtils.format("{} {}", FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
		}
	}
	
	@Test
	public void correctMessageForFileExceptionWithMessageAndCause() {
		try {
		throw new FileException(TEST_MESSAGE, CAUSE, FILE);
		}
		catch (FileException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(TEST_MESSAGE));
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void getMessageForFileException() {
		try {
		throw new FileException(FILE);
		}
		catch (FileException e) {
			assertEquals("Expecting custom exception message.", FILEPATH, e.getMessage());
		}
	}
	
	@Test
	public void filePathCorrectForFileException() {
		try {
		throw new FileException(FILE);
		}
		catch (FileException e) {
			assertEquals("Expecting to see the same file path.", FILE, e.getFile());	
		}
	}

}