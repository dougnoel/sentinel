package hooks;

import com.dougnoel.sentinel.enums.YAMLObjectType;
import com.dougnoel.sentinel.system.TestManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class SentinelHooks {

    @After
    public static void tearDown(Scenario scenario) {
        //Take screenshot and attach to report if scenario has failed
        if(scenario.isFailed() && TestManager.getActiveTestObject().getType() != YAMLObjectType.API) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
    }
}
