@Regression @Login
Feature: Login functionality on SauceDemo

  Background:
    Given the user is on the SauceDemo login page

  Scenario: Successful login with valid credentials
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the user should be on the inventory page

  Scenario: Login fails with locked out user
    When the user logs in with username "locked_out_user" and password "secret_sauce"
    Then an error message "Epic sadface: Sorry, this user has been locked out." should be shown

  Scenario: Login fails with wrong password
    When the user logs in with username "standard_user" and password "wrong_pass"
    Then an error message "Epic sadface: Username and password do not match any user in this service" should be shown

  Scenario: Login fails with blank credentials
    When the user logs in with username "" and password ""
    Then an error message "Epic sadface: Username is required" should be shown

  Scenario: Login fails with blank password
    When the user logs in with username "standard_user" and password ""
    Then an error message "Epic sadface: Password is required" should be shown

  Scenario Outline: Multiple valid users can log in
    When the user logs in with username "<username>" and password "secret_sauce"
    Then the user should be on the inventory page
    Examples:
      | username                |
      | standard_user           |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |
