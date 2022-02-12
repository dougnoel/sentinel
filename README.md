[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dougnoel/sentinel/badge.svg)](https://search.maven.org/artifact/com.dougnoel/sentinel)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![codebeat badge](https://codebeat.co/badges/3d631b66-b308-43cd-87f1-626fde5851ca)](https://codebeat.co/projects/github-com-dougnoel-sentinel-master) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=security_rating)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dougnoel_sentinel&metric=coverage)](https://sonarcloud.io/dashboard?id=dougnoel_sentinel)

# Sentinel - Selenium Automation Framework in Java

A Selenium framework that implements the [Page Object Model](http://cheezyworld.com/2010/11/09/ui-tests-not-brittle/) (POM) using [Object Oriented Programming](https://en.wikipedia.org/wiki/Object-oriented_programming) (OOP) concepts. It abstracts out most of the complexity of Selenium to allow users to focus on automating tests and not spend time dealing with the technical details of implementing an automation framework, so that automated tests can be written in a non-technical way in the same amount of time as manual tests can be executed.

## Quickstart
If you want to write tests, you are in the wrong place. Head over to the [sentinel.example Project](https://github.com/dougnoel/sentinel.example) to get started. There is extensive documentation there along with sample code to get you up and running in minutes. You can also check out the [Sentinel Javadocs](https://dougnoel.github.io/sentinel/), especially the steps package. Sentinel is intended to be included in your test automation and used as a jar file. If you are writing tests, you should **NOT** be modifying the Sentinel code itself.

People wondering where they can write glue code @Given, @When, and @Then steps should first be asking what has been written and attempting to reuse that instead of writing their own. If you are writing your own more than 1% of the time then you are not using the framework as intended and wasting time (or working on something really weird).

# Section 1: Using Sentinel

These instructions will get you a copy of the project up and running on your local machine for development purposes. See deployment for notes on how to deploy the project to Maven Central.

## 1.0 Getting Started

### 1.1 Prerequisites

What things you need to install the software and how to install them:
 * Java (11 or later)
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

### How do I create page objects, feature files and new glue code steps?
All of this is explained at length in the [sentinel.example Project](https://github.com/dougnoel/sentinel.example) project. If you want to use this framework to write tests, that is the code you need to check out - not this. You check this code out to contribute to the project.

### How do I run code coverage for my unit tests using Jacoco? ###
This script will run code coverage and then open up the results in your default browser. The results will not open if there are build failures.

```
src/test/resources/scripts/UnitTests.sh

```

### How do I navigate to step definition file from feature file scenario step? ###
Press CTRL + Click (Command + Click on a Mac) on the feature file scenario, This should navigate to associated step defintion, but if this does not work, the follow below steps: 
1. In Eclipse go to Help
2. Select Eclipse Marketplace
3. Search for cucumber
4. result should return Cucumber eclipse plugin and Natural plugin
5. If none of them are installed then install only Cucumber eclipse plugin
6. If both are installed then uninstall Natural plugin and keep Cucumber plugin installed
7. After install the plugin, Right click on the Project --> Configure  --> Convert to Cucumber project
8. Open any feature file: Right click on feature file and select cucumber editor
9. Now CTRL + Click (Command + Click) should navigate to the step definition file. Enjoy coding :)

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
2. Run `mvn javadoc:javadoc`
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