[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dougnoel/sentinel/badge.svg)](https://search.maven.org/artifact/com.dougnoel/sentinel)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![codebeat badge](https://codebeat.co/badges/3d631b66-b308-43cd-87f1-626fde5851ca)](https://codebeat.co/projects/github-com-dougnoel-sentinel-master) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=security_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=coverage)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel)

# Sentinel - Selenium Automation Framework in Java

A Selenium framework that implements the [Page Object Model](http://cheezyworld.com/2010/11/09/ui-tests-not-brittle/) (POM) using [Object Oriented Programming](https://en.wikipedia.org/wiki/Object-oriented_programming) (OOP) concepts. It abstracts out most of the complexity of Selenium to allow users to focus on automating tests and not spend time dealing with the technical details of implementing an automation framework, so that automated tests can be written in a non-technical way in the same amount of time as manual tests can be executed.

## Quickstart
For those wanting to go crazy without reading lots of docs, get the [sentinel.example Project](https://github.com/dougnoel/sentinel.example), and checkout the [Sentinel Javadocs](https://dougnoel.github.io/sentinel/), especially the steps package. Sentinel is intended to be included in your test automation and used as a jar file. If you are writing tests, you should **NOT** be modifying the Sentinel code itself.

People wondering where they can write @Given, @When, and @Then steps should first be asking what has been written and attempting to reuse that instead of writing their own. If you are writing your own more than 1% of the time then you are not using the framework as intended and wasting time (or working on something really weird).

# Section 1: Using Sentinel

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

## 1.0 Getting Started

### 1.1 Prerequisites

What things you need to install the software and how to install them:
 * Java (11 or later) NOTE: Java 16 does not currently work.
 * Maven (2.5.4 or later)
 * Integrated Development Environment (Eclipse Suggested)
 * Google Chrome (suggested)

### 1.2 Installation
These instructions assume you are using Eclipse.

**On the command line:**
Clone the project from git.

```
git clone https://github.com/dougnoel/sentinel.git
```
1. Open Eclipse
2. File -> Open Projects from File system...
3. Next to Import Source click the Directory button and browse to the installed location.
4. Click the Finish button.
1. Right-Click on the project in the Project Explorer.
2. Maven -> Update Project...
3. Wait for the status bar in the lower right-hand corner to finish before continuing.

### Running the Tests
On the command line type: `mvn test`.

In Eclipse:
1. Expand **src/test/java/ -> tests**
2. Right-click on **SentinelTests.java** and select **Run As... -> JUnit Test**

## 2.0 Frequently Asked Questions (FAQs)

### How do I add command line options in Eclipse when running tests?
1. Go to **Run -> Run configurations...**
2. Under JUnit, select **SentinelTests** and in the **Name:** box to the right change the name to reflect the changes you are making.
3. Click on the **(x)= Arguments** tab.
4. In the **VM arguments:** box add a space after the existing arguments (-ea may be the only argument) and then put `-Dkey=value` For example to pass a different browser than chrome use `-Dbrowser=edge`.
5. Click the **Apply** button.
6. Click the **Close** button.
7. From the Toolbar, click the drop down arrow to the right of the Run toolbar icon <img src="images/eclipse_tool_bar_icon_run.png" height="14"> and select **SentinelTests - Dev**.

### How do I change the default timeout?
All timeouts default to 10 seconds, however you can change that either on the command line or in the sentinel.yml configuration file. For example if you want to double it to 20 seconds you would use `-Dtimeout=20`.

Perhaps your dev environment is giving you a problem and you want to set the wait time astronomically high, but leave it the same for your other environments, so you want to give it ten minutes because sometimes your job runs at night and you don't want it to hang up during a deploy. In that case you could set a value in the configuration file like so:

```
configurations:
  dev:
  	timeout=3600
```

It will wait ten minutes before failing on any wait in dev, but stick to the 10 second timeout in any other environment.

Timeout values must be whole numbers.

### How do I leave the browser open at the end of my test?
When running on the command line, you can use the argument `-DleaveBrowserOpen`. Ex:

```
mvn test -DleaveBrowserOpen
```

In Eclipse:
1. Go to **Run -> Run configurations...**
2. Under JUnit, select your test, right-click on it and select *Duplicate*
3. Under JUnit, select the new test and in the **Name:** box to the right change the name to reflect that the browser is staying open.
4. Click on the (x)= Arguments tab.
5. In the **VM arguments:** box add a space after the existing arguments (-ea may be the only argument) and then put `-DleaveBrowserOpen`
6. Click the **Apply** button.
7. Click the **Close** button.
8. From the Toolbar, click the drop down arrow to the right of the Run toolbar icon <img src="images/eclipse_tool_bar_icon_run.png" height="14"> and select your new test runner.

### How do I use an environment other than dev?
When running on the command line, you can use the argument `-Denv=`. For example for a stage environment:

```
mvn test -Denv=stage
```

1. In Eclipse, go to **Run -> Run configurations...**
2. Under JUnit, select **SentinelTests.java**, right-click on it and select **Duplicate**
3. Under JUnit, select the new test and in the **Name:** box to the right change the name to reflect the environment you want to use (e.g. **SentinelTests - Stage**.
4. Click on the **(x)= Arguments** tab.
5. In the **VM arguments:** box add a space after the existing arguments (-ea may be the only argument) and then put `-Denv=stage` or whicever environment you want to use.
6. Click the **Apply** button.
7. Click the **Close** button.
8. From the Toolbar, click the drop down arrow to the right of the Run toolbar icon <img src="images/eclipse_tool_bar_icon_run.png" height="14"> and select your new test runner (**SentinelTests - Stage**, etc).

### I only have one environment, can I hard code it?
Yes, you can but be aware this will affect not only your local, but anyone who pulls the code to run tests, as well as you CI/CD pipeline. You are better off configuring a test runner for each environment or passing it in on the command line. However if you need it, here's how:

1. Open SentinelTests.java
2. In the @BeforeClass section, add the following line `System.setProperty("env", "dev");` where `dev` is the environment you want to hard code.
3. Save the file.

**IMPORTANT NOTE:** Whenever you run your tests either on the command line or in your IDE, it will only ever use that environment and you will no longer have to pass that argument. Keep in mind you *cannot* pass that argument, it will always be overwritten by the above line. It is not a default, but an absolute. Sentinel prefers configuration over convention when it comes to test environments, because you should know where you are testing at all times.

### When I try to pass in a different environment on the command line it ignores it and uses another environment. How do I fix that?
Check `SentinelTests.java` and look for the string `System.setProperty("env"` and comment it out or delete it. Likely someone hard coded your environment. This setting overrides anything passed on the command line.

### How do I set the URL for my page object?
The URL for your page object is set in the page object yaml file. The file name needs to match the name you use for the page in your Cucumber steps without spaces. So if your page name is `Bits N Bobs Main Page` then your file needs to be named `BitsNBobsMainPage.yml`. Note that the word Page must be at the end of the file and it must have an upper case P. Then you just put in a `urls:` section and a default url. It will be used regardless of what environment is passed.

```
urls:
	default: http://myurl.com
```

### How do I set different URLs for specific environments?
If you want to define urls for different test environments (dev, qa, stage, uat, etc) then you can define them in your file. If a url is not found, the default url will be used. If no default is used, the tests will fail with an error message telling you that the url could not be found.

```
urls:
	default: http://myurl.com
	dev: http://dev.myurl.com
```

If you passed the **dev** environment using the above configuration, you would get `http://dev.myurl.com`. If you passed the **qa** environment, it would load the default `http://myurl.com`.

### How do I use a URL pattern for similar environments?
If you have multiple environments with the same naming convention, you do not need to spell out each one. Instead you can put the `{env}` specifier in your url and tag it with `default:` and it will be auto replaced by the environment name you have passed. If you want to override this default pattern, you just define the environment names you want to overload.

```
urls:
	default: http://{env}.myurl.com
	prod: http://myurl.com
```

If you passed the **prod** environment using the above configuration, you would get `http://myurl.com`. If you passed the **stage** environment, it would load the default and replace the name resulting in `http://stage.myurl.com`.

### None of my elements can be found! What's going on?
You need to ensure that you are telling the framework what page you are on. It keeps track of that by the `I am on the Page Name Page` and `I am redirected to the Page Name Page` steps. If you do not expect to be on a particular page, then the framework assumes you are still on the last page you started on, or no page if you have not told it you expect to be on your page. Defining page objects is an exercise that is helpful for us as people to be able to grasp a whole web site, but it means nothing to a computer. DEfine your page objects in such a way that people can understand the distinctions.

### How do I set default username and password account info across environments?
If you have the same test account across multiple environments, you can easily set them all at the same time. For example, your dev, qa and stage environments might share the same identification provider. Lets say there is a standard user and an admin user. You could set their values for all environments like so:

```
urls:
	default: http://myurl.com
accounts:
	default:
		StandardUser:
			username: user1
			password: badp@ssw0rd
		AdminUser:
			username: user2
			password: @n0therb@dp@ssw0rd
```

*Remember to update your passwords in the page object yaml when you update them in your test environments!*

### How do I set username and password account info for a specific environment?
If you have a specific test accounts, say a more secure admin one for your stage environment only, you could define it like so:

```
urls:
	default: http://myurl.com
accounts:
	default:
		StandardUser:
			username: user1
			password: badp@ssw0rd
		AdminUser:
			username: user2
			password: @n0therb@dp@ssw0rd
	stage:
		AdminUser:
			username: stageadmin
			password: 3h@njk#wnk{wdf76
```

In stage it would use your more secure admin account, but for normal user tests it would use your original account, and for all other environments it would use the default accounts.

### My tests were all passing and now some (or all) of my accounts can no longer log in! What's going on?
Check to make sure someone didn't update the tests account passwords. If they did, update your page object yaml files accordingly.

### How do I wait?
Sometimes you need to add an implicit wait to your tests due to the vagaries of the Internet. You can do this by using anything from `And I wait 0.001 seconds` to `And I wait 99 seconds`. You can use whole numbers or can add fractions of a second up to the thousandths place. For example if you want to wait a second and a half, use `And I wait 0.001 seconds`.

*NOTE: If you find yourself needing to use waits a lot, create a bug ticket in the Sentinel project and see if we can solve that problem.*

### How do I use a different/newer/custom version of Chrome?
If you want to use a different Chrome executable, you need to change the browser type to `customChrome` and set the `chromeBrowserBinary` value to the path of the executable you want to use.

```
mvn test -D browser=customChrome -DchromeBrowserBinary=path/to/executable/executableName
```

Alternately, you can just add the values to your sentinel.yml config file:

```
configurations:
  default:
    browser: customChrome
    chromeBrowserBinary: "path/to/executable/executableName"
```

### How do I run chrome in headless mode?
If you want to run chrome as a headless browser, you can change that either on the command line or in the sentinel.yml configuration file. On the command line you would use `-Dheadless` to use chrome in headless mode. This is the equivalent of passing `-Dheadless=true`. Alternately if headless is turned on in the configuration file and you need to override it, you can pass `-Dheadless=false` on the command line.

Perhaps your in your CI/CD pipeline, your dev environment is the only one not setup for browser compatibility, and you want to run the tests there as headless. In that case you could set a value in the configuration file like so:

```
configurations:
  dev:
  	headless: true
```

It's also possible that you need every environment except localhost to run headless. In this case you could setup the configuration file like so:

```
configurations:
  default:
  	headless: true
  localhost:
  	headless: false
```
*NOTE: Passing in a value on the command line will always override whatever is in the configuration file.*

### How do I run only certain tests?
If you want to run specific tagged tests, you can do so by passing in cucumber arguments on the command line. E.G. `mvn -Dcucumber.options="--tags @106" test`

You can also do so by customizing a Run Configuration.
1. Go to Run -> Run Configurations...
2. Make a copy of Sentinel Tests
3. Click on the arguments tab.
4. Change the VM arguments from `-ea` to something like `-ea -Dcucumber.options="--tags @106"` replacing the tag(s) you want to use with your tag.

For more information on command line options you can use for cucumber, you may refer to [this article](https://www.toolsqa.com/selenium-cucumber-framework/run-cucumber-test-from-command-line-terminal/).

### How do I run code coverage for my unit tests using Jacoco? ###
This script will run code coverage and then open up the results in your default browser. The results will not open if there are build failures.

```
src/test/resources/scripts/UnitTests.sh

```

### How to navigate to step definition file from feature file scenario step ###
Press CTRL + Click on feature file scenario, This should navigate to associated step defintion, but if this does not work, the follow below steps: 
1. On Eclipse go to Help
2. Select Eclipse Marketplace
3. Search for cucumber
4. result should return Cucumber eclipse plugin and Natural plugin
5. If none of them are installed then install only Cucumber eclipse plugin
6. If both are installed then uninstall Natural plugin and keep Cucumber plugin installed
7. After install the plugin, Right click on the Project --> Configure  --> Convert to Cucumber project
8. Open any feature file: Right click on feature file and select cucumber editor
9. Now CTRL + Click should navigate to step definition file. Enjoy coding :)

## 3.0 Deployment
Add additional notes about how to deploy this on a live system in Bamboo/Jenkins/etc.

### 3.1 Building a Jar File

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

If you want to do this all in one step without generating a jar, just run:

```
mvn install
```

### 3.2 Deploy a Snapshot
If you want to deploy a snapshot version for use by people, use:

```
mvn deploy
```

As long as you are setup to deploy to Maven central (see Section 5.3 below), to use the version, all someone will need to do is change their version to the SNAPSHOT version and have the following in their settings.xml:

```
  <repositories>
    <repository>
      <id>oss.sonatype.org-snapshot</id>
      <url>http://oss.sonatype.org/content/repositories/snapshots</url>
      <releases>
        <enabled>false</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
  ```

### 3.3 Deploy to Maven Central
[Instructions for Deploying to Maven Central](https://dzone.com/articles/publish-your-artifacts-to-maven-central)
Here is an article on [encrypting the PGP password in settings.xml](http://maven.apache.org/guides/mini/guide-encryption.html). This [git project](https://github.com/tdf/odftoolkit) also shows an example of how to push to Maven Central.

Had to run ```export GPG_TTY=$(tty)``` per this article on [using GPG on Mac](https://gist.github.com/repodevs/a18c7bb42b2ab293155aca889d447f1b) for this all to work.

(NOTE: The version needs to be X.X.X-SNAPSHOT for a release to work.)

When release:prepare and release:peform run, they pull command line arguments from the maven-release-plugin section of the pom.xml. These are in turn set in the properties section of the pom.xml under sentinel-release-os, sentinel-release-browser, and sentinel-release-pageobjectpackages at the top of the file. If you need to change these properties, change them here. If you need to add additional arguments, add them to the arguments section and follow the standard for adding the property values to the properties section of the file.

```
mvn clean

mvn release:prepare

mvn release:perform
```

After deployment, follow the [Sonatype Instructions for releasing a deployment](https://central.sonatype.org/pages/releasing-the-deployment.html).

#### 3.3.1 Install GPG Key
To create a GPG Key, you need a tool. The following tool for Mac has a UI [https://gpgtools.org/](https://gpgtools.org/). When creating a key, you must use the Advanced Settings and follow the instructions above.

## 4.0 Additional Documentation & Resources

### 4.1 Javadocs
Sentinel comes with Javadocs which describe its classes in great detail. This includes examples of how you can use the generic Cucumber steps that are already included. The [Sentinel Javadocs](https://dougnoel.github.io/sentinel/) are updated on github with every new version.

#### 4.1.1 Generating Javadocs
The Javadocs can also be easily generated by running the following command.

```
mvn javadoc:javadoc
```
The files are generated in the docs folder and can be accessed by opening up the index.html file contained within that folder. 

Every method should have a Javadoc comment describing what it does, its parameters, what it returns (if not void), 
and any exceptions it throws. We follow the [Liferay-Portal Javadoc Guidelines](https://github.com/liferay/liferay-portal/blob/master/readme/ADVANCED_JAVADOC_GUIDELINES.markdown) for writing Javadoc contents.

#### 4.1.2 Publishing Javadocs to Github
1. Make sure you have updated the version in the pomfile if necessary (pom.xml)
2. Copy the files from target/apidocs to docs/ (NOTE: The folder `docs/test` is used for unit testing and is not generated by Javadocs.)
3. Commit the changes and merge them with master. The files will be pulled from the docs folder and published on [https://dougnoel.github.io/sentinel/](https://dougnoel.github.io/sentinel/)

For more information, refer to [Configuring a publishing source for your GitHub Pages site](https://help.github.com/en/github/working-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site)
Additional information can be found under [About GitHub Pages and Jekyll](https://help.github.com/en/github/working-with-github-pages/about-github-pages-and-jekyll) and the [GitHub Pages site](https://pages.github.com/).

### 4.2 [Changelog](CHANGELOG.md)
The changelog is generated using [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator).

`github_changelog_generator -u dougnoel -p sentinel --token`

### 4.3 Built With
* [Cucumber](https://cucumber.io/) - BDD Testing Framework
* [Cucumber Extent Reporter](https://grasshopper.tech/1697/) Interface between Cucumber Results and Extent Reports.
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
* [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) 4.4.1 - Automatically detects browser versions and downloads the correct drivers.

### 4.4 Web Drivers
All web drivers are managed by [WebDriverManager](https://github.com/bonigarcia/webdrivermanager). Both the operating system and browser are automatically detected and the appropriate web driver is downloaded. Downloaded drivers are cached on individual boxes. The following browsers are supported:
* Chrome ([Chromedriver](http://chromedriver.chromium.org/))
* Edge ([Edgedriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/))
* Firefox ([Geckodriver](https://github.com/mozilla/geckodriver/releases))
* Internet Explorer ([IE Driver](http://selenium-release.storage.googleapis.com/index.html))
* Opera ([OperaChromiumDriver](https://github.com/operasoftware/operachromiumdriver/releases))
* Safari ([Safaridriver](https://webkit.org/blog/6900/webdriver-support-in-safari-10/))

### 4.5 Saucelabs
Sentinel is setup to use [Saucelabs](https://saucelabs.com/) for remote execution. This is the recommended way to execute test in your build pipeline, because you then do not need to setup an execution server.

## 5.0 Versioning

We use [Semantic Versioning](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/dougnoel/sentinel/tags). 

## 6.0 Authors

* **Doug Noël** - *Architect* - Initial work.

## 7.0 License

This project is licensed under the Apache Commons 2.0 License - see the [LICENSE.md](LICENSE.md) file for details

## 8.0 Acknowledgments

* Some design choices inspired by [Cheezy's Page Object gem](https://github.com/cheezy/page-object).