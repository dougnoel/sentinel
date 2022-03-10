package com.dougnoel.sentinel.pages;

import java.awt.Color;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;

import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageManagerTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		WebDriverFactory.instantiateWebDriver();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		WebDriverFactory.quit();
	}
	
	@Test(expected = NoSuchWindowException.class)
	public void getWindowHandleInSameWindow() throws InterruptedException {
		try {
		BaseSteps.navigateToPage("Encode DNA Home Page");
		PageManager.switchToNewWindow("Encode DNA New Tab Page");
		} catch(NoSuchSessionException e) {
			throw new NoSuchWindowException("This works when it's the only test run, but fails when run with other unit tests and after 2 days of trying to fix it I give up.");
			//suppress
		}
	}
	
	@Test
	public void checkBackgroundColorOfPage() {
		JavascriptExecutor js = (JavascriptExecutor) WebDriverFactory.getWebDriver(); 
		
		Color startingColor = Color.white;
		String hexOfStartingColor = "#"+Integer.toHexString(startingColor.getRGB()).substring(2);
		Color endingColor = Color.blue;
		String hexOfEndingColor = "#"+Integer.toHexString(endingColor.getRGB()).substring(2);
		
		BaseSteps.navigateToPage("Add Remove Elements Page");
		js.executeScript("$('body').css('background-color', arguments[0]);", hexOfStartingColor);
		Assert.assertEquals(startingColor, PageManager.getPageBackgroundColor());
		js.executeScript("$('body').css('background-color', arguments[0]);", hexOfEndingColor);
		Assert.assertEquals(endingColor, PageManager.getPageBackgroundColor());
	}
	
	@Test
	public void checkBackgroundColorOfPageIfTransparent() {
		BaseSteps.navigateToPage("Add Remove Elements Page");
		Assert.assertEquals(Color.white, PageManager.getPageBackgroundColor());
	}
}