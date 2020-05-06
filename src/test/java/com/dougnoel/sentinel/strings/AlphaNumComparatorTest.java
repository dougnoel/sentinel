package com.dougnoel.sentinel.strings;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class AlphaNumComparatorTest {

	@Test
	public void sortValuesTest() {
		List<String> values = Arrays.asList("dazzle2", "dazzle10", "dazzle1", "dazzle2.7", "dazzle2.10", "2", "10", "1", "EctoMorph6", "EctoMorph62", "EctoMorph7");
    	String testOutput = values.stream().sorted(new AlphanumComparator()).collect(Collectors.joining(" "));
    	assertEquals("String should be sorted correctly.", "1 2 10 EctoMorph6 EctoMorph7 EctoMorph62 dazzle1 dazzle2 dazzle2.7 dazzle2.10 dazzle10", testOutput);
	}

}
