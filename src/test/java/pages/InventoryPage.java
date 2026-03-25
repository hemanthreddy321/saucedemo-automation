package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * InventoryPage - Page Object for the products listing page (/inventory.html)
 */
public class InventoryPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By pageTitle        = By.className("title");
    private final By inventoryItems   = By.className("inventory_item");
    private final By cartBadge        = By.className("shopping_cart_badge");
    private final By cartIcon         = By.className("shopping_cart_link");
    private final By sortDropdown     = By.className("product_sort_container");
    private final By itemNames        = By.className("inventory_item_name");
    private final By burgerMenu       = By.id("react-burger-menu-btn");
    private final By logoutLink       = By.id("logout_sidebar_link");

    // ── State checks ──────────────────────────────────────────────────────────

    public boolean isOnInventoryPage() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Products");
    }

    public int getProductCount() {
        return countElements(inventoryItems);
    }

    // ── Cart ──────────────────────────────────────────────────────────────────

    public void addItemToCart(String itemDataTestId) {
        By addBtn = By.cssSelector("[data-test='add-to-cart-" + itemDataTestId + "']");
        click(addBtn);
    }

    public void removeItemFromCart(String itemDataTestId) {
        By removeBtn = By.cssSelector("[data-test='remove-" + itemDataTestId + "']");
        click(removeBtn);
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) return 0;
        return Integer.parseInt(getText(cartBadge));
    }

    public boolean isCartEmpty() {
        return !isDisplayed(cartBadge);
    }

    public void goToCart() {
        click(cartIcon);
    }

    // ── Sorting ───────────────────────────────────────────────────────────────

    public void sortBy(String visibleText) {
        selectByVisibleText(sortDropdown, visibleText);
    }

    public String getFirstProductName() {
        List<WebElement> names = findAll(itemNames);
        return names.isEmpty() ? "" : names.get(0).getText().trim();
    }

    // ── Product detail ────────────────────────────────────────────────────────

    public void clickProductByName(String productName) {
        By productLink = By.xpath("//div[@class='inventory_item_name'][text()='" + productName + "']");
        click(productLink);
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    public void logout() {
        click(burgerMenu);
        click(logoutLink);
    }
}
