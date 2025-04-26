Feature: Scores
  Scenario: The user checks its scores
    Given A registered user
    When They check their scores
    Then An empty list is shown along with a message