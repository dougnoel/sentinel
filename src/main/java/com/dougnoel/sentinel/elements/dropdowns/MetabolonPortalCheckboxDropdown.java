package com.dougnoel.sentinel.elements.dropdowns;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MetabolonPortalCheckboxDropdown extends JSDropdownElement {

  private static final Logger log = LogManager.getLogger(PrimeNGDropdown.class.getName());
	
  public MetabolonPortalCheckboxDropdown(String elementName, Map<String,String> selectors) {
	  super(elementName, selectors);
	}

  protected static final By openDropdown = By.xpath(".//button/i");
  protected static final By dropdownValue = By.xpath(".//button/label");

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 */
  @Override
  protected WebElement getOption(String selectionText) {    	
    this.element().findElement(openDropdown).click();
    String dropdownOptions = ".//div[@class='Dropdown-menu']//div[contains(@class,'reportName')]/span[contains(text(),'" + selectionText + "')]"; 
   	log.trace("Looking for the value in the dropdown at position {} using {}", selectionText, dropdownOptions);
    	
  	return this.element().findElement(By.xpath(dropdownOptions));
    }

  /**
   * Returns a WebElement for an option with the given index.
   * @param index int the index of the option, starting with 1
	 * @return WebElement the element representing the option
   */
  @Override
  protected WebElement getOption(int index) {
    this.element().findElement(openDropdown).click();
    String dropdownOptions = ".//div[@class='Dropdown-menu']//div[contains(@class,'reportName')]["+(index)+"]/span";
  	log.trace("Trying to click option {} from downdown using the xpath {}{}", index, dropdownOptions);
    	
  	return this.element().findElements(By.xpath(dropdownOptions)).get(index-1); //we must convert the index to start at zero
  }

  /**
   * Gets the text of the first item currently selected.
   * @return String the text value of the selected option
   */
  @Override
  public String getSelectedText() {
   	return this.element().findElement(dropdownValue).getText();
  }

  protected WebElement getDropdownOptionCheckbox(String selectionText){
    this.element().findElement(openDropdown).click();
    String dropdownOptions = ".//div[@class='Dropdown-menu']//div[contains(@class,'reportName')]/span[contains(text(),'" + selectionText + "')]"; 
    String checkbox = "/ancestor::li/div[contains(@class, 'check-cell')]//input";
   	log.trace("Looking for the value in the dropdown at position {} using {}", selectionText, dropdownOptions+checkbox);
  	return this.element().findElement(By.xpath(dropdownOptions+checkbox));
  }

  protected void setDropdownOptionChecked(String selectionText, Boolean checked){
    WebElement checkBox = getDropdownOptionCheckbox(selectionText);
    if (checked.toString() != checkBox.getAttribute("value"))
      checkBox.click();
  }
}