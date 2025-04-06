Feature: Sign-up
  Scenario: The user tries to sign up with an incorrect e-mail
    Given A user with email "qwert.com", name "Prueba" and password "passwd"
    When I fill the data in the sign-up form and click Sing up
    Then The message "The email is not valid" is shown