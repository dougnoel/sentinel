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
@CucumberOptions(monochrome = true, features = "src/test/java/features", glue = { "stepdefinitions",
        "com.dougnoel.sentinel.steps" }
//, plugin = { "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "json:target/cucumber-report.json"}
//                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:reports/sentinel-report.html" }
,plugin = {"pretty", "de.monochromata.cucumber.report.PrettyReports:target/pretty-cucumber", "html:target/cucumber-html", "json:target/report.json"}
         , tags = { "@#22" }
)

public class TestRun {
    private static final Logger log = LogManager.getLogger(TestRun.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
//         System.setProperty("cucumber.options", "@example");
        System.setProperty("org.freemarker.loggerLibrary", "none");
        System.setProperty("env", "dev"); // Set the environment (dev/qa/stage/prod/etc)
        WebDriverFactory.instantiateWebDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SentinelException {
        log.info("Driver: {}", WebDriverFactory.getWebDriver());
        PageManager.quit();
//        System.setProperty("extent.reporter.avent.start", "true");
//        System.setProperty("extent.reporter.avent.config", "conf/extent-config.xml");
//        System.setProperty("extent.reporter.avent.out", "Sentinel Test Report");

//        Reporter.loadXMLConfig(new File("conf/extent-config.xml"));
//        Reporter.setSystemInfo("user", System.getProperty("user.name"));
//        Reporter.setSystemInfo("os", System.getProperty("os"));
//        Reporter.setTestRunnerOutput("Sample test runner output message");
    }
}
