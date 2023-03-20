# Changelog

## [Unreleased](https://github.com/dougnoel/sentinel/tree/HEAD)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/v1.0.12-SNAPSHOT...HEAD)

**Implemented enhancements:**

- Unable to check that a select element has an empty selection [\#453](https://github.com/dougnoel/sentinel/issues/453)
- Add ability to use stored text in table's clickAssociatedLinkInTable step [\#379](https://github.com/dougnoel/sentinel/issues/379)
- Add steps for verifying element relative location [\#371](https://github.com/dougnoel/sentinel/issues/371)
- Improvement: add methods to table to check that all cells in a column are \(or are not\) empty [\#346](https://github.com/dougnoel/sentinel/issues/346)
- Modify Screenshot Comparison To Account For Windows Elements [\#336](https://github.com/dougnoel/sentinel/issues/336)
- click point on element [\#333](https://github.com/dougnoel/sentinel/issues/333)
- getElementAsCustom [\#331](https://github.com/dougnoel/sentinel/issues/331)
- Implement the image-comparison library [\#298](https://github.com/dougnoel/sentinel/issues/298)
- Implement WinAppDriver to automate windows [\#239](https://github.com/dougnoel/sentinel/issues/239)
- Implement Screen Recording [\#207](https://github.com/dougnoel/sentinel/issues/207)
- Add an OnHover Test Step [\#197](https://github.com/dougnoel/sentinel/issues/197)

**Fixed bugs:**

- Fix Unit Test Timeouts [\#506](https://github.com/dougnoel/sentinel/issues/506)
- Table does not check to see if it needs to be updated if a comparison fails. [\#396](https://github.com/dougnoel/sentinel/issues/396)
- Table cell text verification does not take into account tables with no true header elements [\#375](https://github.com/dougnoel/sentinel/issues/375)
- Custom element types in projects extending sentinel cause exception [\#367](https://github.com/dougnoel/sentinel/issues/367)
- PrimeNG Dropdown test fails [\#351](https://github.com/dougnoel/sentinel/issues/351)
- Multiple SLF4J Bindings [\#309](https://github.com/dougnoel/sentinel/issues/309)
- Sentinel should recover from a driver.quit\(\) [\#261](https://github.com/dougnoel/sentinel/issues/261)
- Base step to go to a new tab throws error [\#223](https://github.com/dougnoel/sentinel/issues/223)

**Closed issues:**

- Implement download test page + Same-Origin test file [\#509](https://github.com/dougnoel/sentinel/issues/509)
- Table verification: need ability to numerically compare values in a column to a reference value [\#491](https://github.com/dougnoel/sentinel/issues/491)
- Finding elements in iFrames is slow [\#479](https://github.com/dougnoel/sentinel/issues/479)
- Updates to table page for PR 449 [\#451](https://github.com/dougnoel/sentinel/issues/451)
- Processed number comparison [\#448](https://github.com/dougnoel/sentinel/issues/448)
- Calculator needed for value modification testing [\#446](https://github.com/dougnoel/sentinel/issues/446)
- Add "column does not contain" support [\#433](https://github.com/dougnoel/sentinel/issues/433)
- Fix table test instability [\#426](https://github.com/dougnoel/sentinel/issues/426)
- Textbox page needs to be updated for new tests [\#421](https://github.com/dougnoel/sentinel/issues/421)
- IsDisabled and IsEnabled need to be updated to handle the case of Readonly [\#419](https://github.com/dougnoel/sentinel/issues/419)
- hasAttribute and doesNotHaveAttribute are throwing when unexpected [\#417](https://github.com/dougnoel/sentinel/issues/417)
- Changing windows by title [\#415](https://github.com/dougnoel/sentinel/issues/415)
- Need to implement checking a table's specified row/column entry against a stored value [\#410](https://github.com/dougnoel/sentinel/issues/410)
- Update contributions.md to specify merging from forked repos is unsupported [\#408](https://github.com/dougnoel/sentinel/issues/408)
- Sendkeys for file uploads [\#393](https://github.com/dougnoel/sentinel/issues/393)
- Backspace send key support [\#384](https://github.com/dougnoel/sentinel/issues/384)
- Enter Random Text gherkin is incorrect [\#369](https://github.com/dougnoel/sentinel/issues/369)
- Relative path shortcut support - Windows automation [\#366](https://github.com/dougnoel/sentinel/issues/366)
- Make PDF Downloadable for test [\#357](https://github.com/dougnoel/sentinel/issues/357)
- Host PDF on github site [\#353](https://github.com/dougnoel/sentinel/issues/353)
- Update setup to mandate the winapp driver setup \(for unit tests\) [\#349](https://github.com/dougnoel/sentinel/issues/349)
- Duplicate hover step [\#344](https://github.com/dougnoel/sentinel/issues/344)
- Window switcher does not wait for new windows [\#340](https://github.com/dougnoel/sentinel/issues/340)
- Wait until attribute has value [\#338](https://github.com/dougnoel/sentinel/issues/338)
- attribute contains gherkin glue [\#325](https://github.com/dougnoel/sentinel/issues/325)
- fullscreen flag [\#319](https://github.com/dougnoel/sentinel/issues/319)
- Ambiguous step definitions in TableVerificationSteps [\#316](https://github.com/dougnoel/sentinel/issues/316)
- Table Header Click Gherkin [\#315](https://github.com/dougnoel/sentinel/issues/315)
- Tooltip glue too similar to other functions [\#313](https://github.com/dougnoel/sentinel/issues/313)
- Modify image comparison code to work with windows elements [\#303](https://github.com/dougnoel/sentinel/issues/303)

**Merged pull requests:**

- Added download test page that creates a same-origin text file on pageload [\#510](https://github.com/dougnoel/sentinel/pull/510) ([tyBouch](https://github.com/tyBouch))
- Updated downloads to use the long prcoess timeout of 60 seconds. \(Or … [\#507](https://github.com/dougnoel/sentinel/pull/507) ([dougnoel](https://github.com/dougnoel))
- Add ability to clear config [\#499](https://github.com/dougnoel/sentinel/pull/499) ([sampacos](https://github.com/sampacos))
- Bump webdrivermanager from 5.3.1 to 5.3.2 [\#496](https://github.com/dougnoel/sentinel/pull/496) ([dependabot[bot]](https://github.com/apps/dependabot))
- Add table verification step. Add screenshots for failing tests [\#492](https://github.com/dougnoel/sentinel/pull/492) ([sampacos](https://github.com/sampacos))
- Bump maven-failsafe-plugin from 3.0.0-M3 to 3.0.0-M8 [\#490](https://github.com/dougnoel/sentinel/pull/490) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump gson from 2.8.9 to 2.10.1 [\#489](https://github.com/dougnoel/sentinel/pull/489) ([dependabot[bot]](https://github.com/apps/dependabot))
- Windows Application Testing refactor to Appium 1.2 [\#484](https://github.com/dougnoel/sentinel/pull/484) ([pturchinetz](https://github.com/pturchinetz))
- Speedup element finds, especially in iframes [\#480](https://github.com/dougnoel/sentinel/pull/480) ([sampacos](https://github.com/sampacos))
- 468 more csv 2 [\#478](https://github.com/dougnoel/sentinel/pull/478) ([sampacos](https://github.com/sampacos))
- add header feature [\#477](https://github.com/dougnoel/sentinel/pull/477) ([david-ondrus](https://github.com/david-ondrus))
- Bump junit-bom from 5.5.1 to 5.9.2 [\#476](https://github.com/dougnoel/sentinel/pull/476) ([dependabot[bot]](https://github.com/apps/dependabot))
- Api [\#475](https://github.com/dougnoel/sentinel/pull/475) ([dougnoel](https://github.com/dougnoel))
- 468 more csv [\#474](https://github.com/dougnoel/sentinel/pull/474) ([sampacos](https://github.com/sampacos))
- add a comment column on the html table page with an input inside [\#473](https://github.com/dougnoel/sentinel/pull/473) ([sampacos](https://github.com/sampacos))
- Bump httpcore from 4.4.12 to 4.4.16 [\#472](https://github.com/dougnoel/sentinel/pull/472) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump commons-lang3 from 3.9 to 3.12.0 [\#471](https://github.com/dougnoel/sentinel/pull/471) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump httpclient from 4.5.13 to 4.5.14 [\#470](https://github.com/dougnoel/sentinel/pull/470) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump maven-dependency-plugin from 3.1.1 to 3.4.0 [\#466](https://github.com/dougnoel/sentinel/pull/466) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump pdfbox from 2.0.24 to 2.0.27 [\#465](https://github.com/dougnoel/sentinel/pull/465) ([dependabot[bot]](https://github.com/apps/dependabot))
- Api [\#462](https://github.com/dougnoel/sentinel/pull/462) ([dougnoel](https://github.com/dougnoel))
- 458 reporting mvp [\#461](https://github.com/dougnoel/sentinel/pull/461) ([dougnoel](https://github.com/dougnoel))
- 2.0 readme [\#457](https://github.com/dougnoel/sentinel/pull/457) ([dougnoel](https://github.com/dougnoel))
- 453 empty select and new download verification step [\#455](https://github.com/dougnoel/sentinel/pull/455) ([sampacos](https://github.com/sampacos))
- html changes needed for 453 feature branch [\#454](https://github.com/dougnoel/sentinel/pull/454) ([sampacos](https://github.com/sampacos))
- Updated the table page for new tests [\#452](https://github.com/dougnoel/sentinel/pull/452) ([tyBouch](https://github.com/tyBouch))
- Bump webdrivermanager from 4.4.1 to 5.3.1 [\#450](https://github.com/dougnoel/sentinel/pull/450) ([dependabot[bot]](https://github.com/apps/dependabot))
- Tyler/enhancements 10 31 2022 [\#449](https://github.com/dougnoel/sentinel/pull/449) ([tyBouch](https://github.com/tyBouch))
- Tyler/math verification test page [\#447](https://github.com/dougnoel/sentinel/pull/447) ([tyBouch](https://github.com/tyBouch))
- Adding replaceAll\("\[\t\n\r\]+"," "\) so that it can handle a column hea… [\#444](https://github.com/dougnoel/sentinel/pull/444) ([plmlkrz](https://github.com/plmlkrz))
- Adding xPathToBy and xPathToBy\(int ordinal\) [\#443](https://github.com/dougnoel/sentinel/pull/443) ([plmlkrz](https://github.com/plmlkrz))
- Bump io.cucumber.version from 5.6.0 to 7.8.1 [\#435](https://github.com/dougnoel/sentinel/pull/435) ([dependabot[bot]](https://github.com/apps/dependabot))

## [v1.0.12-SNAPSHOT](https://github.com/dougnoel/sentinel/tree/v1.0.12-SNAPSHOT) (2022-03-28)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/v1.0.10-SNAPSHOT...v1.0.12-SNAPSHOT)

**Implemented enhancements:**

- Add verification of specific rows in tables [\#305](https://github.com/dougnoel/sentinel/issues/305)
- Add ability to send special keys to element [\#292](https://github.com/dougnoel/sentinel/issues/292)
- Add the ability to pass through Saucelabs configuration options [\#271](https://github.com/dougnoel/sentinel/issues/271)
- Need to create a findElement\(\) method for Element objects [\#56](https://github.com/dougnoel/sentinel/issues/56)

**Fixed bugs:**

- Hover Cucumber Step Was Removed [\#304](https://github.com/dougnoel/sentinel/issues/304)
- MaterialUITest Fails on Firefox [\#214](https://github.com/dougnoel/sentinel/issues/214)

**Closed issues:**

- Fix download directory creation and fetching during chrome setup [\#301](https://github.com/dougnoel/sentinel/issues/301)
- Implement visual PDF comparison [\#300](https://github.com/dougnoel/sentinel/issues/300)
- Cannot switch back to parent window if child window closes by itself [\#295](https://github.com/dougnoel/sentinel/issues/295)
- Table getElementInRowThatContains\(int, By\) searches table structure incorrectly for element [\#291](https://github.com/dougnoel/sentinel/issues/291)
- Log internally created custom steps in results [\#255](https://github.com/dougnoel/sentinel/issues/255)

**Merged pull requests:**

- Revving to 1.0.12 [\#308](https://github.com/dougnoel/sentinel/pull/308) ([dougnoel](https://github.com/dougnoel))
- Adding a mouse hover gherkin command, editing a test to exercise it [\#307](https://github.com/dougnoel/sentinel/pull/307) ([pturchinetz](https://github.com/pturchinetz))
- add table verification step for getting specific cell data [\#306](https://github.com/dougnoel/sentinel/pull/306) ([sampacos](https://github.com/sampacos))
- Implement pdf compare and fix download manager [\#302](https://github.com/dougnoel/sentinel/pull/302) ([sampacos](https://github.com/sampacos))
- Pre-release 1.0.11-SNAPSHOT on maven central. Updated Changelog and J… [\#297](https://github.com/dougnoel/sentinel/pull/297) ([dougnoel](https://github.com/dougnoel))
- add method for switching to parent window [\#296](https://github.com/dougnoel/sentinel/pull/296) ([sampacos](https://github.com/sampacos))

## [v1.0.10-SNAPSHOT](https://github.com/dougnoel/sentinel/tree/v1.0.10-SNAPSHOT) (2022-02-23)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/v2.0.0-SNAPSHOT...v1.0.10-SNAPSHOT)

**Merged pull requests:**

- Pre-release 1.0.10-SNAPSHOT on maven central. Updated Changelog and J… [\#294](https://github.com/dougnoel/sentinel/pull/294) ([dougnoel](https://github.com/dougnoel))
- 291 table fixes and 292 add element method for sending special keys [\#293](https://github.com/dougnoel/sentinel/pull/293) ([sampacos](https://github.com/sampacos))

## [v2.0.0-SNAPSHOT](https://github.com/dougnoel/sentinel/tree/v2.0.0-SNAPSHOT) (2022-02-21)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/v1.0.9-SNAPSHOT...v2.0.0-SNAPSHOT)

## [v1.0.9-SNAPSHOT](https://github.com/dougnoel/sentinel/tree/v1.0.9-SNAPSHOT) (2022-02-16)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/1.0.7-SNAPSHOT...v1.0.9-SNAPSHOT)

**Implemented enhancements:**

- Move message creation to exception code [\#273](https://github.com/dougnoel/sentinel/issues/273)
- Cleanup Page Object Locations [\#243](https://github.com/dougnoel/sentinel/issues/243)
- Refactor PageElement to be Element [\#202](https://github.com/dougnoel/sentinel/issues/202)
- PrimeNG has been updated again [\#175](https://github.com/dougnoel/sentinel/issues/175)
- Add find\(By locator\) method to Element objects [\#125](https://github.com/dougnoel/sentinel/issues/125)

**Closed issues:**

- Multiple methods can throw undesirable StaleElementReference exceptions  [\#287](https://github.com/dougnoel/sentinel/issues/287)
- StaleElementReferenceException getting thrown in Element.java sendKeysLoop\(\) [\#282](https://github.com/dougnoel/sentinel/issues/282)
- Create iFrame Test Page [\#263](https://github.com/dougnoel/sentinel/issues/263)
- Dynamic iFrame Handling [\#262](https://github.com/dougnoel/sentinel/issues/262)
- Modify the Page.createElement\(\) method to autodetect the class type so that new Element types can be created without modifying this method [\#258](https://github.com/dougnoel/sentinel/issues/258)
- Create MetabolonDropdown [\#256](https://github.com/dougnoel/sentinel/issues/256)
- Refactor Exceptions [\#203](https://github.com/dougnoel/sentinel/issues/203)

**Merged pull requests:**

- Revving to version 1.0.9-SNAPSHOT [\#290](https://github.com/dougnoel/sentinel/pull/290) ([dougnoel](https://github.com/dougnoel))
- 287 Fix uncaught StaleElementReferenceException and add multiple enhancements [\#289](https://github.com/dougnoel/sentinel/pull/289) ([sampacos](https://github.com/sampacos))
- 282 Fix sendKeysLoop [\#283](https://github.com/dougnoel/sentinel/pull/283) ([sampacos](https://github.com/sampacos))
- Updates logging to show less unnecessary spam. [\#276](https://github.com/dougnoel/sentinel/pull/276) ([dougnoel](https://github.com/dougnoel))
- 273 exception messages [\#274](https://github.com/dougnoel/sentinel/pull/274) ([dougnoel](https://github.com/dougnoel))
- Added ability to pass any saucelabs option. [\#272](https://github.com/dougnoel/sentinel/pull/272) ([dougnoel](https://github.com/dougnoel))
- Fix log4j vulnerability. Updated to 2.16.0 [\#270](https://github.com/dougnoel/sentinel/pull/270) ([dougnoel](https://github.com/dougnoel))
- Adding checkboxes [\#268](https://github.com/dougnoel/sentinel/pull/268) ([dougnoel](https://github.com/dougnoel))
- 263 create iframe test page [\#267](https://github.com/dougnoel/sentinel/pull/267) ([dougnoel](https://github.com/dougnoel))
- Added textbox.html to docs folder to test disabled clicks and enterin… [\#266](https://github.com/dougnoel/sentinel/pull/266) ([dougnoel](https://github.com/dougnoel))
- 262 dynamic iframes [\#265](https://github.com/dougnoel/sentinel/pull/265) ([dougnoel](https://github.com/dougnoel))
- Adding in iframe tst pages. [\#264](https://github.com/dougnoel/sentinel/pull/264) ([dougnoel](https://github.com/dougnoel))
- Adding 2 Metabolon portal dropdowns, with some in-progress table work… [\#260](https://github.com/dougnoel/sentinel/pull/260) ([pturchinetz](https://github.com/pturchinetz))
- Added imbedded logging steps as an example. [\#259](https://github.com/dougnoel/sentinel/pull/259) ([dougnoel](https://github.com/dougnoel))
- Moved all test writing FAQ questions to the sentinel.example project.… [\#254](https://github.com/dougnoel/sentinel/pull/254) ([dougnoel](https://github.com/dougnoel))

## [1.0.7-SNAPSHOT](https://github.com/dougnoel/sentinel/tree/1.0.7-SNAPSHOT) (2021-07-16)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/1.0.5...1.0.7-SNAPSHOT)

**Implemented enhancements:**

- Unexpose Selenium [\#247](https://github.com/dougnoel/sentinel/issues/247)
- Update timeouts for negative tests [\#246](https://github.com/dougnoel/sentinel/issues/246)
- Add unit tests for checkboxes [\#216](https://github.com/dougnoel/sentinel/issues/216)
- Create a Selenium Grid Driver [\#209](https://github.com/dougnoel/sentinel/issues/209)
- Implement Edge browser driver [\#206](https://github.com/dougnoel/sentinel/issues/206)
- Implement Drag and Drop action for PageElements. [\#201](https://github.com/dougnoel/sentinel/issues/201)
- Add a total wait time warning. [\#198](https://github.com/dougnoel/sentinel/issues/198)
- Implement webdrivermanager [\#193](https://github.com/dougnoel/sentinel/issues/193)
- Replace Codebeat with SonarCloud with Github Actions [\#192](https://github.com/dougnoel/sentinel/issues/192)
- Increase the speed of PageElement.isDisplayed [\#186](https://github.com/dougnoel/sentinel/issues/186)
- Increase the Speed of PageElement.isEnabled [\#185](https://github.com/dougnoel/sentinel/issues/185)
- Fix deployment to Maven Central and Update Documentation [\#178](https://github.com/dougnoel/sentinel/issues/178)
- Close Driver even when tests error out [\#158](https://github.com/dougnoel/sentinel/issues/158)
- Allow multiple element locators [\#83](https://github.com/dougnoel/sentinel/issues/83)
- Overload getElement to accept a selector type and selector string [\#80](https://github.com/dougnoel/sentinel/issues/80)
- Add Edge as a browser choice for local execution [\#77](https://github.com/dougnoel/sentinel/issues/77)
- Monitor Google Web Drivers and automatically release new updates [\#69](https://github.com/dougnoel/sentinel/issues/69)

**Fixed bugs:**

- Errors in CI/CD Pipeline [\#236](https://github.com/dougnoel/sentinel/issues/236)
- Default timeout settings doesn't work on elements [\#220](https://github.com/dougnoel/sentinel/issues/220)
- Dynamic wait doesn't work on elements [\#219](https://github.com/dougnoel/sentinel/issues/219)
- Cucumber Wait step is throwing a Null Pointer Exception [\#204](https://github.com/dougnoel/sentinel/issues/204)

**Closed issues:**

- Review PDFSteps [\#249](https://github.com/dougnoel/sentinel/issues/249)
- README is very outdated [\#221](https://github.com/dougnoel/sentinel/issues/221)
- Remove all configurations from the test runner [\#50](https://github.com/dougnoel/sentinel/issues/50)

**Merged pull requests:**

- Version 1.0.7 [\#252](https://github.com/dougnoel/sentinel/pull/252) ([dougnoel](https://github.com/dougnoel))
- Refactored PageElement to Element. Made toWebElement\(\) private so tha… [\#250](https://github.com/dougnoel/sentinel/pull/250) ([dougnoel](https://github.com/dougnoel))
- 243 cleanup page object locations [\#244](https://github.com/dougnoel/sentinel/pull/244) ([dougnoel](https://github.com/dougnoel))
- Bump pdfbox from 2.0.16 to 2.0.24 [\#242](https://github.com/dougnoel/sentinel/pull/242) ([dependabot[bot]](https://github.com/apps/dependabot))
- Updated version to 1.0.6, updated Javadocs. [\#241](https://github.com/dougnoel/sentinel/pull/241) ([dougnoel](https://github.com/dougnoel))
- Bump httpclient from 4.5.10 to 4.5.13 [\#238](https://github.com/dougnoel/sentinel/pull/238) ([dependabot[bot]](https://github.com/apps/dependabot))
- Updated jacoco version to latest. [\#237](https://github.com/dougnoel/sentinel/pull/237) ([dougnoel](https://github.com/dougnoel))
- 197 add an on hover test step1 [\#235](https://github.com/dougnoel/sentinel/pull/235) ([dougnoel](https://github.com/dougnoel))
- Completed \# 3049 - When the "elements:" line is missing from the yaml file we should throw a ConfigurationParseException with a message [\#233](https://github.com/dougnoel/sentinel/pull/233) ([paglajewel](https://github.com/paglajewel))
- added instructions to navigate from scenario to step def [\#229](https://github.com/dougnoel/sentinel/pull/229) ([mdsapon](https://github.com/mdsapon))
- Fixed new window/tab issue and added unit tests [\#228](https://github.com/dougnoel/sentinel/pull/228) ([paglajewel](https://github.com/paglajewel))
- Updated README.md, CHANGELOG.md and Javadocs. [\#226](https://github.com/dougnoel/sentinel/pull/226) ([dougnoel](https://github.com/dougnoel))
- Added Drag and Drop code to PageElement object. [\#218](https://github.com/dougnoel/sentinel/pull/218) ([dougnoel](https://github.com/dougnoel))
- Added Checkbox unit tests and modified language in cucumber steps. [\#217](https://github.com/dougnoel/sentinel/pull/217) ([dougnoel](https://github.com/dougnoel))
- 193 implement webdrivermanager [\#215](https://github.com/dougnoel/sentinel/pull/215) ([dougnoel](https://github.com/dougnoel))
- Added script to support selenium grid [\#212](https://github.com/dougnoel/sentinel/pull/212) ([mdsapon](https://github.com/mdsapon))
- 204 cucumber wait [\#208](https://github.com/dougnoel/sentinel/pull/208) ([dougnoel](https://github.com/dougnoel))
- Removing deprecated code. [\#200](https://github.com/dougnoel/sentinel/pull/200) ([dougnoel](https://github.com/dougnoel))
- Removed all API functionality to redo it. Fixed PrimeNGDropdown selec… [\#199](https://github.com/dougnoel/sentinel/pull/199) ([dougnoel](https://github.com/dougnoel))
- Sonarcloud [\#194](https://github.com/dougnoel/sentinel/pull/194) ([dougnoel](https://github.com/dougnoel))
- Create maven.yml [\#191](https://github.com/dougnoel/sentinel/pull/191) ([dougnoel](https://github.com/dougnoel))
- Javadoc update [\#190](https://github.com/dougnoel/sentinel/pull/190) ([dougnoel](https://github.com/dougnoel))
- Removed deprecated Extent Report config file. [\#188](https://github.com/dougnoel/sentinel/pull/188) ([dougnoel](https://github.com/dougnoel))
- 1.0.5 doc update [\#187](https://github.com/dougnoel/sentinel/pull/187) ([dougnoel](https://github.com/dougnoel))

## [1.0.5](https://github.com/dougnoel/sentinel/tree/1.0.5) (2020-08-06)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/sentinel-1.0.3...1.0.5)

**Implemented enhancements:**

- Move Maximize Window from Page object to Page Manager [\#157](https://github.com/dougnoel/sentinel/issues/157)
- Turn waituntil loop into a lambda expression [\#146](https://github.com/dougnoel/sentinel/issues/146)
- Deprecate Implicit Waits [\#145](https://github.com/dougnoel/sentinel/issues/145)
- Refactor Table.java [\#121](https://github.com/dougnoel/sentinel/issues/121)
- Deprecate wait\_for\_load\(\) Cucumber step [\#97](https://github.com/dougnoel/sentinel/issues/97)
- Use cucumber.io version 5.x [\#93](https://github.com/dougnoel/sentinel/issues/93)
- Add the ability to press keys using a cucumber step [\#87](https://github.com/dougnoel/sentinel/issues/87)
- Abilty to use Material-UI Select [\#84](https://github.com/dougnoel/sentinel/issues/84)
- Allow a custom path for drivers [\#82](https://github.com/dougnoel/sentinel/issues/82)
- Allow choosing to leave browser open on command line/config file [\#79](https://github.com/dougnoel/sentinel/issues/79)
- Deprecate "base" in page object yamls and replace with "Default" [\#72](https://github.com/dougnoel/sentinel/issues/72)
- Ensure the timeout value is being used for element discovery [\#71](https://github.com/dougnoel/sentinel/issues/71)
- Add warn logs when default values are used. [\#70](https://github.com/dougnoel/sentinel/issues/70)
- Update all webdrivers [\#67](https://github.com/dougnoel/sentinel/issues/67)
- Rename log file to sentinel.log [\#64](https://github.com/dougnoel/sentinel/issues/64)
- Clean up Cucumber Steps that require nulls [\#54](https://github.com/dougnoel/sentinel/issues/54)
- Combine click scripts [\#31](https://github.com/dougnoel/sentinel/issues/31)
- Put Sentinel on Maven Central [\#30](https://github.com/dougnoel/sentinel/issues/30)
- Write Unit tests [\#29](https://github.com/dougnoel/sentinel/issues/29)
- Auto-detect local operating system [\#25](https://github.com/dougnoel/sentinel/issues/25)
- Turn Page Objects into YAML files [\#23](https://github.com/dougnoel/sentinel/issues/23)
- Ability to use NGPrime Dropdowns [\#22](https://github.com/dougnoel/sentinel/issues/22)

**Fixed bugs:**

- Saucelabs Driver not working [\#165](https://github.com/dougnoel/sentinel/issues/165)
- I wait for x.x seconds is always waiting at least a second [\#135](https://github.com/dougnoel/sentinel/issues/135)
- Need to add CLASS enumeration import to Page object [\#101](https://github.com/dougnoel/sentinel/issues/101)
- Remove protocol variable from URL navigation [\#96](https://github.com/dougnoel/sentinel/issues/96)
- Remove sections from Saucelabs Job Name if values are Null [\#76](https://github.com/dougnoel/sentinel/issues/76)
- pageObjectPackages configuration option is spelled incorrectly in error message [\#75](https://github.com/dougnoel/sentinel/issues/75)
- Default users cannot be found in page object yml [\#74](https://github.com/dougnoel/sentinel/issues/74)
- Table column sort is incorrectly sorting numbers in strings [\#73](https://github.com/dougnoel/sentinel/issues/73)
- Optional config value errors are showing up in the console [\#61](https://github.com/dougnoel/sentinel/issues/61)
- Sometimes a wait of 0.002 seconds will make the next click go faster [\#60](https://github.com/dougnoel/sentinel/issues/60)

**Closed issues:**

- Create a Changelog [\#166](https://github.com/dougnoel/sentinel/issues/166)
- Add stripTrailing\(\) back in [\#144](https://github.com/dougnoel/sentinel/issues/144)
- Replace .newInstance\(\) with .getDeclaredConstructor\(\).newInstance\(\) [\#138](https://github.com/dougnoel/sentinel/issues/138)
- Refactor getElement\(\) [\#122](https://github.com/dougnoel/sentinel/issues/122)
- Cleanup WebDriverManager.java [\#113](https://github.com/dougnoel/sentinel/issues/113)
- Reduce Step Definition complexity [\#110](https://github.com/dougnoel/sentinel/issues/110)
- Page.java cleanup [\#108](https://github.com/dougnoel/sentinel/issues/108)
- Ability to use PrimeNG Radio Buttons [\#103](https://github.com/dougnoel/sentinel/issues/103)
- Add badges for License and Code Quality [\#99](https://github.com/dougnoel/sentinel/issues/99)
- Update documentation for pushing a SNAPSHOT Version [\#91](https://github.com/dougnoel/sentinel/issues/91)
- i\_am\_redirected\_to\_the\_page should validate page load [\#78](https://github.com/dougnoel/sentinel/issues/78)
- Document how to: updating sentinel drivers [\#68](https://github.com/dougnoel/sentinel/issues/68)
- Get rid of Traprange references in the documentation. [\#63](https://github.com/dougnoel/sentinel/issues/63)

**Merged pull requests:**

- Update to pom.xml to try and get Javadocs working on some versions of… [\#172](https://github.com/dougnoel/sentinel/pull/172) ([dougnoel](https://github.com/dougnoel))
- Removed failing test for dropdown text verification for NGPrime. Chan… [\#171](https://github.com/dougnoel/sentinel/pull/171) ([dougnoel](https://github.com/dougnoel))
- Update POM version to 1.0.5. [\#170](https://github.com/dougnoel/sentinel/pull/170) ([dougnoel](https://github.com/dougnoel))
- 25 auto detect os [\#168](https://github.com/dougnoel/sentinel/pull/168) ([dougnoel](https://github.com/dougnoel))
- Auto Generated Changelog. [\#167](https://github.com/dougnoel/sentinel/pull/167) ([dougnoel](https://github.com/dougnoel))
- Removed Codacy badge because it wasn't working. [\#163](https://github.com/dougnoel/sentinel/pull/163) ([dougnoel](https://github.com/dougnoel))
- Removed orphaned code that was searching the filesystem to no benefit… [\#162](https://github.com/dougnoel/sentinel/pull/162) ([dougnoel](https://github.com/dougnoel))
- 23 turn page objects into yaml files [\#161](https://github.com/dougnoel/sentinel/pull/161) ([dougnoel](https://github.com/dougnoel))
- Modify pom.xml to use Java 11 oficially. [\#155](https://github.com/dougnoel/sentinel/pull/155) ([dougnoel](https://github.com/dougnoel))
- 93 update cucumber [\#149](https://github.com/dougnoel/sentinel/pull/149) ([dougnoel](https://github.com/dougnoel))
- 82 custom driver path [\#148](https://github.com/dougnoel/sentinel/pull/148) ([dougnoel](https://github.com/dougnoel))
- Added Lambda. [\#147](https://github.com/dougnoel/sentinel/pull/147) ([dougnoel](https://github.com/dougnoel))
- 87 press keys [\#140](https://github.com/dougnoel/sentinel/pull/140) ([dougnoel](https://github.com/dougnoel))
- Fix linting issues. [\#137](https://github.com/dougnoel/sentinel/pull/137) ([dougnoel](https://github.com/dougnoel))
- Fixed \#60 where waits were necessary after page loads and some drop d… [\#136](https://github.com/dougnoel/sentinel/pull/136) ([dougnoel](https://github.com/dougnoel))
- 72 deprecate base [\#134](https://github.com/dougnoel/sentinel/pull/134) ([dougnoel](https://github.com/dougnoel))
- Fixed \#72. Supporting both default and base for now. Added unit tests. [\#132](https://github.com/dougnoel/sentinel/pull/132) ([dougnoel](https://github.com/dougnoel))
- Renamed StringUtils and updated all the references to it. Fixed a few… [\#131](https://github.com/dougnoel/sentinel/pull/131) ([dougnoel](https://github.com/dougnoel))
- 74 default users attempt 2 [\#130](https://github.com/dougnoel/sentinel/pull/130) ([dougnoel](https://github.com/dougnoel))
- Added in the ability to leave the browser open using the argument -Dl… [\#127](https://github.com/dougnoel/sentinel/pull/127) ([dougnoel](https://github.com/dougnoel))
- Added json output for Cucumber for Jenkins. [\#126](https://github.com/dougnoel/sentinel/pull/126) ([dougnoel](https://github.com/dougnoel))
- 121 refactor table [\#124](https://github.com/dougnoel/sentinel/pull/124) ([dougnoel](https://github.com/dougnoel))
- Made small naming changes. Holding off on refactoring this. Closing \#… [\#123](https://github.com/dougnoel/sentinel/pull/123) ([dougnoel](https://github.com/dougnoel))
- 73 table column sort [\#120](https://github.com/dougnoel/sentinel/pull/120) ([dougnoel](https://github.com/dougnoel))
- Setup for fixing sort. Need to add more columns to the test table and… [\#115](https://github.com/dougnoel/sentinel/pull/115) ([dougnoel](https://github.com/dougnoel))
- 76 nulls in saucelabs jobs [\#114](https://github.com/dougnoel/sentinel/pull/114) ([dougnoel](https://github.com/dougnoel))
- Fixed \#101. [\#112](https://github.com/dougnoel/sentinel/pull/112) ([dougnoel](https://github.com/dougnoel))
- 110 reduce step def complexity [\#111](https://github.com/dougnoel/sentinel/pull/111) ([dougnoel](https://github.com/dougnoel))
- Fixed \#108 linting errors. [\#109](https://github.com/dougnoel/sentinel/pull/109) ([dougnoel](https://github.com/dougnoel))
- 103 prime ng radio button [\#107](https://github.com/dougnoel/sentinel/pull/107) ([dougnoel](https://github.com/dougnoel))
- Adding Radio button dummy page for testing. [\#106](https://github.com/dougnoel/sentinel/pull/106) ([dougnoel](https://github.com/dougnoel))
- Added Maven central Badge [\#105](https://github.com/dougnoel/sentinel/pull/105) ([dougnoel](https://github.com/dougnoel))
- Adding badges. [\#100](https://github.com/dougnoel/sentinel/pull/100) ([dougnoel](https://github.com/dougnoel))
- Fixed \#54, \#96, \#97. Minor variable name cleanup. [\#98](https://github.com/dougnoel/sentinel/pull/98) ([dougnoel](https://github.com/dougnoel))
- Fixed \#75. Corrected the error message. [\#95](https://github.com/dougnoel/sentinel/pull/95) ([dougnoel](https://github.com/dougnoel))
- Fixed \#60 by adding a 2 millisecond wait before searching for element… [\#94](https://github.com/dougnoel/sentinel/pull/94) ([dougnoel](https://github.com/dougnoel))
- Fixed \#91. [\#92](https://github.com/dougnoel/sentinel/pull/92) ([dougnoel](https://github.com/dougnoel))
- 1.0.4 [\#86](https://github.com/dougnoel/sentinel/pull/86) ([dougnoel](https://github.com/dougnoel))

## [sentinel-1.0.3](https://github.com/dougnoel/sentinel/tree/sentinel-1.0.3) (2020-02-28)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/1.0.0...sentinel-1.0.3)

**Implemented enhancements:**

- Allow clicking an element in an ngx-datatable [\#44](https://github.com/dougnoel/sentinel/issues/44)
- Create config values for name, tags and build for Saucelabs execution [\#34](https://github.com/dougnoel/sentinel/issues/34)
- Clean up Exceptions for reading configuration files. [\#33](https://github.com/dougnoel/sentinel/issues/33)
- Make Traprange an internal library or remove it [\#28](https://github.com/dougnoel/sentinel/issues/28)
- Remove the requirement for a configuration file [\#27](https://github.com/dougnoel/sentinel/issues/27)
- Ability to identify a table made from Divs [\#21](https://github.com/dougnoel/sentinel/issues/21)
- Need a better error message for a missing page object package list [\#18](https://github.com/dougnoel/sentinel/issues/18)
- Need a human readable error for drivers not executable. [\#17](https://github.com/dougnoel/sentinel/issues/17)

**Fixed bugs:**

- Cucumber Extent Reports outputs a bunch of useless info when DEBUG log level is set [\#38](https://github.com/dougnoel/sentinel/issues/38)
- Getting an optional config value should not throw an error if the config file doesn't exist. [\#36](https://github.com/dougnoel/sentinel/issues/36)
- Downgrade logging level [\#26](https://github.com/dougnoel/sentinel/issues/26)
- pom.xml does not include cucumber-junit as compile [\#15](https://github.com/dougnoel/sentinel/issues/15)
- Some Cucumber steps are not static methods [\#14](https://github.com/dougnoel/sentinel/issues/14)

**Closed issues:**

- Update Readme to include how to publish the javadocs to github [\#32](https://github.com/dougnoel/sentinel/issues/32)

**Merged pull requests:**

- 30 put sentinel on maven [\#62](https://github.com/dougnoel/sentinel/pull/62) ([dougnoel](https://github.com/dougnoel))
- Updating for release. [\#59](https://github.com/dougnoel/sentinel/pull/59) ([dougnoel](https://github.com/dougnoel))
- Updating Javadocs in docs folder. [\#58](https://github.com/dougnoel/sentinel/pull/58) ([dougnoel](https://github.com/dougnoel))
- Updated Documentation. DRYed up some Table code. [\#57](https://github.com/dougnoel/sentinel/pull/57) ([dougnoel](https://github.com/dougnoel))
- Ngx table [\#53](https://github.com/dougnoel/sentinel/pull/53) ([dougnoel](https://github.com/dougnoel))
- Ngx table [\#52](https://github.com/dougnoel/sentinel/pull/52) ([dougnoel](https://github.com/dougnoel))
- Ngx table [\#46](https://github.com/dougnoel/sentinel/pull/46) ([dougnoel](https://github.com/dougnoel))
- Ngx table [\#43](https://github.com/dougnoel/sentinel/pull/43) ([dougnoel](https://github.com/dougnoel))
- Updating Table html file for testing. [\#42](https://github.com/dougnoel/sentinel/pull/42) ([dougnoel](https://github.com/dougnoel))
- Release 1 0 3 [\#41](https://github.com/dougnoel/sentinel/pull/41) ([dougnoel](https://github.com/dougnoel))
- Ngx table [\#40](https://github.com/dougnoel/sentinel/pull/40) ([dougnoel](https://github.com/dougnoel))
- Fixed \#36 so that missing optional config options no longer log as er… [\#39](https://github.com/dougnoel/sentinel/pull/39) ([dougnoel](https://github.com/dougnoel))
- Release 1 0 3 [\#37](https://github.com/dougnoel/sentinel/pull/37) ([dougnoel](https://github.com/dougnoel))
- Fixed \#18. Also fixed a bug found that was preventing values from bei… [\#20](https://github.com/dougnoel/sentinel/pull/20) ([dougnoel](https://github.com/dougnoel))
- Steps are not static [\#16](https://github.com/dougnoel/sentinel/pull/16) ([dougnoel](https://github.com/dougnoel))
- Needed to increment the version number. [\#13](https://github.com/dougnoel/sentinel/pull/13) ([dougnoel](https://github.com/dougnoel))
- Added maven-assembly-plugin to add jar-with-dependencies to deal with… [\#12](https://github.com/dougnoel/sentinel/pull/12) ([dougnoel](https://github.com/dougnoel))

## [1.0.0](https://github.com/dougnoel/sentinel/tree/1.0.0) (2020-01-18)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/2c5764349fe1dc782deb6fc98e2290725518e60b...1.0.0)

**Implemented enhancements:**

- Configuration file should allow default values [\#8](https://github.com/dougnoel/sentinel/issues/8)
- Add Tunneling capability for Saucelabs [\#7](https://github.com/dougnoel/sentinel/issues/7)
- Needing to use minimal waits before a lot of clicks [\#4](https://github.com/dougnoel/sentinel/issues/4)
- Renaming packages to comply with Maven standards [\#3](https://github.com/dougnoel/sentinel/issues/3)
- Add \<dependencyManagement\> section and use boms for versioning [\#1](https://github.com/dougnoel/sentinel/issues/1)

**Merged pull requests:**

- Packaging and release [\#11](https://github.com/dougnoel/sentinel/pull/11) ([dougnoel](https://github.com/dougnoel))
- Updates to Readme and adding Javadocs for Github reading. [\#10](https://github.com/dougnoel/sentinel/pull/10) ([dougnoel](https://github.com/dougnoel))
- Fixes \#7 Saucelabs Tunneling and \#8 No Default Config file. [\#9](https://github.com/dougnoel/sentinel/pull/9) ([dougnoel](https://github.com/dougnoel))
- Fixing issue \#4. Fixing minimal waits and config file. Try 2 [\#6](https://github.com/dougnoel/sentinel/pull/6) ([dougnoel](https://github.com/dougnoel))
- GH-1:  Clean up maven \(test fails\) [\#2](https://github.com/dougnoel/sentinel/pull/2) ([jmax01](https://github.com/jmax01))



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
