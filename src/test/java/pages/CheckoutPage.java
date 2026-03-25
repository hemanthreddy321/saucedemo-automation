package pages;

import org.openqa.selenium.By;

/**
 * CheckoutPage - covers both checkout step one (/checkout-step-one.html)
 * and step two / overview (/checkout-step-two.html) and confirmation.
 */
public class CheckoutPage extends BasePage {

    // ── Step One locators ─────────────────────────────────────────────────────
    private final By firstNameField   = By.id("first-name");
    private final By lastNameField    = By.id("last-name");
    private final By postalCodeField  = By.id("postal-code");
    private final By continueButton   = By.id("continue");
    private final By cancelButton     = By.id("cancel");
    private final By stepOneError     = By.cssSelector("[data-test='error']");

    // ── Step Two / Overview locators ──────────────────────────────────────────
    private final By finishButton     = By.id("finish");
    private final By overviewTitle    = By.className("title");
    private final By summaryTotal     = By.className("summary_total_label");
    private final By cartItems        = By.className("cart_item");
    private final By itemNames        = By.className("inventory_item_name");

    // ── Confirmation locators ─────────────────────────────────────────────────
    private final By confirmHeader    = By.className("complete-header");
    private final By confirmText      = By.className("complete-text");
    private final By backHomeButton   = By.id("back-to-products");

    // ── Step One actions ──────────────────────────────────────────────────────

    public void enterFirstName(String name) {
        type(firstNameField, name);
    }

    public void enterLastName(String name) {
        type(lastNameField, name);
    }

    public void enterPostalCode(String code) {
        type(postalCodeField, code);
    }

    public void fillShippingInfo(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    public boolean isStepOneErrorShown() {
        return isDisplayed(stepOneError);
    }

    public String getStepOneErrorText() {
        return getText(stepOneError);
    }

    // ── Step Two / Overview actions ───────────────────────────────────────────

    public boolean isOnOverviewPage() {
        return isDisplayed(overviewTitle)
               && getText(overviewTitle).equalsIgnoreCase("Checkout: Overview");
    }

    public String getOrderTotal() {
        return getText(summaryTotal);
    }

    public boolean isTotalVisible() {
        return isDisplayed(summaryTotal);
    }

    public boolean overviewContainsItem(String itemName) {
        return findAll(itemNames).stream()
                                 .anyMatch(e -> e.getText().trim().equalsIgnoreCase(itemName));
    }

    public void clickFinish() {
        click(finishButton);
    }

    // ── Confirmation actions ──────────────────────────────────────────────────

    public String getConfirmationHeader() {
        return getText(confirmHeader);
    }

    public String getConfirmationText() {
        return getText(confirmText);
    }

    public void clickBackToProducts() {
        click(backHomeButton);
    }
}
