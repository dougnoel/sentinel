package com.dougnoel.sentinel.elements.radiobuttons;

import java.util.Map;

import org.openqa.selenium.By;

import com.dougnoel.sentinel.elements.Element;

/**
 * Implementation of an NGPrime Radio Button.
 * @see <a href="https://primefaces.org/primeng/showcase/#/radiobutton">PrimeNG Radio Button</a>
 */
public class PrimeNGRadioButton extends Element {

	/**
	 * Default Prime NG Radio Button constructor. An override of the Element constructor.
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
