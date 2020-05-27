[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dougnoel/sentinel/badge.svg)](https://search.maven.org/artifact/com.dougnoel/sentinel)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d966affc00454cf392c4820074aa783d)](https://www.codacy.com/manual/dougnoel/sentinel?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dougnoel/sentinel&amp;utm_campaign=Badge_Grade)[![codebeat badge](https://codebeat.co/badges/3d631b66-b308-43cd-87f1-626fde5851ca)](https://codebeat.co/projects/github-com-dougnoel-sentinel-master)

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
 * Integrated Development Environment (Eclipse Suggested)
 * Java 8 (1.8) (Java 9 and later are NOT supported by Cucumber)
 * Maven (2.5.4 or later)

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
On the command line type: `mvn test -Denv=dev browser=chrome os=mac` or if you have your `sentinel.yml` created in conf, you can just run `mvn test -Denv=dev`

In Eclipse:
1. Expand **src/test/java/ -> tests**
2. Right-click on **SentinelTests.java** and select **Run As... -> JUnit Test**
3. The test will fail because no environment is set.
4. Go to **Run -> Run configurations...**
2. Under JUnit, select **SentinelTests** and in the **Name:** box to the right change the name to *vSentinelTests - Dev**.
4. Click on the **(x)= Arguments** tab.
5. In the **VM arguments:** box add a space after the existing arguments (-ea may be the only argument) and then put `-Denv=dev`
6. Click the **Apply** button.
7. Click the **Close** button.
8. From the Toolbar, click the drop down arrow to the right of the Run toolbar icon <img src="images/eclipse_tool_bar_icon_run.png" height="14"> and select **SentinelTests - Dev**.

## 2.0 - 4.0
These sections have been moved to the [sentinel.example Project](https://github.com/dougnoel/sentinel.example). Please refer to that Readme for how to create and execute tests.

## 3.0 Release Notes

### 1.0.4

**Bug Fixes:**
- Waits using the Cucumber step `I wait x seconds` were always waiting at least one second. They will now wait the actual amount of time they ar intended to wait.

## 4.0 Frequently Asked Questions (FAQs)

### How do I change the default timeout?
All timeouts default to 10 seconds, however you can change that either on the command line or in the sentinel.yml configuration file. For example if you want to double it to 20 seconds you would use `-Dtimeout=20`. If you wanted it to fail fast after only half a second you could use `-Dtimeout=500 -Dtimeunit=MILLISECONDS`. If you really wanted to extend the time, you could extend it to a minute using `-Dtimeout=1 -Dtimeunit=MINUTES`.

Perhaps your dev environment is giving you a problem and you want to set the wait time astronomically high, but leave it the same for your other environments, so you want to give it an hour because sometimes your job runs at night and you don't want it to hang up during a deploy. In that case you could set a value in the configuration file like so:

```
configurations:
  dev:
  	timeout=10
  	timeunit=MINUTES
```

It will wait ten minutes before failing on any wait in dev, but stick to the 10 second timeout in any other environment.

All timeout values must be whole numbers. The valid values for `timeunit` are DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS. It is not recommended that you use anything other than SECONDS or MILLISECONDS. MINUTES and HOURS can be used on nightly regression jobs to deal with infrastructure instability issues you cannot control. Your tests will likely all fail if you use MICROSECONDS or NANOSECONDS, but they'll fail fast, and that's the Agile way!

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

### I only have one environment, can I hardcode it?
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
You need to ensure that you are telling the framework what page you are on. It keeps track of that by the `I am on the Page Name Page` steps. If you do not expect to be on a particular page, then the framework assumes you are still on the last page you started on, or no page if you have not told it you expect to be on your page. Defining page objects is an exercise that is helpful for us as people to be able to grasp a whole web site, but it means nothing to a computer. DEfine your page objects in such a way that people can understand the distinctions.

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

### How do I use a different/newer/custom webdriver?
If you want to update the default driver used, just overwrite the driver in the src/main/resources/drivers folder under the correct OS. If you are updating a linux or mac driver, ensure the driver is executable by typing `chmod x drivername` on the command line in the folder where the driver resides.

*NOTE: chmod +x is not the most secure way to do this, but an explanation of users and groups in linux is beyond this document.*

You can also pass a custom driver in on the command line or in the configuration file. This is here to allow you to use different driver versions that the one currently bundled with Sentinel. Note, this doesn't work with Safari, as its driver is bundled with the browser in the operating system. You could also potentially use this method if your local computer has a version of chrome that has not yet been released and you need to use a beta driver.

```
mvn test -Ddriver=path/to/driver/drivername
```

Alternately, you can just add the path to your sentinel.yml config file:

```
configurations:
  default:
    driver: "path/to/driver/drivername"
```

*NOTE: This is only intended as a way to temporarily update a driver. You can only pass in a path for one driver at a time. If you hard code it in your configuration file and attempt to switch browsers, you will need to comment this line out or the custom driver will be used and your execution will fail.*

### Chromedriver is telling me it timed out or I have the wrong version, what do I do?
If you get something like the below errors, your Chromedriver is likely out of date.

```
org.openqa.selenium.TimeoutException: timeout: Timed out receiving message from renderer: -0.001
  (Session info: chrome=81.0.4044.138)
Build info: version: '3.141.59', revision: 'e82be7d358', time: '2018-11-14T08:17:03'
System info: host: 'Optimus.attlocal.net', ip: '2600:1700:1850:6c90:0:0:0:45%en0', os.name: 'Mac OS X', os.arch: 'x86_64', os.version: '10.15.4', java.version: '11.0.2'
Driver info: org.openqa.selenium.chrome.ChromeDriver
Capabilities {acceptInsecureCerts: false, browserName: chrome, browserVersion: 81.0.4044.138, chrome: {chromedriverVersion: 81.0.4044.69
```

```
17:27:57.355 ERROR webdrivers.ChromeDriverFactory (ChromeDriverFactory.java:41) - session not created: This version of ChromeDriver only supports Chrome version 81
```

*NOTE: Chrome automatically updates, and it will do so while you are using it, which can cause your tests to suddenly fail.*

To update Chrome, first determine your version of Chrome:
1. Open Chrome.
2. Click on the three vertical dots on the right-hand side of the window past the address (url) bar.
3. Select Help -> About Google Chrome
4. Go to the Google Chrome Download page (See section 6.3 for a link and the current version included with Sentinel).
5. Download the version that most closely matches your browser version.
6. Copy that file to the src/main/resources/drivers/[os] folder, where [os] is the operating system of your computer.

*NOTE: You may need to update the version for other operating systems if your co-workers or CI/CD pipeline use different operating systems.*

## 5.0 Deployment

Add additional notes about how to deploy this on a live system in Bamboo/Jenkins/etc.

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

If you want to do this all in one step without generating a jar, just run:

```
mvn install
```

### 5.2 Deploy a Snapshot
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

### 5.3 Deploy to Maven Central
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

## 6.0 Additional Documentation & Resources

### 6.1 Javadocs
Sentinel comes with Javadocs which describe its classes in great detail. This includes examples of how you can use the generic Cucumber steps that are already included. The [Sentinel Javadocs](https://dougnoel.github.io/sentinel/) are updated on github with every new version.

#### 6.1.1 Generating Javadocs
The Javadocs can also be easily generated by running the following command.

```
mvn javadoc:javadoc
```
The files are generated in the project root folder under the default path of target/site/apidocs and can be accessed by opening up the index.html file contained within that folder.

The following commands will patch the updated javadocs, however it deletes the test table and updates every document. Looking to see if there's a better way to update only the changed docs.
```
diff -ruN docs/ target/apidocs/ > javadocs.patch
patch -p0 < javadocs.patch
rm javadocs.patch
git checkout -- docs/test/table.html
```

Every method should have a Javadoc comment describing what it does, its parameters, what it returns (if not void), and any exceptions it throws. We follow the [Liferay-Portal Javadoc Guidelines](https://github.com/liferay/liferay-portal/blob/master/readme/ADVANCED_JAVADOC_GUIDELINES.markdown) for writing Javadoc contents.

#### 6.1.2 Publishing Javadocs to Github
1. Make sure you have updated the version in the pomfile if necessary (pom.xml)
2. Copy the files from target/site/apidocs to docs/
3. Commit the changes and merge them with master. The files will be pulled from the docs folder and published on [https://dougnoel.github.io/sentinel/](https://dougnoel.github.io/sentinel/)

For more information, refer to [Configuring a publishing source for your GitHub Pages site](https://help.github.com/en/github/working-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site)
Additional information can be found under [About GitHub Pages and Jekyll](https://help.github.com/en/github/working-with-github-pages/about-github-pages-and-jekyll) and the [GitHub Pages site](https://pages.github.com/).

### 6.2 Built With

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

### 6.3 Web Drivers

The web drivers are stored in src/main/resources/drivers/[os] to make sure there is only one place to fix driver compatibility issues. Chrome auto updates, and so is the one that will go of date most often. While we could pull the driver from a path and let each implementation install the drivers, this can become problematic in CI/CD environments where we do not control the system. This also reduces the learning curve for using Sentinel.
NOTE: All drivers are 64-bit versions. If you need to test on an old 32-bit browser, you will need to replace the drivers provided with a 32-bit driver. See the driver creators for support.

* [Chromedriver](http://chromedriver.chromium.org/) 83.0.4103.39 (2020-05-05) - Driver for automating Google Chrome.
* [Geckodriver](https://github.com/mozilla/geckodriver/releases) v0.26.0 (Oct 11 2019) - Driver for automating Mozilla Firefox.
* [IE Driver](http://selenium-release.storage.googleapis.com/index.html) 3.9 (2018-02-05) - Driver for automating IE. (*This driver version matches the selenium version being used and NOT the IE Version.*)
* [Safari](https://webkit.org/blog/6900/webdriver-support-in-safari-10/) - Safari driver is embedded in Safari.

### 6.4 Saucelabs
Sentinel is setup to use [Saucelabs](https://saucelabs.com/) for remote execution. This is the recommended way to execute test in your build pipeline, because you then do not need to setup an execution server.

## 7.0 Versioning

We use [Semantic Versioning](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/dougnoel/sentinel/tags). 

## 8.0 Authors

* **Doug Noël** - *Architect* - Initial work.

## 9.0 License

This project is licensed under the Apache Commons 2.0 License - see the [LICENSE.md](LICENSE.md) file for details

## 10.0 Acknowledgments

* Some design choices inspired by [Cheezy's Page Object gem](https://github.com/cheezy/page-object).