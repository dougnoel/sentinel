package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.steps.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.webdrivers.Driver;


public class ElementTests {

    @BeforeClass
    public static void setUpBeforeClass() {
        Time.reset();
        Configuration.update("timeout", 1);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        Time.reset();
        Configuration.clear("timeout");
        Driver.quitAllDrivers();
    }


    @Test(expected = ElementNotInteractableException.class)
    public void clickOnDisabledTextbox() {
        BaseSteps.navigateToPage("TextboxPage");
        getElement("First Name Field").click();
    }

    @Test
    public void checkHasAttribute() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to have attribute.", getElement("Middle Name Field").hasAttribute("readonly"));
    }

    @Test
    public void checkNotHaveAttribute() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to not have attribute.", getElement("Middle Name Field").doesNotHaveAttribute("disabled"));
    }

    @Test
    public void checkHasAttributeOnNotHasAttribute() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to not have attribute.", getElement("Middle Name Field").hasAttribute("disabled"));
    }

    @Test
    public void checkNotHaveAttributeOnHasAttribute() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to have attribute.", getElement("Middle Name Field").doesNotHaveAttribute("readonly"));
    }

    @Test
    public void isDisabledOnDisabledElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be disabled.", getElement("First Name Field").isDisabled());
    }

    @Test
    public void isDisabledOnEnabledElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be enabled.", getElement("Last Name Field").isDisabled());
    }

    @Test
    public void isDisabledOnReadonlyElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be disabled.", getElement("Middle Name Field").isDisabled());
    }

    @Test
    public void isEnabledOnDisabledElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be disabled.", getElement("First Name Field").isEnabled());
    }

    @Test
    public void isEnabledOnReadonlyElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be disabled.", getElement("Middle Name Field").isEnabled());
    }

    @Test
    public void isEnabledOnEnabledElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be enabled.", getElement("Last Name Field").isEnabled());
    }

    @Test
    public void isHiddenOnHiddenElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be hidden.", getElement("Hidden Field").isHidden());
    }

    @Test
    public void isHiddenOnNotDisplayedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be hidden.", getElement("Not Displayed Field").isHidden());
    }

    @Test
    public void isHiddenOnVisibleElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be visible.", getElement("Last Name Field").isHidden());
    }

    @Test
    public void isDisplayedOnVisibleElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be visible.", getElement("Last Name Field").isDisplayed());
    }

    @Test
    public void isDisplayedOnHiddenElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be hidden.", getElement("Hidden Field").isDisplayed());
    }

    @Test
    public void isDisplayedOnNotDisplayedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be hidden.", getElement("Not Displayed Field").isDisplayed());
    }

    @Test
    public void isSelectedOnUnCheckedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to not be selected.", getElement("Car Checkbox").isSelected());
    }

    @Test
    public void isSelectedOnCheckedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to be selected.", getElement("Boat Checkbox").isSelected());
    }

    @Test
    public void isNotSelectedOnUnCheckedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to not be selected.", getElement("Car Checkbox").isNotSelected());
    }

    @Test
    public void isNotSelectedOnCheckedElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to be selected.", getElement("Boat Checkbox").isNotSelected());
    }

    @Test
    public void doesNotExistOnVisibleElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertFalse("Expecting element to exist.", getElement("Last Name Field").doesNotExist(true));
    }

    @Test
    public void doesNotExistOnBadElement() {
        BaseSteps.navigateToPage("TextboxPage");
        assertTrue("Expecting element to not exist.", getElement("Bad Element").doesNotExist(true));
    }

    @Test(expected = ElementNotInteractableException.class)
    public void sendingTextToDisabledTextbox() {
        BaseSteps.navigateToPage("TextboxPage");
        getElement("First Name Field").sendKeys("stuff");
    }

    @Test
    public void sendingTextToHiddenTextbox() {
        BaseSteps.navigateToPage("TextboxPage");
        getElement("Hidden Field").sendKeys("stuff");
        assertTrue("Expecting hidden element to receive text.", getElement("Hidden Field").getText().contains("stuff"));
    }

    @Test(expected = NoSuchElementException.class)
    public void ElementDoesNotExist() {
        BaseSteps.navigateToPage("TextboxPage");
        getElement("Blah");
    }

    @Test(expected = InvalidSelectorException.class)
    public void InvalidSelectorTest() {
        BaseSteps.navigateToPage("TextboxPage");
        Map<String, String> selectors = new HashMap<>();
        selectors.put("value", "1");
        new Element("Checkbox", "Checkbox", selectors).element();
    }

    @Test
    public void sendSpecialKey() throws java.io.IOException {
        BaseSteps.navigateToPage("KeyPressesPage");
        var input = (Textbox) getElement("key press input");
        input.sendKeys("A");
        input.sendSpecialKey(Keys.ESCAPE);
        input.sendSpecialKey(Keys.ENTER);
        input.sendSpecialKey(Keys.RETURN);
        input.sendSpecialKey(Keys.TAB);
        input.sendSpecialKey(Keys.PAGE_UP);
        input.sendSpecialKey(Keys.PAGE_DOWN);
        input.sendSpecialKey(Keys.BACK_SPACE);
        TextVerificationSteps.verifyElementTextContains("result text", "", "contains", "BACK_SPACE");
        assertTrue("Expecting key press input to be empty.", input.getText().isEmpty());
    }

    @Test
    public void checkParentColorsOfTransparentElement() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getWebDriver();

        Color colorBlue = Color.blue;
        String colorBlueHexValue = "#" + Integer.toHexString(colorBlue.getRGB()).substring(2);

        BaseSteps.navigateToPage("TextboxPage");
        js.executeScript("arguments[0].style.backgroundColor=arguments[1];", getElement("Body").element(), colorBlueHexValue);
        Assert.assertEquals(colorBlue, getElement("Car Checkbox").getBackgroundColor());
    }

    @Test
    public void checkElementBackgroundColor() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getWebDriver();

        Color colorRed = Color.red;
        String colorRedHexValue = "#" + Integer.toHexString(colorRed.getRGB()).substring(2);

        BaseSteps.navigateToPage("TextboxPage");
        js.executeScript("arguments[0].style.backgroundColor=arguments[1];", getElement("Car Checkbox").element(), colorRedHexValue);
        Assert.assertEquals(colorRed, getElement("Car Checkbox").getBackgroundColor());
    }

    @Test
    public void checkElementBackgroundColorDefaultsWhiteIfAllTransparency() {
        BaseSteps.navigateToPage("TextboxPage");
        Assert.assertEquals(Color.white, getElement("Car Checkbox").getBackgroundColor());
    }

    @Test(expected = NoSuchElementException.class)
    public void tableColumnDoesNotExist() throws Exception {
        BaseSteps.navigateToPage("TablePage");
        TableVerificationSteps.verifyCellInSpecifiedRow("1", "Not a real column", "example table", "","contains", "Bob");
    }

	@Test(expected = NoSuchElementException.class)
	public void getNonexistantTableHeaderElement() {
		BaseSteps.navigateToPage("TablePage");
		ElementFunctions.getElementAsTable("example table").clickColumnHeader("Not a real column");
	}
	
	@Test
	public void clickBasicTableHeaderElement() {
		BaseSteps.navigateToPage("InternetTablesPage");
		var table = ElementFunctions.getElementAsTable("table 1");
		table.clickColumnHeader("Last Name");
		assertTrue("Last Name column was expected to be sorted in descending order.", table.verifyColumnCellsAreSortedAscending("Last Name"));
	}

	@Test
	public void clickOnPoint() {
		BaseSteps.navigateToPage("TextboxPage");
		Element e = getElement("car checkbox");
		e.clickPositionOnElement(new Point(0, 200));
		assertTrue(e.isNotSelected());
		Element body = getElement("body");
		body.clickPositionOnChildElement(new Point(0, 0), By.xpath(".//label[@for='vehicle2']"));
		assertTrue(e.isSelected());
	}

    @Test
    public void assertTableColumnEmpty() {
        BaseSteps.navigateToPage("InternetTablesPage");
        var table = ElementFunctions.getElementAsTable("table 1");
        assertFalse("All cells in Last Name column not empty.", table.verifyAllColumnCellsEmpty("Last Name"));
    }

    @Test
    public void assertTableColumnNotEmpty() {
        BaseSteps.navigateToPage("InternetTablesPage");
        var table = ElementFunctions.getElementAsTable("table 1");
        assertTrue("All cells in Last Name column not empty.", table.verifyAllColumnCellsNotEmpty("Last Name"));
    }

    @Test
    public void validSingleUpload(){
        BaseSteps.navigateToPage("UploadPage");
        UploadSteps.sendPathsToInputElement("eclipse_run_icon_image", "choose file button");
        Element fileUploadCount = getElement("file counter");
        Element fileResult1 = getElement("upload result 1");
        Assert.assertTrue(fileUploadCount.getAttribute("data-filecount").equals("1") && fileResult1.getAttribute("data-filename").equals("eclipse_tool_bar_icon_run.png"));
    }

    @Test
    public void validMultiUpload(){
        BaseSteps.navigateToPage("UploadPage");
        UploadSteps.sendPathsToInputElement("eclipse_run_icon_image, eclipse_run_icon_image_2", "choose file button");
        Element fileUploadCount = getElement("file counter");
        Element fileResult1 = getElement("upload result 1");
        Element fileResult2 = getElement("upload result 2");
        Assert.assertTrue(fileUploadCount.getAttribute("data-filecount").equals("2") && fileResult1.getAttribute("data-filename").equals("eclipse_tool_bar_icon_run.png") && fileResult2.getAttribute("data-filename").equals("eclipse_tool_bar_icon_run_2.png"));
    }

    @Test
    public void invalidFilePathSending(){
        boolean assertionThrown = false;
        BaseSteps.navigateToPage("UploadPage");

        try{
            UploadSteps.sendPathsToInputElement("file_doesnot_exist", "choose file button");
        }
        catch(IOException fileException){
            assertionThrown = true;
            Assert.assertTrue("Error message should indicate a file was not found", fileException.getMessage().toLowerCase().contains("file could not be found"));
        }
        Assert.assertTrue("A fileException was expected to be thrown, but no exceptions occurred", assertionThrown);
    }

    @Test
    public void validDirectoryPathSendFailure(){
        boolean assertionThrown = false;
        BaseSteps.navigateToPage("UploadPage");

        try{
            UploadSteps.sendPathsToInputElement("eclipse_run_icon_image_folder", "choose file button");
        }
        catch(IOException fileException){
            assertionThrown = true;
            Assert.assertTrue("Error message should indicate the given file was a directory", fileException.getMessage().toLowerCase().contains("was a directory"));
        }

        Assert.assertTrue("A fileException was expected to be thrown, but no exceptions occurred", assertionThrown);
    }

    @Test
    public void waitForTableCellToHaveFailure(){
        BaseSteps.navigateToPage("InternetTablesPage");
        var table = ElementFunctions.getElementAsTable("table 1");
        Assert.assertFalse("Timed out waiting for cell in the 1st row and Last Name column to have the text Jones",
                table.waitForSpecificCellToContain(1, "Last Name", 1, "Jones", false, false));
    }

    @Test
    public void waitForTableCellToHaveExpectFalseFailure(){
        BaseSteps.navigateToPage("InternetTablesPage");
        var table = ElementFunctions.getElementAsTable("table 1");
        Assert.assertFalse("Timed out waiting for cell in the 1st row and Last Name column to not have the text Smith",
                table.waitForSpecificCellToContain(1, "Last Name", 1, "Smith", true, true));
    }

    @Test
    public void waitForAllTableCellsToHaveFailure(){
        BaseSteps.navigateToPage("TablePage");
        var table = ElementFunctions.getElementAsTable("example table");
        Assert.assertFalse("Timed out waiting for all cells in the Last Name column to have the text Jones",
                table.waitForAllRowsInColumnToContain(1, "Last Name", "Jones", false, false));
    }

    @Test
    public void waitForAllTableCellsToHaveExpectFalseFailure(){
        BaseSteps.navigateToPage("TablePage");
        var table = ElementFunctions.getElementAsTable("example table");
        Assert.assertFalse("Timed out waiting for all cells in the Last Name column to not contain the text Smith",
                table.waitForAllRowsInColumnToContain(1, "Last Name", "Smith", true, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void compareTableColumnValuesWithInvalidComparisonType() {
        BaseSteps.navigateToPage("TablePage");
        ElementFunctions.getElementAsTable("example table").verifyNumericValuesInWholeColumn("ID", "fake comparison", 3.0);
    }
}