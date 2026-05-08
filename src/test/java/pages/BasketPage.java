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
        highlightAndScroll(cartIcon);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", cartIcon);

        // Sepete Git butonuna tıkla
        WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a#js-cart")));
        highlightAndScroll(goToCartButton);
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
        highlightAndScroll(total);
        String text = total.getText().trim();
        System.out.println("Basket total text: " + text);
        return parsePrice(text);
    }

    public void updateQuantity(String qty) {
        WebElement qtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("form input[name='quantity']")));
        highlightAndScroll(qtyInput);
        qtyInput.clear();
        qtyInput.sendKeys(qty);
        
        WebElement refreshBtn = driver.findElement(By.cssSelector("i.fa-refresh, i.fa-sync, i[class*='update']"));
        highlightAndScroll(refreshBtn);
        refreshBtn.click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    public boolean isQuantityErrorMessageDisplayed() {
        // Sepette geçersiz bir miktar (-1 vb.) girildiğinde, düzgün bir e-ticaret sitesi hata göstermelidir.
        // Ancak Kitapyurdu muhtemelen ya "0" yapacak ya hata vermeyecek ya da görmezden gelecektir.
        // Biz burada sitenin bir uyarı fırlatmasını bekliyoruz.
        try {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.alert-danger, span.error, div[class*='error']")));
            highlightAndScroll(errorMsg);
            return errorMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
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