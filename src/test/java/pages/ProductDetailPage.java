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


  //===== dilanur yeni TESTLER =====


    public boolean hasNameAndPrice() {
        try {
            // Ürün adı var mı?
            WebElement name = driver.findElement(
                    org.openqa.selenium.By.cssSelector(
                            "h1.pr_header__heading"));
            boolean hasName = name.isDisplayed() && !name.getText().trim().isEmpty();

            // Fiyat var mı?
            boolean hasPrice = isPriceDisplayed();

            System.out.println("Has name: " + hasName + " Has price: " + hasPrice);
            return hasName && hasPrice;
        } catch (Exception e) {
            System.out.println("hasNameAndPrice error: " + e.getMessage());
            return false;
        }
    }

    private double parsePrice(String priceText) {
        try {
            return Double.parseDouble(
                    priceText.replaceAll("[^0-9,]", "").replace(",", "."));
        } catch (Exception e) {
            return 0;
        }
    }

}