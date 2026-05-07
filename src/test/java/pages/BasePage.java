package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void highlightAndScroll(org.openqa.selenium.WebElement element) {
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            
            // Ekrani elementin tam ortasina kaydir (Smooth scroll)
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            
            // Sunum icin elementi vurgula (Kirmizi cerceve ve sari arka plan)
            js.executeScript("arguments[0].style.border='3px solid red'; arguments[0].style.backgroundColor='yellow';", element);
            
            // Vurguyu izlemek icin 1 saniye bekle
            Thread.sleep(1000);
            
            // Vurguyu kaldir (Eski haline dondur)
            js.executeScript("arguments[0].style.border=''; arguments[0].style.backgroundColor='';", element);
        } catch (Exception e) {
            System.out.println("Highlight or scroll failed: " + e.getMessage());
        }
    }
}