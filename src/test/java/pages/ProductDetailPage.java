package pages;

import org.openqa.selenium.By;

/**
 * ProductDetailPage - Page Object for individual product pages (/inventory-item.html)
 */
public class ProductDetailPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By productName        = By.className("inventory_details_name");
    private final By productDescription = By.className("inventory_details_desc");
    private final By productPrice       = By.className("inventory_details_price");
    private final By addToCartButton    = By.cssSelector("[data-test^='add-to-cart']");
    private final By removeButton       = By.cssSelector("[data-test^='remove']");
    private final By backButton         = By.id("back-to-products");
    private final By productImage       = By.className("inventory_details_img");

    // ── State checks ──────────────────────────────────────────────────────────

    public boolean isOnProductDetailPage() {
        return isDisplayed(productName);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean isProductImageDisplayed() {
        return isDisplayed(productImage);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void addToCart() {
        click(addToCartButton);
    }

    public void removeFromCart() {
        click(removeButton);
    }

    public void goBack() {
        click(backButton);
    }
}
