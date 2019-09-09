# Sentinel - Selenium Automation Framework in Java

A Selenium framework that implements the [Page Object Model](http://cheezyworld.com/2010/11/09/ui-tests-not-brittle/) (POM) using [Object Oriented Programming](https://en.wikipedia.org/wiki/Object-oriented_programming) (OOP) concepts. It abstracts out most of the complexity of Selenium to allow users to focus on automating tests and not spend time dealing with the technical details of implementing an automation framework.

# Section 1: Using Sentinel

## 1.0 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### 1.1 Prerequisites

What things you need to install the software and how to install them:
 * Eclipse (Optional)
 * Java
 * Maven

### 1.2 Installation

Clone the project from git.

```
git clone https://github.com/dougnoel/sentinel.git
```

Install Traprange to your local Maven Repo

```
cd [your project dir]/sentinel
mvn install:install-file -Dfile=lib/traprange/traprange-1.1.1.jar -DgroupId=com.giaybac -DartifactId=traprange -Dversion=1.1.1 -Dpackaging=jar -DgeneratePom=true
```

Open the project in Eclipse.

```
File -> Open Projects from Filesystem...
```

## 2.0 Creating Tests

Explain how to run the automated tests for this system

### 2.1 Creating Page Objects

A page object contains the location information for all the elements on the page. With a config file, you can configure URLs for environments, as well as user names and passwords per environment.

#### 2.1.1 Creating Page Objects

Page objects must be located in the pages package. Create one by inheriting the Page class. 

```
package pages;

public class MyNewPage extends Page {
}
```

#### 2.1.2 Add an element

You add an element by creating a function that returns an element type. For example:

```
package pages;

public class MyNewPage extends Page {
	public Div google_map() { return new Div(ID, "map"); }
}
```

### 2.2 Add a configuration file (YAML)

Create a file in the directory with the same name (including case) as your page object with a .yml extension.

```
urls:
  base: http://bkcmember.lho{env}.lho-npe.net/Default.aspx
  prod: https://www.mybluekcma.com/Default.aspx
dev:
  username: devuser
  password: Test1234
qa:
  username: qauser
  password: Test1234
prod:
  username: produser
  password: Test1234
```

### 2.3 Create a feature file

Create a feature file using the [Gherkin Syntax](https://docs.cucumber.io/gherkin/step-organization/). 

```
#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Title of your feature
  I want to use this template for my feature file

  @tag1
  Scenario: Title of your scenario
    Given I want to write a step with precondition
    And some other precondition
    When I complete action
    And some other action
    And yet another action
    Then I validate the outcomes
    And check more outcomes

  @tag2
  Scenario Outline: Title of your scenario outline
    Given I want to write a step with <name>
    When I check for the <value> in step
    Then I verify the <status> in step

    Examples: 
      | name  | value | status  |
      | name1 |     5 | success |
      | name2 |     7 | Fail    |

```

### 2.4 Create a step definition file

If the generic steps in stepdefinitions/BaseSteps.java do not fit your needs, then you can create a new file in the stepdefinition package.

```
package stepdefinitions;

public class MySteps {
	private static final Logger log = LogManager.getLogger(BaseSteps.class.getName()); // Create a logger.

	@When("^I do stuff like (.*)$")
	public void i_do_stuff(String stuff) throws Throwable {
	    //Operate on the variable stuff
	}
}
```
## 3.0 Creating API Tests
Sentinel now supports API testing.

### 3.1 Creating API Objects
An API object contains all the information needed to connect to an API. In future versions, we will pull directly from a swagger file.

#### 3.1.1 Creating an API Object
API objects must be located in the pages package path. Create one by inheriting the API class. 

```
package pages;

public class MyNewAPI extends API {
}
```

#### 3.1.2 Add a URL
You add a URL by creating a constructor for your API class:

```
package apis;

import java.net.MalformedURLException;
import java.net.URL;

import sentinel.apis.API;

public class TestAPI extends API {
	
	public TestAPI() throws MalformedURLException {
		this.url = new URL("https://jsonplaceholder.typicode.com/");
	}
}
```

#### 3.1.3 Add an action
You add an action by creating a function that returns an action type with a designated endpoint. There are three kinds: GET, POST and PUT. For example if I had an endpoint of "todos" that used a GET action to retrieve a list of items I would use the following:

```
package apis;

import java.net.MalformedURLException;
import java.net.URL;

import sentinel.apis.API;
import sentinel.apis.GET;

public class TestAPI extends API {
	public GET to_do_list () { return new GET("todos"); }
	
	public TestAPI() throws MalformedURLException {
		this.url = new URL("https://jsonplaceholder.typicode.com/");
	}
}
```

## 4.0 Executing Tests
Tests can be executed either in side an editor using JUnit, or on the commandline using maven. The first option is good for
getting debug output while developing. The second is good for running in a CI/CD pipeline. Either way, you must setup a JUnit test to run.

### 4.1 Executing tests using JUnit

1. Create a package in src/test/java called "tests".
2. Create a java file in the packages with the name "Test" in it. (E.G. TestRun.java)

#### 4.1.1 Setting the Test Environment

Create a file in the conf directory called 'sentinel.yml'. Here you will need to set values in order to run your tests. All configuration properties such as which browser and operating system to use during testing, saucelabs configuration, which page object packages you want to test, and other necessary values are to be set on an environment specific basis. 

##### **You must set browser and operating system values in order to run a test**

 The below example is taken from the example configuration file in the conf directory called sentinel.example.yml.

```
---
configurations:
  dev:
    os: "OS X"
    browser: chrome
    ssltrust: none
    pageObjectPackages: "pages.,pages.UserAdminTool,pages.BayCare,pages.BKC,pages.BLA,pages.COX,pages.EHI,pages.MutualOfOmaha"
  stage:
    ssltrust: none
    pageObjectPackages: "pages.NewComponent,pages.UnitedHealth,pages.BLA,pages.Test"
  qa:
    browser: firefox
    saucelabs: "test1:1j14h-141h-11j3-1h31j3"
  sit:
    browser: firefox
    os: "Windows 10"
  prod:
    ssltrust: all
    os: Linux
...
```
Here are all the properities you can set in the sentinel.yml file:

| Property Name     |Possible Values                                                   |
| ------------------|------------------------------------------------------------------|
| env               |any environment name                                              |
| browser           |Chrome, Firefox, IE, Safari                                       |
| os                |"OS X", Windows, Mac, Linux, Win                                  |  
| ssltrust          |all, none                                                         |
| pageObjectPackages|a comma separated list of page oject packages defined in sentinel |
| saucelabs         |"username:passwordKey"                                            |  
| timeout           |any number, defaults to 10                                        |
| timeunit          |any unit of time, defaults to seconds                             |
| user.name         |The person running the test, NOT a test user                      |  
| download          |The download directory                                            |

#### 4.1.2 Set Tags in Test Package

In the test setup file, add the tags you want to test. These should match the tags in the feature file you have created.

```
tags = { "@ABCD-1234" }
```

#### 4.1.3 Closing the tests
This is where you clean up. In the tearDownAfterClass() add code similar to the following:

```
WebDriver driver = WebDriverFactory.getWebDriverAndHandleErrors();
log.debug("Driver: " + driver);
driver.quit();
```
### 4.2 Executing Tests using Maven
You can execute the tests in the dev environment on the command line.

```
mvn test
```
#### 4.2.1 Using command line parameters
You can also set configuration properties from the command line. Simply enter:

```
mvn -Dproperty=value test
```

Note that only values with spaces require double quotes:

```
mvn -Dproperty="my value" test
```
If you want to use tags you can include them like so:

```
mvn -D"cucumber.options--tags @TAG-103,@TAG-449" test
```

You will only have to do this the first time you run a test with a certain set of configurations. Any property you can set in 
the config file, you can set on the command line. If you find yourself testing multiple configurations, please refer to Section 3.1.1.

## 5.0 Deployment

Add additional notes about how to deploy this on a live system in Bamboo.

### 5.1 Building a Jar File

To build a file for release, open up the pom.xml and change the version number using [Semantic Versioning](http://semver.org/) to the next release number. Alternately an X.X.X-SNAPSHOT version can be created.

Then just run:

```
mvn package
```
The jar file will be located in the target directory.

To install it locally:

```
mvn install:install-file -Dfile=sentinel-1.0.0-SNAPSHOT.jar -DgroupId=com.dougnoel -DartifactId=sentinel -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
```

## 6.0 Additional Documentation & Resources

### 6.1 Javadocs
Sentinel comes with Javadocs which describe its classes in great detail. This includes examples of how you can use the
generic Cucumber steps that are already included.

The Javadocs can also be easily generated by running the following command.

```
mvn javadoc:javadoc
```
The files are generated in the project root folder under the default path of target/site/apidocs and can be accessed by opening up the index.html file contained within that folder.

Every method should have a Javadoc comment describing what it does, its parameters, what it returns (if not void), and any exceptions it throws. We follow the [Liferay-Portal Javadoc Guidelines](https://github.com/liferay/liferay-portal/blob/master/readme/ADVANCED_JAVADOC_GUIDELINES.markdown) for writing Javadoc contents.

### 6.2 Built With

* [Cucumber](https://cucumber.io/) - BDD Testing Framework
* [Cucumber Extent Reporter](http://www.vimalselvam.com/cucumber-extent-reporter/) Interface between Cucumber Results and Extent Reports.
* [Commons Lang](https://commons.apache.org/proper/commons-lang/) - Apache Commons Lang 3 for common Java language options
* [Extent Reports](http://extentreports.com/) - A report framework for automation testing.
* [HttpComponents](http://hc.apache.org/) - Apache HttpComponents for API testing.
* [Jackson](https://github.com/FasterXML/jackson) - Libraries for reading YAML, JSON and other config files.
* [Log4j2](https://logging.apache.org/log4j/2.x/) - Apache logging framework.
* [PDFBox](https://pdfbox.apache.org/) - Apache library for reading PDF files.
* [Maven](https://maven.apache.org/) - Apache dependency management.
* [Maven Javadoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) - Plugin to allow maven to generate Javadocs.
* [Saucelabs](https://saucelabs.com/) - Multi-Platform, multi-browser external testing service.
* [Selenium](https://www.seleniumhq.org/) - The automation framework workhorse.
* [Swagger Parser](https://github.com/swagger-api/swagger-parser) - Reads Swagger API files, allowing us to use them as API Objects.
* [Traprange](https://github.com/thoqbk/traprange) - Independent library developed using PDFBox to deal with tables in PDFs.
* [Unirest](http://unirest.io/java.html) - A simple API library used for the API testing functionality.

### 6.3 Web Drivers

The web drivers used by Sentinel are stored in drivers/os to make sure there is only one place to fix driver compatibility issues.
Chrome auto updates, and so is the one that will go of date most often. While we could pull the driver from a path and let each implementation install the drivers, this can become problematic in CI/CD environments where we do not control the system. This also
reduces the learning curve for using Sentinel.

* [Chromedriver](http://chromedriver.chromium.org/) 2.42 - Driver for automating Google Chrome.
* [Geckodriver](https://github.com/mozilla/geckodriver/releases) - Driver for automating Mozilla Firefox.
* [Safari](https://webkit.org/blog/6900/webdriver-support-in-safari-10/) - Safari driver is embedded in Safari.
* [IE Driver](http://selenium-release.storage.googleapis.com/index.html) - Driver for automating IE.

## 7.0 Versioning

We use [Semantic Versioning](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/dougnoel/sentinel/tags). 

## 8.0 Authors

* **Doug Noël** - *Architect* - Initial work.
* **Sirajuddin Shaik** - *Contributor* - Browser back, forward and refresh buttons.
* **Mahamad Gouse Shaik** - *Contributor* - iFrame handling.
* **Rama Neralla** - *Contributor* - Advanced click handling and rollover functionality. 
* **Will Warren** - *Contributor* - Using a yml file to set test system configurations, dynamic wait for page load, documentation, improved error handling.

## 9.0 License

This project is licensed under the Apache Commons 2.0 License - see the [LICENSE.md](LICENSE.md) file for details

## 10.0 Acknowledgments

* Some design choices inspired by [Cheezy's Page Object gem](https://github.com/cheezy/page-object).