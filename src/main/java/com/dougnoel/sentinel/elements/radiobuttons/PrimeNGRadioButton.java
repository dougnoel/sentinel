package com.dougnoel.sentinel.elements.radiobuttons;

import org.openqa.selenium.By;

import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

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

	public PrimeNGRadioButton(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
	
	/**
	 * Overrides the click method to send it to the correct part of the radio button.
	 */
	@Override
	public PageElement click() throws ElementNotFoundException {
		element().findElement(By.xpath("//input")).click();
		return this;
	}
	
	/**
	 * Overrides the isSelected method to send it to the correct part of the radio button.
	 */
	@Override
	public boolean isSelected() throws ElementNotFoundException {
		return element().findElement(By.xpath("//input")).isSelected();
	}
}
