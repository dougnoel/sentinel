# Changelog

## [Unreleased](https://github.com/dougnoel/sentinel/tree/HEAD)

[Full Changelog](https://github.com/dougnoel/sentinel/compare/sentinel-1.0.3...HEAD)

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
