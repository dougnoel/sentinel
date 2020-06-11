package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.exceptions.NoSuchElementException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.pages.PageManager;

/**
 * Retrieves as an element as a  PageElement or as one of the following types:
 * 		- Button, Checkbox, Div, Dropdown, GoogleMap, Link, Radio, Select, Span, Table, Textbox
 * 
 * To return as the desired type, we call getElement which returns a PageElement, and the element is then typecast as the desired element.
 *
 */
public abstract class ElementFunctions {

    private ElementFunctions() {
    	// Exists to defeat instantiation.
    }
    /**
     * Returns a PageElement object for a given elementName string from current page. Gets current page reference, 
     * replaces page name space characters with '_'
     * 
     * @param elementName String name of requested element
     * @return PageElement the requested element
     * @throws PageNotFoundException if no page object found or defined.
     * @throws NoSuchElementException if element is not found or defined.
     */
    public static PageElement getElement(String elementName) throws NoSuchElementException, PageNotFoundException {
        Page page = PageManager.getPage();
        
        return page.getElement(elementName);
        
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
    public static EmbeddedMap getElementAsGoogleMap(String elementName) throws SentinelException {
        return (EmbeddedMap) getElement(elementName);
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
    public static SelectElement getElementAsSelectElement(String elementName) throws SentinelException {
        return (SelectElement) getElement(elementName);
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
