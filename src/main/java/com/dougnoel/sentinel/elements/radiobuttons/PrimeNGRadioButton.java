package com.dougnoel.sentinel.elements.radiobuttons;

import java.util.Map;

import org.openqa.selenium.By;

import com.dougnoel.sentinel.elements.Element;

/**
 * Implementation of an NGPrime Radio Button.
 * @see <a href="https://primefaces.org/primeng/showcase/#/radiobutton">PrimeNG Radio Button</a>
 * <p>
 * <b>Page Object Examples:</b>
 * <ul>
 * <li>public PrimeNGRadioButton yes_radio_buton() { return new PrimeNGRadioButton(XPATH, "//p-radiobutton[@inputid='yes']"); }</li>
 * <li>public PrimeNGRadioButton no_option() { return new PrimeNGRadioButton(XPATH, "//p-radiobutton[@id='no_option']"); }</li>
 * <li>public PrimeNGRadioButton option_three_radio_buton() { return new PrimeNGRadioButton(XPATH, "//*[@id='opt3']"); }</li>
 * </ul>
 */
public class PrimeNGRadioButton extends Radiobutton {

	/**
	 * Implementation of a Radiobutton to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public PrimeNGRadioButton(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}
	
	/**
	 * Overrides the click method to send it to the correct part of the radio button.
	 */
	@Override
	public Element click() {
		element().findElement(By.xpath("//input")).click();
		return this;
	}
	
	/**
	 * Overrides the isSelected method to send it to the correct part of the radio button.
	 */
	@Override
	public boolean isSelected() {
		return element().findElement(By.xpath("//input")).isSelected();
	}
}
