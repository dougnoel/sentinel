package com.dougnoel.sentinel.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

public class YAMLFileExceptionTests {
	private static final File FILE = new File("conf/sentinel.yml");
	private static final String FILEPATH = FILE.getAbsoluteFile().toString();
	private static final String TEST_MESSAGE = "My test message.";
	private static final Throwable CAUSE = new Throwable(TEST_MESSAGE);
		
	@Test
	public void correctMessageForYAMLFileException() {
		try {
		throw new YAMLFileException(FILE);
		}
		catch (YAMLFileException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH));	
		}
	}
	
	@Test
	public void JsonParseExceptionTest() throws JsonParseException, IOException {
		String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
		JsonFactory factory = new JsonFactory();
		JsonParser parser  = factory.createParser(carJson);
		JsonParseException exception = new JsonParseException(parser, FILEPATH);
		
		try {
			throw new YAMLFileException(TEST_MESSAGE, exception, FILE);
		} catch (YAMLFileException e) {
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
			throw new YAMLFileException(TEST_MESSAGE, exception, FILE);
		} catch (YAMLFileException e) {
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
			throw new YAMLFileException(TEST_MESSAGE, exception, FILE);
		} catch (YAMLFileException e) {
			String expectedMessage = SentinelStringUtils.format("{} cannot be found in the specified location. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be FileNotFoundException", exception, e.getCause());
		}
	}
	
	@Test
	public void IOExceptionTest() {
		IOException exception = new IOException();
		try {
			throw new YAMLFileException(TEST_MESSAGE, exception, FILE);
		} catch (YAMLFileException e) {
			String expectedMessage = SentinelStringUtils.format("{} cannot be opened. {}",
					FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be IOException", exception, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForYAMLFileExceptionWithCause() {
		try {
		throw new YAMLFileException(CAUSE, FILE);
		}
		catch (YAMLFileException e) {
			String expectedMessage = SentinelStringUtils.format("{} java.lang.Throwable: {}", FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void correctMessageForYAMLFileExceptionWithMessage() {
		try {
		throw new YAMLFileException(TEST_MESSAGE, FILE);
		}
		catch (YAMLFileException e) {
			String expectedMessage = SentinelStringUtils.format("{} {}", FILEPATH, TEST_MESSAGE);
			assertEquals("Expecting custom exception message.", expectedMessage, e.getMessage());
		}
	}
	
	@Test
	public void correctMessageForYAMLFileExceptionWithMessageAndCause() {
		try {
		throw new YAMLFileException(TEST_MESSAGE, CAUSE, FILE);
		}
		catch (YAMLFileException e) {
			assertTrue("Expecting exception message to contain the full file path.", e.getMessage().contains(FILEPATH) );	
			assertTrue("Expecting exception message to contain test message.", e.getMessage().contains(TEST_MESSAGE));
			assertEquals("Expecting exception cause to be what was passed", CAUSE, e.getCause());
		}
	}
	
	@Test
	public void getMessageForYAMLFileException() {
		try {
		throw new YAMLFileException(FILE);
		}
		catch (YAMLFileException e) {
			assertEquals("Expecting custom exception message.", FILEPATH, e.getMessage());
		}
	}
	
	@Test
	public void filePathCorrectForYAMLFileException() {
		try {
		throw new YAMLFileException(FILE);
		}
		catch (YAMLFileException e) {
			assertEquals("Expecting to see the same file path.", FILE, e.getFile());	
		}
	}

}