package com.dougnoel.sentinel.system;

import org.junit.Test;

import com.dougnoel.sentinel.exceptions.MalformedURLException;

public class JavaURLTests {

	@Test(expected = MalformedURLException.class)
	public void MalFormedURLTest() {
		JavaURL.create(null);
	}

}
