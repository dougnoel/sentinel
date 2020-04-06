package com.dougnoel.sentinel.elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.exceptions.NoSuchElementException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.StringUtils;

/**
 * Retrieves as an element as a  PageElement or as one of the following types:
 * 		- Button, Checkbox, Div, Dropdown, GoogleMap, Link, Radio, Select, Span, Table, Textbox
 * 
 * To return as the desired type, we call getElement which returns a PageElement, and the element is then typecast as the desired element.
 *
 */
public abstract class ElementFunctions {
	/**
	 * Instance of LogManager for a given ElementFunctions class
	 */
    private static final Logger log = LogManager.getLogger(ElementFunctions.class); 

    /**
     * Returns a PageElement object for a given elementName string from current page. Gets current page reference, replaces page name space characters qith '_'
     * 
     * @param elementName String name of requested element
     * @return PageElement the requested element
     * @throws PageNotFoundException if no page object found or defined.
     * @throws NoSuchElementException if element is not found or defined.
     */
    public static PageElement getElement(String elementName) throws NoSuchElementException, PageNotFoundException {
        Page page = PageManager.getPage();
        elementName = elementName.replaceAll("\\s+", "_").toLowerCase();
        Method pageElementName = null;
        try { // Create a Method object to store the PageElementwe want to exercise;
            pageElementName = page.getClass().getMethod(elementName);
        } catch (NoSuchMethodException e) {
            String errorMessage = StringUtils.format("Element {} is not defined for the page object {}. Make sure you have spelled the page object name correctly in your Cucumber step definition and in the page object.", elementName, page
                    .getClass().getSimpleName());
            log.error(errorMessage);
            throw new NoSuchElementException(errorMessage, e);
        }
        PageElement element = null;
        try {  // Invoke the creation of the PageElement and return it into a variable if no exception is thrown.
            element = (PageElement) pageElementName.invoke(page);
            log.trace("PageElement Name: " + pageElementName.getName());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        	String errorMessage = StringUtils.format("PageElement {} could not be found on Page {}. Please ensure the element is defined on the page.", pageElementName.getName(), page.getName());
        	log.error(errorMessage);
        	throw new NoSuchElementException(errorMessage);
        } 

        return element;
    }

    /**
     * Returns the Button associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Button the button associated with the element name on the currently active page
     * @throws PageNotFoundException if the page cannot be found
     * @throws NoSuchElementException if the element cannot be found 
     */
    public static Button getElementAsButton(String elementName) throws SentinelException {
        return (Button) getElement(elementName);
    }

    /**
     * Returns the Checkbox associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Checkbox the checkbox associated with the element name on the currently active page
     * @throws PageNotFoundException if the page cannot be found
     * @throws NoSuchElementException if the element cannot be found
     */
    public static Checkbox getElementAsCheckbox(String elementName) throws SentinelException {
        return (Checkbox) getElement(elementName);
    }

    /**
     * Returns the Div associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Div the div associated with the element name on the currently active page
     * @throws PageNotFoundException if the page cannot be found
     * @throws NoSuchElementException if the element cannot be found
     */
    public static Div getElementAsDiv(String elementName) throws SentinelException {
        return (Div) getElement(elementName);
    }

    /**
     * Returns the Dropdown associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Dropdown the dropdown associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Dropdown getElementAsDropdown(String elementName) throws SentinelException {  	
        return (Dropdown) getElement(elementName);
    }

    /**
     * Returns the GoogleMap associated with the element name on the currently
     * active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return GoogleMap the GoogleMap associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static GoogleMap getElementAsGoogleMap(String elementName) throws SentinelException {
        return (GoogleMap) getElement(elementName);
    }

    /**
     * Returns the Link associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Link the link associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Link getElementAsLink(String elementName) throws SentinelException {
        return (Link) getElement(elementName);
    }

    /**
     * Returns the PageSelectElement associated with the element name on the
     * currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return PageSelectElement the select element associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static PageSelectElement getElementAsSelectElement(String elementName) throws SentinelException {
        return (PageSelectElement) getElement(elementName);
    }

    /**
     * Returns the Radiobutton associated with the element name on the currently
     * active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Radio the radio button associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Radiobutton getElementAsRadiobutton(String elementName) throws SentinelException {
        return (Radiobutton) getElement(elementName);
    }

    /**
     * Returns the Span associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Span the span associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Span getElementAsSpan(String elementName) throws SentinelException {
        return (Span) getElement(elementName);
    }

    /**
     * Returns the Table associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Table the table associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Table getElementAsTable(String elementName) throws SentinelException {
        return (Table) getElement(elementName);
    }

    /**
     * Returns the Textbox associated with the element name on the currently active
     * page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Textbox the text box associated with the element name on the currently active page
     * @throws NoSuchElementException if the element cannot be found
     * @throws PageNotFoundException if the page cannot be found
     */
    public static Textbox getElementAsTextbox(String elementName) throws SentinelException {
        return (Textbox) getElement(elementName);
    }
}
