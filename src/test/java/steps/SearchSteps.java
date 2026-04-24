package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.SearchResultsPage;

public class SearchSteps {

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ProductDetailPage detailPage;
    private int basketCountBefore;

    @When("I search for {string}")
    public void iSearchFor(String keyword) {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.searchFor(keyword);
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
    }

    @Then("search results should be displayed")
    public void searchResultsShouldBeDisplayed() {
        Assert.assertTrue(searchResultsPage.hasResults(), "No search results found!");
    }

    @When("I click on the first result")
    public void iClickOnFirstResult() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.clickFirstResult();
        detailPage = new ProductDetailPage(DriverManager.getDriver());
    }

    @Then("the product price should be displayed")
    public void productPriceShouldBeDisplayed() {
        Assert.assertTrue(detailPage.isPriceDisplayed(), "Price is not displayed!");
    }

    @Then("the availability status should be displayed")
    public void availabilityStatusShouldBeDisplayed() {
        Assert.assertTrue(detailPage.isInStock(), "Availability status is not displayed!");
    }

    @When("I sort by {string}")
    public void iSortBy(String option) {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.sortBy(option);
    }

    @Then("results should be sorted by price ascending")
    public void resultsShouldBeSortedByPriceAscending() {
        Assert.assertTrue(searchResultsPage.isSortedByPriceAscending(),
                "Results are not sorted by price ascending!");
    }

    @When("I add the first available book to basket")
    public void iAddFirstAvailableBookToBasket() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        basketCountBefore = searchResultsPage.getBasketCount();
        searchResultsPage.addFirstAvailableBookToBasket();
    }

    @Then("the basket count should increase")
    public void basketCountShouldIncrease() {
        int basketCountAfter = searchResultsPage.getBasketCount();
        Assert.assertTrue(basketCountAfter > basketCountBefore,
                "Basket count did not increase! Before: " + basketCountBefore + " After: " + basketCountAfter);
    }

    @Then("no results message should be displayed")
    public void noResultsMessageShouldBeDisplayed() {
        Assert.assertTrue(searchResultsPage.noResultsMessageDisplayed(),
                "No results message was not shown!");
    }

    @Then("the page should still be functional")
    public void pageShouldStillBeFunctional() {
        Assert.assertTrue(searchResultsPage.pageIsFunctional(),
                "Page is not functional after empty search!");
    }

    @Then("the product should show stock status")
    public void productShouldShowStockStatus() {
        Assert.assertTrue(detailPage.isInStock(), "Stock status is not shown!");
    }

    @Then("each product card should display image, name and price")
    public void eachProductCardShouldDisplayUIElements() {
        Assert.assertTrue(searchResultsPage.productCardsHaveUIElements(),
                "Some product cards are missing image, name or price!");
    }
}