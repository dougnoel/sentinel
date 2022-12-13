package com.dougnoel.sentinel.webdrivers;

import com.dougnoel.sentinel.steps.BaseSteps;
import org.junit.AfterClass;
import org.junit.Test;

import static com.dougnoel.sentinel.webdrivers.Driver.doesWindowExist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WindowListTest {

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Driver.quitAllDrivers();
    }

    @Test
    public void checkWindowExists() {
        BaseSteps.navigateToPage("InternetPage");
        assertTrue("Expecting window exists.", doesWindowExist("The Internet"));
        assertFalse("Expecting window does not exist.", doesWindowExist("Lorem"));
    }

}
