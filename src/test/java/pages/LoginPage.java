package pages;

import org.openqa.selenium.By;

/**
 * LoginPage - Page Object for https://www.saucedemo.com (login screen)
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By usernameField  = By.id("user-name");
    private final By passwordField  = By.id("password");
    private final By loginButton    = By.id("login-button");
    private final By errorContainer = By.cssSelector("[data-test='error']");
    private final By errorButton    = By.className("error-button");

    // ── Actions ───────────────────────────────────────────────────────────────

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    /** Full login in one call */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void dismissError() {
        if (isDisplayed(errorButton)) click(errorButton);
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    public boolean isErrorDisplayed() {
        return isDisplayed(errorContainer);
    }

    public String getErrorMessage() {
        return getText(errorContainer);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginButton);
    }
}
