package tests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true
	, features = "src/test/java/features"
	, glue = { "stepdefinitions", "com.dougnoel.sentinel.steps" }
	, plugin = {"json:target/cucumber.json",
			"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
	, strict = true
//  , tags = { "@19" }
)

public class SentinelTests {
    private static final Logger log = LogManager.getLogger(SentinelTests.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
        System.setProperty("org.freemarker.loggerLibrary", "none");
        System.setProperty("env", "dev"); // Set the environment (dev/qa/stage/prod/etc)
        WebDriverFactory.instantiateWebDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SentinelException {
        log.info("Driver: {}", WebDriverFactory.getWebDriver());
        if (System.getProperty("leaveBrowserOpen", "false") == "false") {
        	PageManager.quit();
        }
    }
}