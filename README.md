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

## 2.0 - 4.0
These sections have been moved to the [sentinel.example Project](https://github.com/dougnoel/sentinel.example). Please refer to that Readme for how to create and execute tests.

## 4.0 Frequently Asked Questions (FAQs)

### How do I change the default timeout?
All timeouts default to 10 seconds, however you can change that either on the commandline or in the sentinel.yml config file. For example if you want to double it to 20 seconds you would use `-Dtimeout=20`. If you wanted it to fail fast after only half a second you could use `-Dtimeout=500 -Dtimeunit=MILLISECONDS`. If you realy wanted to extend the time, you could etend it to a minute using `-Dtimeout=1 -Dtimeunit=MINUTES`.

Perhaps your dev environment is giving you a problem and you want to set the wait time astronomically high, but leave it the same for your other environments, so you want to give it an hour because sometimes your job runs at night and you don't want it to hangup during a deploy. In that case you could set a value in the conf file like so:

```
configurations:
  dev:
  	timeout=10
  	timeunit=MINUTES
```

It will wait ten minutes before failing on any wait in dev, but stick to the 10 second timeout in any other environment.

All timeout values must be whole numbers. The valid values for timeunit are DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS. It is not recommended that you use anything other than SECONDS or MILLISECONDS. MINUTES and HOURS can be used on nightly regression jobs to deal with infrastructure instability issues you cannot control. Your tests will likely all fail if you use MICROSECONDS or NANOSECONDS, but they'll fail fast, and that's the Agile way!

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

As long as you are setup to deploy to Maven central (see Section 5.3 below), to use the version, all someone will need to do is change their version to the SNAPSHOT version and have the following in their pom.xml:

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

The web drivers are stored in src/main/resources/drivers/[os] to make sure there is only one place to fix driver compatibility issues. Chrome auto updates, and so is the one that will go of date most often. While we could pull the driver from a path and let each implementation install the drivers, this can become problematic in CI/CD environments where we do not control the system. This also reduces the learning curve for using Sentinel.
NOTE: All drivers are 64-bit versions. If you need to test on an old 32-bit browser, you will need to replace the drivers provided with a 32-bit driver. See the driver creators for support.

* [Chromedriver](http://chromedriver.chromium.org/) 80.0.3987.106 (2020-02-13) - Driver for automating Google Chrome.
* [Geckodriver](https://github.com/mozilla/geckodriver/releases) v0.26.0 (Oct 11 2019) - Driver for automating Mozilla Firefox.
* [IE Driver](http://selenium-release.storage.googleapis.com/index.html) 3.9 (2018-02-05) - Driver for automating IE.
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