package com.dougnoel.sentinel.elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import org.openqa.selenium.NoSuchElementException;

public class ElementFactory {
    
    protected static Map<String,Class<?>> elementClasses = new HashMap<>();
    
	private static final Logger log = LogManager.getLogger(ElementFactory.class);

    private ElementFactory(){
        // Exists to defeat instantiation.
    }

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
		Map<String, String> elementData = findElementData(elementName, page.getName());
		
		if (elementData == null) {
			var errorMessage = SentinelStringUtils.format("Data for the element {} could not be found in the {}.yml file.", elementName, page.getName());
			throw new NoSuchElementException(errorMessage);
		}
		try{
            if (page.getPageObjectType().equals(PageObjectType.EXECUTABLE)) {
                return new WindowsElement(elementName, elementData);
            }
            
			String elementType = null;
			if (!elementData.containsKey("elementType"))
				return new Element(elementName, elementData);
			
			elementType = elementData.get("elementType");
			
			Class<?> mappedAndRetrievedClass;

            // If the cached Class of the elementType has already been found, create an element of that Class
			if ((mappedAndRetrievedClass = elementClasses.get(elementType)) != null){
                var debugMessage = SentinelStringUtils.format("Successfully retrieved cached element Class in element factory for {} element of type {}", elementName, elementType);
                log.debug(debugMessage);
                return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);
            }
				
			
            // Attempt to look in com.dougnoel.sentinel.elements for the Class corresponding to the elementType
            // This will work in projects extending Sentinel for element types in the Sentinel jar.
			mappedAndRetrievedClass = retrieveClassBySimpleName(elementType);
			
            // If this ^^^ attempt failed, look on disk
			if (mappedAndRetrievedClass == null) {
                // First, look for the Class in "this" project's directories. This is the case where a custom element type is created in a project extending Sentinel.
                var debugMessage =  SentinelStringUtils.format("Failed to find element type {} in default sentinel element package. Looking in current project.", elementType);
                log.debug(debugMessage);
				String classPath = FileManager.getClassPath(elementType);
		    	if (classPath == null) {
                    // If the above search (in project directories) fails, default the class to "Element".
                    var debugMessage1 = SentinelStringUtils.format("Failed to find element type {} in current project. Defaulting to type Element.", elementType);
                    log.debug(debugMessage1);
		    		mappedAndRetrievedClass = Element.class;
		    	} else {
                    // If the above search is successful, fetch the Class corresponding to the elementType.
                    var debugMessage1 = SentinelStringUtils.format("Successfully found element type {} in current project.", elementType);
                    log.debug(debugMessage1);
		    		mappedAndRetrievedClass = Class.forName(classPath);
		    	}
			}
	    	
            // Add the resolved Class of the elementType to the elementClasses HashMap so the next time an element of that type is created we have the Class cached.
	    	elementClasses.put(elementType, mappedAndRetrievedClass);
	    	
            // Fetch the constructor for the resolved Class and call it to create a new instance of the element.
	    	return mappedAndRetrievedClass.getConstructor(String.class, Map.class).newInstance(elementName, elementData);

        }catch(NoSuchMethodException|InvocationTargetException|IllegalAccessException|InstantiationException|ClassNotFoundException e){
            throw new FileException("Caught", e, new File(page.getName() + ".yml"));
        }
	}
	
    /**
     * Returns Class &lt;?&gt; corresponding to the passed elementType if it exists in the com.dougnoel.sentinel.elements package.
     * If there is no class in the com.dougnoel.sentinel.elements package that matches the elementType, this method returns null.
     * @param elementType String the elementType from the page object that is spelled the same (case-insensitive) as the class of the element
     * @return Class &lt;?&gt; the Class corresponding to the passsed elementType. null if not found.
     */
    private static Class<?> retrieveClassBySimpleName(String elementType){
        try{
            var allClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getTopLevelClassesRecursive("com.dougnoel.sentinel.elements")
                .stream();
            Optional<ClassInfo> optionalFilteredClassInfo = allClasses
                                            .filter(c -> c.getSimpleName()
                                            .equalsIgnoreCase(elementType))
                                            .findFirst();
            ClassInfo filteredClassInfo = optionalFilteredClassInfo.isPresent() ? optionalFilteredClassInfo.get() : null;
            
            if(filteredClassInfo == null)
                return null;
            else
                return filteredClassInfo.load();
        }
        catch (java.util.NoSuchElementException|IOException e) {
        	return null;
        }
    }

    /**
     * Returns a Map &lt;String, String&gt; which contains all data for an element that is declared in the page object YAML file for the given page.
     * @param elementName String the name of the element
     * @param pageName String the name of the page
     * @return Map &lt;String, String&gt; the collection of keys and values which were declared in the page object YAML file for the given element on the given page.
     */
    private static Map<String, String> findElementData(String elementName, String pageName) {
		Map<String, String> elementData = Configuration.getElement(elementName, pageName);
		if (elementData == null) {
			for (String page : Configuration.getPageParts(pageName)) {
				elementData = findElementData(elementName, page);
				if (elementData != null) {
					break;
				}
			}
		}
		return elementData;
	}
}
