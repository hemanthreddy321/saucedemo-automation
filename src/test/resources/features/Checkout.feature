@Regression @Checkout
Feature: End-to-end checkout flow on SauceDemo

  Background:
    Given the user is logged in as "standard_user"

  Scenario: Successfully complete a purchase
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user enters first name "John" last name "Doe" and postal code "560001"
    And the user clicks the continue button
    Then the checkout overview page should be displayed
    And the order total should be visible
    When the user clicks the finish button
    Then the order confirmation message should be "Thank you for your order!"

  Scenario: Checkout overview shows correct item
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user enters first name "Jane" last name "Smith" and postal code "400001"
    And the user clicks the continue button
    Then the checkout overview should contain "Sauce Labs Backpack"

  Scenario: Checkout fails when first name is missing
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user clicks the continue button
    Then a checkout validation error should appear

  Scenario: Checkout fails when postal code is missing
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user enters first name "John" last name "Doe" and postal code ""
    And the user clicks the continue button
    Then a checkout validation error should appear

  Scenario: User can cancel checkout and return to cart
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user cancels the checkout
    Then the user should be back on the cart page

  @DataDriven
  Scenario Outline: Complete purchase with multiple user data sets
    Given the user has added "sauce-labs-backpack" to the cart
    When the user navigates to the cart page
    And the user clicks the checkout button
    And the user enters first name "<firstName>" last name "<lastName>" and postal code "<postal>"
    And the user clicks the continue button
    When the user clicks the finish button
    Then the order confirmation message should be "Thank you for your order!"
    Examples:
      | firstName | lastName | postal |
      | Hemanth   | Kumar    | 500001 |
      | Priya     | Sharma   | 600002 |
      | Ravi      | Verma    | 700003 |
