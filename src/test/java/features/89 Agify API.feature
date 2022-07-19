#language: en
#Author: Edison Nica	
@89
Feature: 89 API Agify Tests
  Testing API code.

  @89A
  Scenario: 89A Basic API Test
    Given I use the Agify API
    When I send a request to the agify endpoint
    Then I verify a response was received
      And I validate the response contains the text "29"
