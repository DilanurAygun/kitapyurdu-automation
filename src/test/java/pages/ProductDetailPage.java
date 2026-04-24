package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailPage extends BasePage {

    @FindBy(css = "span.ky-product-price, span[class*='sell-price'], div[class*='price']")
    private WebElement price;

    @FindBy(css = "div[class*='stock'], span[class*='stock'], button[data-action='add-to-cart']")
    private WebElement stockStatus;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPriceDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(price));
            return price.isDisplayed() && !price.getText().isEmpty();
        } catch (Exception e) {
            System.out.println("isPriceDisplayed error: " + e.getMessage());
            return false;
        }
    }

    public boolean isInStock() {
        try {
            wait.until(ExpectedConditions.visibilityOf(stockStatus));
            return stockStatus.isDisplayed();
        } catch (Exception e) {
            System.out.println("isInStock error: " + e.getMessage());
            return false;
        }
    }
}