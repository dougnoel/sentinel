package com.dougnoel.sentinel.elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Retrieves as an element as an Element or as a child type.
 * 
 * To return as the desired type, we call getElement which returns an Element, and the element is 
 * cast as the desired element.
 *
 */
public class ElementFunctions {
	private static final Logger log = LogManager.getLogger(ElementFunctions.class.getName()); // Create a logger.

    private ElementFunctions() {
    	// Exists to defeat instantiation.
    }
    /**
     * Returns a Element object for a given elementName string from current page. Gets current page reference, 
     * replaces page name space characters with '_'
     * 
     * @param elementName String name of requested element
     * @return Element the requested element
     */
    public static Element getElement(String elementName) {
        return PageManager.getPage().getElement(elementName);
    }

    /**
     * Returns the Dropdown associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Dropdown the drop down associated with the element name on the currently active page
     */
    public static Dropdown getElementAsDropdown(String elementName) { 
		try {
			return (Dropdown) getElement(elementName);
		} catch (ClassCastException e) {
			throw new ClassCastException(buildClassCastExceptionMessage(elementName, Dropdown.class.getSimpleName()));
		}
    }
    
    /**
     * Returns the PageSelectElement associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return PageSelectElement the select element associated with the element name on the currently active page
     */
    public static SelectElement getElementAsSelectElement(String elementName) {
		try {
			return (SelectElement) getElement(elementName);
		} catch (ClassCastException e) {
			throw new ClassCastException(buildClassCastExceptionMessage(elementName, SelectElement.class.getSimpleName()));
		}
    }

    /**
     * Returns the Table associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Table the table associated with the element name on the currently active page
     */
    public static Table getElementAsTable(String elementName) {
		try {
			return (Table) getElement(elementName);
		} catch (ClassCastException e) {
			throw new ClassCastException(buildClassCastExceptionMessage(elementName, Table.class.getSimpleName()));
		}
    }

	/**
	 * Returns an element from the active page in any type you cast it as.
	 * @param elementName String the name of the element to return
	 * @param <T> Class the type of element to return
	 * @return Class an instance of the class you typed the element as
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getElementAsCustom(String elementName) {
		return (T) getElement(elementName);
	}
    
    /**
     * Returns an error message that is a clearer casting error and logs it.
     * @param elementName the element that was improperly used
     * @param className the page object name
     * @return String the error message to pass to the user
     */
    private static String buildClassCastExceptionMessage(String elementName, String className) {
    	String errorMessage = SentinelStringUtils.format("\"{}\" was not created as a {}. Update the {}.yml page object and try again."
    			, elementName
    			, className
    			, PageManager.getPage().getName());
    	log.error(errorMessage);
    	return errorMessage;
    }
}
