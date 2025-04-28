Feature: Game
  Scenario: The user starts a custom with 1 question
    Given A registered user
    When They start a custom game with 1 question and the first category
    Then The game starts

  Scenario: The user plays a custom game with 1 question
    Given A registered user
    When They start a custom game with 1 question and the first category, and they select an answer
    Then The game results page appears

  Scenario: The user select Play Again after ending the game
    Given A registered user
    When They finish a game and click on Play Again
    Then The game starts

  Scenario: The user select Check your scores after ending the game
    Given A registered user
    When They finish a game and click on Check your scores
    Then The score list is shown