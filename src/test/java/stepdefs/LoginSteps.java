package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * LoginSteps - step definitions for Login.feature
 */
public class LoginSteps {

    private final LoginPage     loginPage     = new LoginPage();
    private final InventoryPage inventoryPage = new InventoryPage();

    @Given("the user is on the SauceDemo login page")
    public void openLoginPage() {
        loginPage.navigateTo(ConfigReader.getBaseUrl());
    }

    @When("the user logs in with username {string} and password {string}")
    public void loginWithCredentials(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the user should be on the inventory page")
    public void verifyInventoryPage() {
        Assert.assertTrue(
            inventoryPage.isOnInventoryPage(),
            "Expected Products page after login but URL was: " + inventoryPage.getCurrentUrl()
        );
    }

    @Then("an error message {string} should be shown")
    public void verifyErrorMessage(String expectedMsg) {
        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Login error container was not visible");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedMsg,
            "Error message text did not match");
    }
}
