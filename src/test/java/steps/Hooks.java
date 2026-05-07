package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before
    public void setUp() {
        DriverManager.getDriver();
        dismissCookiePopup();
    }

    private void dismissCookiePopup() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait =
                    new org.openqa.selenium.support.ui.WebDriverWait(
                            DriverManager.getDriver(), java.time.Duration.ofSeconds(5));
            org.openqa.selenium.WebElement rejectButton = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                            org.openqa.selenium.By.cssSelector("div#cookiescript_reject")));
            rejectButton.click();
            System.out.println("Cookie popup rejected!");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("No cookie popup: " + e.getMessage());
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot");
        }
        DriverManager.quitDriver();
    }
}