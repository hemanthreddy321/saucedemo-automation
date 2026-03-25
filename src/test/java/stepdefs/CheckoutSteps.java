package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;

/**
 * CheckoutSteps - step definitions for Checkout.feature
 */
public class CheckoutSteps {

    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage      cartPage      = new CartPage();
    private final CheckoutPage  checkoutPage  = new CheckoutPage();

    @Given("the user has added {string} to the cart")
    public void addItemToCart(String itemId) {
        inventoryPage.addItemToCart(itemId);
    }

    @When("the user navigates to the cart page")
    public void goToCart() {
        inventoryPage.goToCart();
    }

    @And("the user clicks the checkout button")
    public void clickCheckout() {
        cartPage.clickCheckout();
    }

    @And("the user enters first name {string} last name {string} and postal code {string}")
    public void fillShippingInfo(String firstName, String lastName, String postalCode) {
        checkoutPage.fillShippingInfo(firstName, lastName, postalCode);
    }

    @And("the user clicks the continue button")
    public void clickContinue() {
        checkoutPage.clickContinue();
    }

    @And("the user clicks the finish button")
    public void clickFinish() {
        checkoutPage.clickFinish();
    }

    @And("the user cancels the checkout")
    public void cancelCheckout() {
        checkoutPage.clickCancel();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the order confirmation message should be {string}")
    public void verifyConfirmationMessage(String expected) {
        Assert.assertEquals(checkoutPage.getConfirmationHeader(), expected,
            "Order confirmation header did not match");
    }

    @Then("the checkout overview page should be displayed")
    public void verifyOverviewPage() {
        Assert.assertTrue(checkoutPage.isOnOverviewPage(),
            "Expected checkout overview page");
    }

    @Then("the order total should be visible")
    public void verifyOrderTotalVisible() {
        Assert.assertTrue(checkoutPage.isTotalVisible(),
            "Order total label was not visible on overview");
    }

    @Then("the checkout overview should contain {string}")
    public void verifyOverviewContainsItem(String itemName) {
        Assert.assertTrue(checkoutPage.overviewContainsItem(itemName),
            "Item '" + itemName + "' not found in checkout overview");
    }

    @Then("a checkout validation error should appear")
    public void verifyCheckoutError() {
        Assert.assertTrue(checkoutPage.isStepOneErrorShown(),
            "Expected checkout validation error to be shown");
    }

    @Then("the user should be back on the cart page")
    public void verifyBackOnCartPage() {
        Assert.assertTrue(cartPage.isOnCartPage(),
            "Expected cart page after cancel but URL was: " + cartPage.getCurrentUrl());
    }
}
