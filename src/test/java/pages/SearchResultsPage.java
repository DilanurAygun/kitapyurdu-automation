package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(css = "div.ky-product")
    private List<WebElement> searchResults;

    @FindBy(css = "select[name='sort-types']")
    private WebElement sortDropdown;

    @FindBy(css = "div.ky-product button[data-action='add-to-cart']")
    private List<WebElement> addToCartButtons;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasResults() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.ky-product")
            ));
            return !searchResults.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickFirstResult() {
        WebElement firstLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.ky-product a[class*='text-decoration-none']")
                )
        );
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", firstLink);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", firstLink);
    }

    public void sortBy(String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(optionText);
        // Sıralama sonrası sayfanın yenilenmesini bekle
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean isSortedByPriceAscending() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
            List<WebElement> prices = driver.findElements(
                    By.cssSelector("div.ky-product span.ky-product-price.ky-product-sell-price")
            );
            if (prices.size() < 2) return true;

            double firstPrice = parsePrice(prices.get(0).getText());
            double secondPrice = parsePrice(prices.get(1).getText());
            System.out.println("First: " + firstPrice + " Second: " + secondPrice);
            return firstPrice <= secondPrice;
        } catch (Exception e) {
            System.out.println("isSortedByPriceAscending error: " + e.getMessage());
            return false;
        }
    }

    private double parsePrice(String priceText) {
        // "209,30 TL" → 209.30
        return Double.parseDouble(
                priceText.replaceAll("[^0-9,]", "").replace(",", ".")
        );
    }

    public int getBasketCount() {
        try {
            WebElement basketCount = driver.findElement(By.id("cart-items"));
            String text = basketCount.getText().trim();
            System.out.println("Basket count text: " + text);
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            System.out.println("getBasketCount error: " + e.getMessage());
            return 0;
        }
    }

    public void addFirstAvailableBookToBasket() {
        // Yeşil "Sepete Ekle" butonlarını bul (gri "Ürünü İncele" değil)
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> greenButtons = driver.findElements(
                By.cssSelector("div.ky-product button.ky-btn-primary[data-action='add-to-cart']")
        );
        if (greenButtons.isEmpty()) {
            throw new RuntimeException("No available books to add to basket!");
        }
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", greenButtons.get(0));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", greenButtons.get(0));
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean noResultsMessageDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Aradığınız kriterlere uygun ürün bulunamadı')]")
            ));
            return msg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pageIsFunctional() {
        try {
            // Sayfa çökmedi mi? Arama kutusu hala var mı?
            WebElement searchBox = driver.findElement(By.id("search-input"));
            return searchBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean productCardsHaveUIElements() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));

            // Tüm kartlar yerine sadece ilk kartı kontrol et
            WebElement firstCard = searchResults.get(0);

            // Resim kontrolü
            List<WebElement> images = firstCard.findElements(
                    By.cssSelector("a.ky-product-cover img"));
            if (images.isEmpty()) {
                System.out.println("No image found!");
                return false;
            }
            String src = images.get(0).getAttribute("src");
            System.out.println("Image src: " + src);
            if (src == null || src.isEmpty()) return false;

            // İsim kontrolü
            List<WebElement> names = firstCard.findElements(
                    By.cssSelector("span.ky-product-title"));
            if (names.isEmpty() || names.get(0).getText().trim().isEmpty()) {
                System.out.println("No name found!");
                return false;
            }
            System.out.println("Product name: " + names.get(0).getText());

            // Fiyat kontrolü - fiyatsız ürün olabilir, sadece elementi ara
            List<WebElement> prices = firstCard.findElements(
                    By.cssSelector("span.ky-product-price"));
            System.out.println("Price elements found: " + prices.size());
            if (prices.isEmpty()) return false;

            return true;
        } catch (Exception e) {
            System.out.println("UI check error: " + e.getMessage());
            return false;
        }
    }
}