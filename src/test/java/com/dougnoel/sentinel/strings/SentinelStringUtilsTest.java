package com.dougnoel.sentinel.strings;

import static org.junit.Assert.*;

import org.junit.Test;

public class SentinelStringUtilsTest {

	@Test
	public void formatTest() {
		String stringArg = "1";
		int intArg = 2;
		double doubleArg = 3.0;
		var formattedString = SentinelStringUtils.format("My numbers are {}, {}, {}.", stringArg, intArg, doubleArg);
		assertEquals("All arguments should be substituted correctly.", "My numbers are 1, 2, 3.0.", formattedString);
	}
	
	@Test
	public void stripSingleQuotes() {
		var text = "'parent-tunnel=\"optumtest\", tunnelIdentifier='Optum-Prd''";
		var result = SentinelStringUtils.stripSurroundingQuotes(text);
		assertEquals("Single quotes should be removed", "parent-tunnel=\"optumtest\", tunnelIdentifier='Optum-Prd'", result);
	}

	@Test
	public void stripDoubleQuotes() {
		var text = "\"parent-tunnel=\"optumtest\", tunnelIdentifier='Optum-Prd'\"";
		var result = SentinelStringUtils.stripSurroundingQuotes(text);
		assertEquals("Single quotes should be removed", "parent-tunnel=\"optumtest\", tunnelIdentifier='Optum-Prd'", result);
	}

	@Test
	public void stripNull() {
		String text = null;
		var result = SentinelStringUtils.stripSurroundingQuotes(text);
		assertEquals("Null should be returned without error.", null, result);
	}
}
