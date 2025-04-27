Feature: Login
  Scenario: The user tries to login with correct credentials
    Given A user with email "loginTest@correcto.com", name "Test" and password "passwd"
    When They register and fill the data in the login form
    Then They are redirected to the home page

  Scenario: The user tries to login with a wrong e-mail
    Given A user with email "wrong" and password "passwd"
    When They fill the data in the login form
    Then The message "Invalid username or password." is shown

  Scenario: The user tries to login with a wrong password
    Given A user with email "loginTest@correcto.com" and password "wrong"
    When They fill the data in the login form
    Then The message "Invalid username or password." is shown

