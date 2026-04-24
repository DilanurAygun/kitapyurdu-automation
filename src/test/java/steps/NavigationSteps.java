package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.HomePage;

public class NavigationSteps {

    private HomePage homePage;

    @Given("I open Kitapyurdu homepage")
    public void iOpenHomepage() {
        homePage = new HomePage(DriverManager.getDriver());
        Assert.assertTrue(homePage.isLoaded(), "Homepage did not load!");
    }

    @Then("the page title should contain {string}")
    public void pageTitleShouldContain(String expectedTitle) {
        Assert.assertTrue(homePage.getTitle().contains(expectedTitle),
                "Title does not contain: " + expectedTitle +
                        " Actual: " + homePage.getTitle());
    }

    @When("I click on {string} category")
    public void iClickOnCategory(String categoryName) {
        homePage.clickCategory(categoryName);
    }

    @Then("the category page should load")
    public void categoryPageShouldLoad() {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("kategori") || currentUrl.contains("kitap"),
                "Category page did not load! URL: " + currentUrl);
    }
}