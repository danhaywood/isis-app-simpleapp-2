Feature: List and Create New Customers

  @DomainAppDemo
  Scenario: Existing customers can be listed and new ones created
    Given there are initially 10 customers
    When  I create a new customer
    Then  there are 11 customers

  Scenario: When there are no objects at all
    Given there are initially 0 customers
    When  I create a new customer
    And   I create another new customer
    Then  there are 2 customers

