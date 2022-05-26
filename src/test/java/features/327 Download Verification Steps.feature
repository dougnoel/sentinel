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