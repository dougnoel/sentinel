#author: sampacos

@327
Feature: Download Verification Steps
  Unit tests for the DownloadVerificationSteps

  Background:
    Given I navigate to the Radio Button Page
      And I clear all files from the downloads folder

  @327A
  Scenario: Download via file extension verification
    When I click the sample download link
    Then I verify a new file is downloaded with the extension pdf

  @327B
  Scenario: Download via filename verification
    When I click the sample download link
    Then I verify a new file is downloaded with the name TestPDF.pdf

  @386 @upload
  Scenario: Download via file extension verification and then re-upload the same file
    When I click the sample download link
    Then I verify a new file is downloaded with the name TestPDF.pdf
    When I navigate to the Upload Page
      And I re-upload the most recently downloaded file to the choose file button
    Then I verify the file counter with the attribute data-filecount has the value 1
      And I verify the upload result 1 with the attribute data-filename has the value TestPDF.pdf