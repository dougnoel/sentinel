package com.dougnoel.sentinel.elements;

import org.openqa.selenium.By;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

public class PrimeNGRadioButton extends Radiobutton {

	public PrimeNGRadioButton(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
	
	public PageElement click() throws ElementNotFoundException {
		element().findElement(By.xpath("//input")).click();
		return this;
	}
	
	public boolean isSelected() throws ElementNotFoundException {
		return element().findElement(By.xpath("//input")).isSelected();
	}
}
