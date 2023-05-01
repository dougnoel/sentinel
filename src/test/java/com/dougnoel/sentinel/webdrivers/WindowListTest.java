package com.dougnoel.sentinel.webdrivers;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.steps.BaseSteps;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;

import java.util.NoSuchElementException;

import static com.dougnoel.sentinel.webdrivers.Driver.doesWindowExist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WindowListTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        Time.reset();
        Configuration.update("timeout", 3);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        Time.reset();
        Configuration.clear("timeout");
        Driver.quitAllDrivers();
    }

    @Test
    public void checkWindowExists() {
        BaseSteps.navigateToPage("InternetPage");
        assertTrue("Expecting window exists.", doesWindowExist("The Internet"));
        assertFalse("Expecting window does not exist.", doesWindowExist("Lorem"));
    }

    @Test(expected = NoSuchElementException.class)
    public void tryToSwitchToNamedWindowThatDoesNotExist(){
        BaseSteps.navigateToPage("InternetPage");
        Driver.goToTitledWindow("Fake Page Title");
    }

    @Test(expected = NoSuchElementException.class)
    public void tryToSwitchToNamedWindowContainingTitleThatDoesNotExist(){
        BaseSteps.navigateToPage("InternetPage");
        Driver.goToTitledWindowThatContains("Fake Page Title");
    }
}
