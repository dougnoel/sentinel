package com.dougnoel.sentinel.elements;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.InvalidSelectorException;

import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.MaterialUISelect;
import com.dougnoel.sentinel.elements.dropdowns.PrimeNGDropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

public class ElementFunctionsTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("env", "dev");
		PageManager.setPage("Elements");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Driver.quitAllDrivers();
	}

	@Test
	public void createDefaultElement() {
		Element element = ElementFunctions.getElement("generic");
		assertTrue("Expecting Element class.", element instanceof Element);
		assertEquals("Expecting Default Name.", "generic", element.getName());
	}
	
	@Test

	public void createDropDown() {
		Element element = ElementFunctions.getElementAsDropdown("dropdown");
		assertTrue("Expecting Dropdown class.", element instanceof Dropdown);
		assertEquals("Expecting Dropdown Name.", "dropdown", element.getName());
	}
	
	@Test(expected = ClassCastException.class)
	public void failToCreateDropDown() {
		ElementFunctions.getElementAsDropdown("generic");
	}
	
	@Test
	public void createMaterialUISelect() {
		Element element = ElementFunctions.getElement("material_ui_select");
		assertTrue("Expecting MaterialUISelect class.", element instanceof MaterialUISelect);
		assertEquals("Expecting MaterialUISelect Name.", "material_ui_select", element.getName());
	}

	@Test
	public void createPrimeNGDropdown() {
		Element element = ElementFunctions.getElement("prime_ng_dropdown");
		assertTrue("Expecting PrimeNGDropdown class.", element instanceof PrimeNGDropdown);
		assertEquals("Expecting PrimeNGDropdown Name.", "prime_ng_dropdown", element.getName());
	}
	
	@Test
	public void createSelect() {
		Element element = ElementFunctions.getElementAsSelectElement("select");
		assertTrue("Expecting SelectElement class.", element instanceof SelectElement);
		assertEquals("Expecting SelectElement Name.", "select", element.getName());
	}
	
	@Test(expected = ClassCastException.class)
	public void failToCreateSelect() {
		ElementFunctions.getElementAsSelectElement("generic");
	}
	
	@Test
	public void createNGXDataTable() {
		Element element = ElementFunctions.getElement("ngx_data_table");
		assertTrue("Expecting NGXDataTable class.", element instanceof NGXDataTable);
		assertEquals("Expecting NGXDataTable Name.", "ngx_data_table", element.getName());
	}
	
	@Test
	public void createTable() {
		Element element = ElementFunctions.getElementAsTable("table");
		assertTrue("Expecting Table class.", element instanceof Table);
		assertEquals("Expecting Table Name.", "table", element.getName());
	}

	@Test
	public void createCustom() {
		Textbox element = ElementFunctions.getElementAsCustom("textbox");
		assertTrue("Expecting Element class.", element instanceof Textbox);
		assertEquals("Expecting Custom Name.", "textbox", element.getName());
	}

	@Test(expected = ClassCastException.class)
	public void failToCreateCustom() {
		@SuppressWarnings("unused")
		Table element = ElementFunctions.getElementAsCustom("foobar");
	}

	@Test(expected = ClassCastException.class)
	public void failToCreateTable() {
		ElementFunctions.getElementAsTable("generic");
	}

	@Test(expected = InvalidSelectorException.class)
	public void badSelector() {
		ElementFunctions.getElement("bad_selector").click();
	}
	
	@Test(expected = InvalidSelectorException.class)
	public void creationFailure() {
		ElementFunctions.getElement("bad_element").click();
	}
	
}
