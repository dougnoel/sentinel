#language: en
@89
Feature: 89 API Testing
  Tests using the Swagger Pet Store example API.
  https://petstore3.swagger.io/

  @89A
  Scenario: 89A Swagger Test
    Given I use the API named Pet Store API
    When I send 1 to the pet endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "photoUrls"