package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class HomePage extends BasePage {

    @FindBy(id = "search-input")
    private WebElement searchBox;

    @FindBy(css = "div.ky-product")
    private List<WebElement> productCards;

    public HomePage(WebDriver driver) {
        super(driver);
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.get("https://www.kitapyurdu.com/");
                dismissCookiePopup();
                break;
            } catch (Exception e) {
                attempts++;
                System.out.println("Homepage load attempt " + attempts + " failed, retrying...");
                if (attempts == 3) throw e;
            }
        }
    }

    private void dismissCookiePopup() {
        try {
            // Cookie popup'in hic gorunmemesi ve bekleme yapmamasi icin CSS enjekte ediyoruz.
            // Bu sayede popup yuklense bile sayfada gorunmez ve hicbir seye engel olmaz.
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            String css = "#cookiescript_injected { display: none !important; z-index: -9999 !important; }";
            String script = "var style = document.createElement('style'); style.innerHTML = '" + css + "'; document.head.appendChild(style);";
            js.executeScript(script);
        } catch (Exception e) {
            System.out.println("CSS Injection for cookie popup failed: " + e.getMessage());
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchBox));
            return searchBox.isDisplayed();
        } catch (Exception e) {
            System.out.println("isLoaded error: " + e.getMessage());
            return false;
        }
    }

    public void searchFor(String keyword) {
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        highlightAndScroll(searchBox);
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.ENTER);
    }

    public void clickCategory(String categoryName) {
        // Tüm linkleri tara, text eşleşmesi bul
        List<WebElement> allLinks = driver.findElements(
                org.openqa.selenium.By.tagName("a")
        );
        for (WebElement link : allLinks) {
            try {
                if (link.getText().trim().equalsIgnoreCase(categoryName)) {
                    highlightAndScroll(link);
                    link.click();
                    return;
                }
            } catch (Exception ignored) {}
        }
    }
}