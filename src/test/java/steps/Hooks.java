package steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @io.cucumber.java.BeforeAll
    public static void beforeAll() {
        try {
            // Allure raporu icin ortam ve kategori bilgilerini otomatik olustur
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("allure-results"));
            java.nio.file.Files.writeString(
                java.nio.file.Paths.get("allure-results/environment.properties"),
                "Browser=Chrome\nOS=Windows 11\nProject=Kitapyurdu Automation\nTeam=Tugba & Dilanur\nTest_Type=UI Automation");
            
            java.nio.file.Files.writeString(
                java.nio.file.Paths.get("allure-results/categories.json"),
                "[\n  {\n    \"name\": \"Gercek Sistem Aciklari (Bugs)\",\n    \"matchedStatuses\": [\"failed\"]\n  },\n  {\n    \"name\": \"Bozuk/Eksik Kod Hatalari\",\n    \"matchedStatuses\": [\"broken\"]\n  }\n]");
                
            java.nio.file.Files.writeString(
                java.nio.file.Paths.get("allure-results/executor.json"),
                "{\n  \"name\": \"Maven\",\n  \"type\": \"maven\",\n  \"buildName\": \"Kitapyurdu-Tests-Run\",\n  \"buildUrl\": \"http://localhost\"\n}");
        } catch (Exception e) {
            System.out.println("Allure configuration failed: " + e.getMessage());
        }
    }

    @Before
    public void setUp() {
        DriverManager.getDriver();
    }

    @AfterStep
    public void afterStep() {
        // Sunum esnasında adımların daha net görünmesi için her adımdan sonra yarım saniye (500ms) bekleme eklendi
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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