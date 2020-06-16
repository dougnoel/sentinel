package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.pages.PageManager;

/**
 * Retrieves as an element as a  PageElement or as one of the following types:
 * Checkbox, Dropdown, Radiobutton, Select, Table, or Textbox.
 * 
 * To return as the desired type, we call getElement which returns a PageElement, and the element is 
 * cast as the desired element.
 *
 */
public class ElementFunctions {

    private ElementFunctions() {
    	// Exists to defeat instantiation.
    }
    /**
     * Returns a PageElement object for a given elementName string from current page. Gets current page reference, 
     * replaces page name space characters with '_'
     * 
     * @param elementName String name of requested element
     * @return PageElement the requested element
     */
    public static PageElement getElement(String elementName) {
        return PageManager.getPage().getElement(elementName);
    }

    /**
     * Returns the Checkbox associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Checkbox the checkbox associated with the element name on the currently active page
     */
    public static Checkbox getElementAsCheckbox(String elementName) {
        return (Checkbox) getElement(elementName);
    }

    /**
     * Returns the Dropdown associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Dropdown the drop down associated with the element name on the currently active page
     */
    public static Dropdown getElementAsDropdown(String elementName) {  	
        return (Dropdown) getElement(elementName);
    }

    /**
     * Returns the Radio button associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Radio the radio button associated with the element name on the currently active page
     */
    public static Radiobutton getElementAsRadiobutton(String elementName) {
        return (Radiobutton) getElement(elementName);
    }
    
    /**
     * Returns the PageSelectElement associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return PageSelectElement the select element associated with the element name on the currently active page
     */
    public static SelectElement getElementAsSelectElement(String elementName) {
        return (SelectElement) getElement(elementName);
    }

    /**
     * Returns the Table associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Table the table associated with the element name on the currently active page
     */
    public static Table getElementAsTable(String elementName) {
        return (Table) getElement(elementName);
    }

    /**
     * Returns the Textbox associated with the element name on the currently active page.
     * 
     * @param elementName String the name of the element to be returned
     * @return Textbox the text box associated with the element name on the currently active page
     */
    public static Textbox getElementAsTextbox(String elementName) {
        return (Textbox) getElement(elementName);
    }
}
