package tests;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.cucumber.listener.Reporter;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, features = "src/test/java/features", glue = { "stepdefinitions",
        "com.dougnoel.sentinel.steps" }, plugin = {
                "com.cucumber.listener.ExtentCucumberFormatter:reports/extent-cucumber-report.html" }
         , tags = { "@example" }
)

public class TestRun {
    private static final Logger log = LogManager.getLogger(TestRun.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
//         System.setProperty("cucumber.options", "@example");
        System.setProperty("env", "dev"); // Set the environment (dev/qa/stage/prod/etc)
        WebDriverFactory.instantiateWebDriver(); 
    }

    @AfterClass
    public static void tearDownAfterClass() throws SentinelException {
        log.debug("Driver: {}", WebDriverFactory.getWebDriver());
        PageManager.quit();
        Reporter.loadXMLConfig(new File("conf/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", System.getProperty("os"));
        Reporter.setTestRunnerOutput("Sample test runner output message");
      
    }
}
