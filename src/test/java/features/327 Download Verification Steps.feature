#author: sampacos, tylerbouchard

@327 @551
Feature: Download Verification Steps
  Unit tests for the DownloadVerificationSteps

  Background:
    Given I navigate to the Downloads Test Page
      And I clear all files from the downloads folder

  @327A
  Scenario: Download via file extension verification
    Then I verify that by clicking the sample download link a new file is downloaded with the extension pdf

  @327B
  Scenario: Download via filename verification
    Then I verify that by clicking the sample download link a new file is downloaded with the name TestPDF.pdf
      And I verify the most recently downloaded file contains the text "%PDF-1.5"

  @386 @upload
  Scenario: Download via file name verification and then re-upload the same file
    Then I verify that by clicking the sample download link a new file is downloaded with the name TestPDF.pdf
    When I navigate to the Upload Page
      And I re-upload the most recently downloaded file to the choose file button
    Then I verify the file counter with the attribute data-filecount has the value 1
      And I verify the upload result 1 with the attribute data-filename has the value TestPDF.pdf

  @327C
  Scenario: Download zip and verify it contains a PDF
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
      And I verify the most recently downloaded zip file contains a file with the extension pdf
      And I verify the most recently downloaded zip file does not contain a file with the extension png

  @327D
  Scenario: Download dynamic name txt file and verify name contains current date time
    Then I verify that by clicking the text file download link a new file is downloaded with the extension txt
      And I verify the filename contains today's date
      And I verify the filename contains today's date formatted as _HH_

  @551A
  Scenario: Download zip and verify it contains 1 pdf
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
      And I verify the most recently downloaded zip file contains 1 file with the extension pdf

  @551B
  Scenario: Download zip and verify it contains no files with png
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
      And I verify the most recently downloaded zip file contains 0 files with the extension png
      And I verify the most recently downloaded zip file does not contains 1 file with the extension png
      And I verify the most recently downloaded zip file does not contain any files with the extension png

  @551C
  Scenario: Download zip and verify it does not contain 5 pdfs, contains 1 pdf, and contains 1 html
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
    And I verify the most recently downloaded zip file does not contain 5 files with the extension pdf
    And I verify the most recently downloaded zip file contains 1 file with the extension pdf
    And I verify the most recently downloaded zip file contains 1 file with the extension html

  @551D
  Scenario: Download zip and verify it contains 2 files of any extension
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
      And I verify the most recently downloaded zip file contains 2 files
      And I verify the most recently downloaded zip file contains 02 files
      And I verify the most recently downloaded zip file does not contain 3 files
      And I verify the most recently downloaded zip file does not contain 0 files
      And I verify the most recently downloaded zip file does not contain 00 files
      And I verify the most recently downloaded zip file does not contain 1 file

  @551E
  Scenario: Download zip and verify it contains any files
    Then I verify that by clicking the zip download link a new file is downloaded with the extension zip
      And I verify the most recently downloaded zip file contains any files