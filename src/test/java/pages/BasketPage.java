package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class BasketPage extends BasePage {

    public BasketPage(WebDriver driver) {
        super(driver);

        // JavaScript ile sepet ikonuna tıkla
        WebElement cartIcon = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div#cart")));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", cartIcon);

        // Sepete Git butonuna tıkla
        WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a#js-cart")));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", goToCartButton);

        // Sepet sayfasının yüklenmesini bekle
        wait.until(ExpectedConditions.urlContains("checkout/cart"));
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public List<String> getBasketItemNames() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("p.product-name.flex-grow-1")));
        List<WebElement> names = driver.findElements(
                By.cssSelector("p.product-name.flex-grow-1"));
        List<String> nameList = new java.util.ArrayList<>();
        for (WebElement name : names) {
            String text = name.getText().trim();
            System.out.println("Basket item: " + text);
            nameList.add(text);
        }
        return nameList;
    }

    public List<Double> getBasketItemPrices() {
        List<WebElement> prices = driver.findElements(
                By.cssSelector("p.fs-14.fw-bold.lh-1.mb-0"));
        List<Double> priceList = new java.util.ArrayList<>();
        for (WebElement price : prices) {
            String text = price.getText().trim();
            double value = parsePrice(text);
            System.out.println("Basket item price: " + value);
            priceList.add(value);
        }
        return priceList;
    }

    public double getBasketTotal() {
        WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div#cart-nav-total")));
        String text = total.getText().trim();
        System.out.println("Basket total text: " + text);
        return parsePrice(text);
    }

    private double parsePrice(String priceText) {
        try {
            return Double.parseDouble(
                    priceText.replaceAll("[^0-9,]", "").replace(",", "."));
        } catch (Exception e) {
            System.out.println("parsePrice error: " + e.getMessage());
            return 0;
        }
    }
}