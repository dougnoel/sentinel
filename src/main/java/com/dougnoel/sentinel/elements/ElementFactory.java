package com.dougnoel.sentinel.elements;

import java.io.IOException;
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
		Class mappedAndRetrievedClass;
		String elementType = null;
		if (elementData.containsKey("elementType")) {
			elementType = elementData.get("elementType");
		}else{
            elementType = "Element";
            mappedAndRetrievedClass = elementClasses.putIfAbsent(elementType, Element.class);
        }
		

//		try {
            mappedAndRetrievedClass = elementClasses.computeIfAbsent(elementType, type -> (retrieveClassBySimpleName(type)));
//		}catch(Exception e){
//            mappedAndRetrievedClass = elementClasses.putIfAbsent(elementType, Element.class);
//        }
        try{
        	if (mappedAndRetrievedClass == null) {
//        		mappedAndRetrievedClass = Configuration.getClassPath(elementType);
        	}
            return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);
        //}catch(NoSuchMethodException nsme){
            
        }catch(Exception e){
            throw new RuntimeException("Caught", e);
        }
	}
	
    private static Class retrieveClassBySimpleName(String elementType){
        try{
            var allClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getTopLevelClassesRecursive("elements")
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
