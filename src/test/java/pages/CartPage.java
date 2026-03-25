package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * CartPage - Page Object for the shopping cart (/cart.html)
 */
public class CartPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By cartItems        = By.className("cart_item");
    private final By itemNames        = By.className("inventory_item_name");
    private final By checkoutButton   = By.id("checkout");
    private final By continueShopBtn  = By.id("continue-shopping");
    private final By cartTitle        = By.className("title");

    // ── State checks ──────────────────────────────────────────────────────────

    public boolean isOnCartPage() {
        return isDisplayed(cartTitle) && getText(cartTitle).equalsIgnoreCase("Your Cart");
    }

    public int getCartItemCount() {
        return countElements(cartItems);
    }

    public boolean isItemInCart(String itemName) {
        List<WebElement> items = findAll(itemNames);
        return items.stream()
                    .anyMatch(e -> e.getText().trim().equalsIgnoreCase(itemName));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void clickCheckout() {
        click(checkoutButton);
    }

    public void continueShopping() {
        click(continueShopBtn);
    }

    public void removeItem(String itemDataTestId) {
        By removeBtn = By.cssSelector("[data-test='remove-" + itemDataTestId + "']");
        click(removeBtn);
    }
}
