package com.dougnoel.sentinel.system;

public class TestManager {
	private static YAMLObject activeTestObject = null;
	
	public static void setActiveTestObject(YAMLObject yamlObject) {
		activeTestObject = yamlObject;
	}
	public static YAMLObject getActiveTestObject() {
		return activeTestObject;
	}
	
	private TestManager() {
		// Exists only to defeat instantiation.
	}
}
