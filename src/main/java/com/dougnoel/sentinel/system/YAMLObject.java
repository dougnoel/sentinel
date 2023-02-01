package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.enums.YAMLObjectType;

/**
 * An object created by a YAML File for testing.
 * @author dougnoel
 *
 */
public abstract class YAMLObject {
	protected String yamlObjectName = null;
	protected YAMLObjectType yamlObjectType = YAMLObjectType.UNKNOWN;
	
	/**
	 * Constructor
	 * @param name String the exact name on disk without the .yml extension of the file to be loaded.
	 */
	public YAMLObject(String name) {
		this.yamlObjectName = name;
	}
	
	/**
	 * Return the name of the object.
	 * @return String the name of the file where the object is stored without the .yml extension
	 */
	public String getName() {
		return yamlObjectName;
	}
	
	/**
	 * Return the type of object this is.
	 * @return YAMLObjectType the type of object, either API or Page
	 */
	public YAMLObjectType getType() {
		return yamlObjectType;
	}
 }
