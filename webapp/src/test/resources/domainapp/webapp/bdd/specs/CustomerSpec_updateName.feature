Feature: Can modify name (with restrictions)

  @DomainAppDemo
  Scenario: Can modify name with most characters
    Given a customer
    When  I modify the name to 'abc'
    Then  the name is now 'abc'

  @DomainAppDemo
  Scenario: Cannot modify name if has invalid character
    Given a customer
    When  I attempt to modify the name to 'abc&'
    Then  the name is unchanged
     And  a warning is raised


