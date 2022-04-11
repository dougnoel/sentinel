package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.exceptions.MalformedURLException;

/**
 * Wrapper for the Java URL object that throws a runtime exception when creating 
 * a URL object so that failure does not halt all tests.
 * @author dougnoel@gmail.com
 *
 */
public class JavaURL {
	
	private JavaURL() {}
	
	/**
	 * 
	 * @param url String the url as a string to convert.
	 * @return java.net.URL a URL object that can be used by methods that require that argument.
	 * @throws com.dougnoel.sentinel.exceptions.MalformedURLException if the string passed is not a valid URL
	 */
	public static java.net.URL create(String url) {
		try {
			return new java.net.URL(url);
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
	}
}
