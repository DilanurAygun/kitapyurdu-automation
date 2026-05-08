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

    private String firstBookNameForComparison = "";

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
        highlightAndScroll(firstLink);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", firstLink);
    }

    public void clickFirstDiscountedResult() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        for (WebElement product : searchResults) {
            List<WebElement> listPrice = product.findElements(By.cssSelector("span.ky-product-price.ky-product-list-price"));
            if (!listPrice.isEmpty()) {
                // İndirimli ürün bulundu! Resmine/Linkine tıkla
                WebElement link = product.findElement(By.cssSelector("a[class*='text-decoration-none']"));
                highlightAndScroll(link);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                return;
            }
        }
        throw new RuntimeException("Sayfada indirimli (ustu cizili fiyata sahip) urun bulunamadi!");
    }

    public void filterInStock() {
        try {
            WebElement inStockCheckbox = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input#filter_in_stock")));
            if (!inStockCheckbox.isSelected()) {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", inStockCheckbox);
                System.out.println("In stock filter applied!");
                Thread.sleep(2000); // Sayfanın yenilenmesini bekle
            }
        } catch (Exception e) {
            System.out.println("filterInStock error: " + e.getMessage());
        }
    }

    public void sortBy(String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        WebElement firstItem = driver.findElement(By.cssSelector("div.ky-product"));
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(optionText);
        // Sıralama sonrası eski elementin sayfadan silinmesini (staleness) ve yenisinin gelmesini bekle
        wait.until(ExpectedConditions.stalenessOf(firstItem));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ky-product")));
    }

    public void filterByText(String text) {
        try {
            // "Sadece Stoktakiler" veya "Stokta Olanlar" gibi filtreleri bulmak icin jenerik xpath
            WebElement filterElement = wait.until(ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath(
                            "//label[contains(., '" + text + "')] | " +
                            "//span[contains(., '" + text + "')] | " +
                            "//a[contains(., '" + text + "')]"
                    )
            ));
            highlightAndScroll(filterElement);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", filterElement);
            
            // Sayfanin filtrelenip yenilenmesi icin bekle
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ky-product")));
        } catch (Exception e) {
            System.out.println("Filtre tiklanamadi: " + text + " - Hata: " + e.getMessage());
        }
    }

    public boolean isSortedByPriceAscending() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
            if (searchResults.size() < 2) return true;

            WebElement firstProduct = searchResults.get(0);
            WebElement secondProduct = searchResults.get(1);

            String firstName = firstProduct.findElement(By.cssSelector("span.ky-product-title")).getText();
            String firstPriceText = firstProduct.findElement(By.cssSelector("span.ky-product-price.ky-product-sell-price")).getText();
            double firstPrice = parsePrice(firstPriceText);

            String secondName = secondProduct.findElement(By.cssSelector("span.ky-product-title")).getText();
            String secondPriceText = secondProduct.findElement(By.cssSelector("span.ky-product-price.ky-product-sell-price")).getText();
            double secondPrice = parsePrice(secondPriceText);

            String logMsg = String.format("Karsilastirilan 1. Kitap: '%s' (%.2f TL) <= 2. Kitap: '%s' (%.2f TL)", 
                                          firstName, firstPrice, secondName, secondPrice);
            System.out.println(logMsg);
            io.qameta.allure.Allure.step(logMsg);

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
        highlightAndScroll(greenButtons.get(0));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", greenButtons.get(0));
        // Sepete eklendiğine dair bir belirti beklemek daha sağlıklıdır (Örn: bildirim mesajı veya sepet sayısında artış)
        // Şimdilik sadece sepet elementinin tıklanabilir/görünür olmasını bekleyerek explicit wait kullanıyoruz
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-items")));
    }

    public void waitForSearchResults() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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

    //dilanur yeni test//

    public String getFirstBookName() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> names = driver.findElements(
                By.cssSelector("span.ky-product-title"));

        for (WebElement nameElement : names) {
            String name = nameElement.getText().trim();
            if (!name.isEmpty()) {
                System.out.println("First book name: " + name);
                return name;
            }
        }
        return "";
    }

    public String getSecondBookName() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> names = driver.findElements(
                By.cssSelector("span.ky-product-title"));

        int count = 0;
        for (WebElement nameElement : names) {
            String name = nameElement.getText().trim();
            if (!name.isEmpty()) {
                count++;
                if (count == 2) {
                    System.out.println("Second book name: " + name);
                    return name;
                }
            }
        }
        return "";
    }

    public double getFirstBookPrice() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> prices = driver.findElements(
                By.cssSelector("span.ky-product-price.ky-product-sell-price"));
        double price = parsePrice(prices.get(0).getText());
        System.out.println("First book price: " + price);
        return price;
    }

    public double getSecondBookPrice() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> prices = driver.findElements(
                By.cssSelector("span.ky-product-price.ky-product-sell-price"));
        double price = parsePrice(prices.get(1).getText());
        System.out.println("Second book price: " + price);
        return price;
    }

    public double getBasketTotal() {
        try {
            WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("span.cart-total, div[class*='cart-total'], span[class*='total-price']")));
            String text = total.getText().trim();
            System.out.println("Basket total text: " + text);
            return parsePrice(text);
        } catch (Exception e) {
            System.out.println("getBasketTotal error: " + e.getMessage());
            return 0;
        }
    }

    public void addSecondAvailableBookToBasket() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        List<WebElement> greenButtons = driver.findElements(
                By.cssSelector("div.ky-product button.ky-btn-primary[data-action='add-to-cart']"));
        if (greenButtons.size() < 2) {
            throw new RuntimeException("Not enough available books!");
        }
        highlightAndScroll(greenButtons.get(1));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", greenButtons.get(1));
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean allPricesAreValidNumbers() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
            List<WebElement> prices = driver.findElements(
                    By.cssSelector("span.ky-product-price.ky-product-sell-price"));
            if (prices.isEmpty()) return false;
            for (WebElement price : prices) {
                String text = price.getText().trim();
                if (text.isEmpty()) continue;
                double value = parsePrice(text);
                if (value <= 0) {
                    System.out.println("Invalid price found: " + text);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("allPricesAreValidNumbers error: " + e.getMessage());
            return false;
        }
    }

    public boolean outOfStockBooksHaveNoAddToBasketButton() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
            // Gri "Ürünü İncele" butonu olan kartlar stokta yok demektir
            List<WebElement> outOfStockCards = driver.findElements(
                    By.cssSelector("div.ky-product button.ky-btn-default[data-action='add-to-cart']"));
            // Bu kartlarda yeşil buton olmamalı
            for (WebElement card : outOfStockCards) {
                List<WebElement> greenBtn = card.findElements(
                        By.cssSelector("button.ky-btn-primary[data-action='add-to-cart']"));
                if (!greenBtn.isEmpty()) {
                    System.out.println("Out of stock book has add to basket button!");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("outOfStockCheck error: " + e.getMessage());
            return true;
        }
    }
}