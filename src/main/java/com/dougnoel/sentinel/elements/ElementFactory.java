package com.dougnoel.sentinel.elements;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import org.openqa.selenium.NoSuchElementException;

public class ElementFactory {
    
    protected static Map<String,Class<?>> elementClasses = new HashMap<>();

    /**
     * Returns an Object that is an Element using the element name and Page. 
     * We look first in the Sentinel jar, then in the local repository class files.
     * If we don't find the element type in either location, we create a base Element type.
     * 
     * @param elementName String the name of the element to create
     * @param page Page the page object to lookup the element from
     * @return Object the element that is created
     */
	public static Object createElement(String elementName, Page page) {
		Map<String, String> elementData = findElement(elementName, page.getName());
		
		if (elementData == null) {
			var errorMessage = SentinelStringUtils.format("Data for the element {} could not be found in the {}.yml file.", elementName, page.getName());
			throw new NoSuchElementException(errorMessage);
		}
		try{
		
			String elementType = null;
			if (!elementData.containsKey("elementType"))
				return new Element(elementName, elementData);
			
			elementType = elementData.get("elementType");
			
			Class<?> mappedAndRetrievedClass;

            // If the cached Class of the elementType has already been found, create an element of that Class
			if ((mappedAndRetrievedClass = elementClasses.get(elementType)) != null)
				return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);
			
            // Attempt to look in com.dougnoel.sentinel.elements for the Class corresponding to the elementType
            // This will work in projects extending Sentinel for element types in the Sentinel jar.
			mappedAndRetrievedClass = retrieveClassBySimpleName(elementType);
			
            // If this ^^^ attempt failed, look on disk
			if (mappedAndRetrievedClass == null) {
                // First, look for the Class in "this" project's directories. This is the case where a custom element type is created in a project extending Sentinel.
				String classPath = Configuration.getClassPath(elementType);
		    	if (classPath == null) {
                    // If the above search (in project directories) fails, default the class to "Element".
		    		mappedAndRetrievedClass = Element.class;
		    	} else {
                    // If the above search is successful, fetch the Class corresponding to the elementType.
		    		mappedAndRetrievedClass = Class.forName(classPath);
		    	}
			}
	    	
            // Add the resolved Class of the elementType to the elementClasses HashMap so the next time an element of that type is created we have the Class cached.
	    	elementClasses.put(elementType, mappedAndRetrievedClass);
	    	
            // Fetch the constructor for the resolved Class and call it to create a new instance of the element.
	    	return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);

        }catch(NoSuchMethodException|InvocationTargetException|IllegalAccessException|InstantiationException|ClassNotFoundException e){
            throw new RuntimeException("Caught", e);
        }
	}
	
    /**
     * 
     * @param elementType
     * @return
     */
    private static Class<?> retrieveClassBySimpleName(String elementType){
        try{
            var allClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getTopLevelClassesRecursive("com.dougnoel.sentinel.elements")
                .stream();
            ClassInfo filteredClass = allClasses
                                            .filter(c -> c.getSimpleName()
                                            .equalsIgnoreCase(elementType))
                                            .findFirst()
                                            .get();
            return filteredClass.load();
        }
        catch (java.util.NoSuchElementException|IOException e) {
        	return null;
        }
    }

    /**
     * 
     * @param elementName
     * @param pageName
     * @return
     */
    private static Map<String, String> findElement(String elementName, String pageName) {
		Map<String, String> elementData = Configuration.getElement(elementName, pageName);
		if (elementData == null) {
			for (String page : Configuration.getPageParts(pageName)) {
				elementData = findElement(elementName, page);
				if (elementData != null) {
					break;
				}
			}
		}
		return elementData;
	}
}
