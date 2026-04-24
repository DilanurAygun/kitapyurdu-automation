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
        driver.get("https://www.kitapyurdu.com/");
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
                    link.click();
                    return;
                }
            } catch (Exception ignored) {}
        }
    }
}