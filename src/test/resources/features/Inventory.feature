@Regression @Inventory
Feature: Product inventory on SauceDemo

  Background:
    Given the user is logged in as "standard_user"

  Scenario: Inventory page displays correct number of products
    Then the inventory page should display 6 products

  Scenario: Add a single product to the cart
    When the user adds "sauce-labs-backpack" to the cart
    Then the cart badge should show "1"

  Scenario: Add multiple products to the cart
    When the user adds "sauce-labs-backpack" to the cart
    And the user adds "sauce-labs-bike-light" to the cart
    And the user adds "sauce-labs-bolt-t-shirt" to the cart
    Then the cart badge should show "3"

  Scenario: Remove a product from the cart on inventory page
    When the user adds "sauce-labs-backpack" to the cart
    And the user removes "sauce-labs-backpack" from the cart
    Then the cart should be empty

  Scenario: Sort products by name A to Z
    When the user sorts products by "Name (A to Z)"
    Then the first product should be "Sauce Labs Backpack"

  Scenario: Sort products by name Z to A
    When the user sorts products by "Name (Z to A)"
    Then the first product should be "Test.allTheThings() T-Shirt (Red)"

  Scenario: Sort products by price low to high
    When the user sorts products by "Price (low to high)"
    Then the first product should be "Sauce Labs Onesie"

  Scenario: Sort products by price high to low
    When the user sorts products by "Price (high to low)"
    Then the first product should be "Sauce Labs Fleece Jacket"

  Scenario: Navigate to product detail page
    When the user clicks on product "Sauce Labs Backpack"
    Then the product detail page should be displayed
    And the product name on detail page should be "Sauce Labs Backpack"
