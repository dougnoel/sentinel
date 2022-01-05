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
    
    protected static Map<String,Class> elementClasses = new HashMap<>();


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
			
			Class mappedAndRetrievedClass = elementClasses.get(elementType);
			if (mappedAndRetrievedClass != null)
				return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);
			
			mappedAndRetrievedClass = retrieveClassBySimpleName(elementType);
			
			if (mappedAndRetrievedClass == null) {
				String classPath = Configuration.getClassPath(elementType);
		    	if (classPath == null) {
		    		mappedAndRetrievedClass = Element.class;
		    		
		    	} else {
		    		mappedAndRetrievedClass = Class.forName(classPath);
		    	}
			}
	    	
	    	elementClasses.put(elementType, mappedAndRetrievedClass);
	    	
	    	return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);

        }catch(NoSuchMethodException|InvocationTargetException|IllegalAccessException|InstantiationException|ClassNotFoundException e){
            throw new RuntimeException("Caught", e);
        }
	}
	
    private static Class retrieveClassBySimpleName(String elementType){
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
