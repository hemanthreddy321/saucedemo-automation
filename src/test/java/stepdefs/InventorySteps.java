package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.InventoryPage;
import pages.LoginPage;
import pages.ProductDetailPage;
import utils.ConfigReader;

/**
 * InventorySteps - step definitions for Inventory.feature
 */
public class InventorySteps {

    private final LoginPage         loginPage         = new LoginPage();
    private final InventoryPage     inventoryPage     = new InventoryPage();
    private final ProductDetailPage productDetailPage = new ProductDetailPage();

    @Given("the user is logged in as {string}")
    public void loginAsUser(String username) {
        loginPage.navigateTo(ConfigReader.getBaseUrl());
        loginPage.login(username, ConfigReader.get("valid.password"));
        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
            "Login failed for user: " + username);
    }

    @Then("the inventory page should display {int} products")
    public void verifyProductCount(int expectedCount) {
        Assert.assertEquals(inventoryPage.getProductCount(), expectedCount,
            "Product count on inventory page did not match");
    }

    @When("the user adds {string} to the cart")
    public void addItemToCart(String itemId) {
        inventoryPage.addItemToCart(itemId);
    }

    @And("the user removes {string} from the cart")
    public void removeItemFromCart(String itemId) {
        inventoryPage.removeItemFromCart(itemId);
    }

    @Then("the cart badge should show {string}")
    public void verifyCartBadge(String expected) {
        Assert.assertEquals(
            String.valueOf(inventoryPage.getCartBadgeCount()),
            expected,
            "Cart badge count did not match"
        );
    }

    @Then("the cart should be empty")
    public void verifyCartEmpty() {
        Assert.assertTrue(inventoryPage.isCartEmpty(),
            "Expected cart to be empty but badge was visible");
    }

    @When("the user sorts products by {string}")
    public void sortProducts(String sortOption) {
        inventoryPage.sortBy(sortOption);
    }

    @Then("the first product should be {string}")
    public void verifyFirstProduct(String expectedName) {
        Assert.assertEquals(inventoryPage.getFirstProductName(), expectedName,
            "First product name after sort did not match");
    }

    @When("the user clicks on product {string}")
    public void clickProduct(String productName) {
        inventoryPage.clickProductByName(productName);
    }

    @Then("the product detail page should be displayed")
    public void verifyProductDetailPage() {
        Assert.assertTrue(productDetailPage.isOnProductDetailPage(),
            "Product detail page was not displayed");
    }

    @Then("the product name on detail page should be {string}")
    public void verifyProductDetailName(String expectedName) {
        Assert.assertEquals(productDetailPage.getProductName(), expectedName,
            "Product name on detail page did not match");
    }
}
