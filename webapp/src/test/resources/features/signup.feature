Feature: Sign-up
  Scenario: The user tries to sign up with an incorrect e-mail
    Given A user with email "qwert.com", name "Prueba" and password "passwd"
    When They fill the data in the sign-up form and click Sing up
    Then The message "The email is not valid" is shown

  Scenario: The user successfully sings up
    Given A user with email "signupTest@correcto.com", name "Prueba" and password "passwd"
    When They fill the data in the sign-up form and click Sing up
    Then The login page appears

  Scenario: The user tries to sing up with an e-mail already in use
    Given A user with email "signupTest@correcto.com", name "Prueba" and password "passwd"
    When They fill the data in the sign-up form and click Sing up
    Then The message "The account could not be created, an account with that email might already exist" is shown