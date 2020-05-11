package com.dougnoel.sentinel.strings;

import static org.junit.Assert.*;

import org.junit.Test;

public class SentinelStringUtilsTest {

	@Test
	public void formatTest() {
		String stringArg = "1";
		int intArg = 2;
		double doubleArg = 3.0;
		String formattedString = SentinelStringUtils.format("My numbers are {}, {}, {}.", stringArg, intArg, doubleArg);
		assertEquals("All arguments should be substituted correctly.", "My numbers are 1, 2, 3.0.", formattedString);
	}
	
}
