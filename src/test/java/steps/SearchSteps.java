package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.BasketPage;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.SearchResultsPage;

import java.util.List;


public class SearchSteps {

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ProductDetailPage detailPage;
    private int basketCountBefore;
    private String firstBookName;
    private String secondBookName;
    private double firstBookPrice;
    private double secondBookPrice;
    private BasketPage basketPage;

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

    @When("I click on the first discounted result")
    public void iClickOnFirstDiscountedResult() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.clickFirstDiscountedResult();
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

    @When("I filter by in stock items")
    public void iFilterByInStockItems() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.filterInStock();
    }

    @When("I sort by {string}")
    public void iSortBy(String option) {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.sortBy(option);
    }

    @When("I filter by {string}")
    public void iFilterBy(String filterText) {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.filterByText(filterText);
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

    //dilanur yeni test



    @Then("the discounted price should be less than original price")
    public void discountedPriceShouldBeLessThanOriginal() {
        Assert.assertTrue(detailPage.hasDiscountedPrice(),
                "Discounted price is not less than original price!");
    }

    @Then("the basket total should be greater than zero")
    public void basketTotalShouldBeGreaterThanZero() {
        double total = searchResultsPage.getBasketTotal();
        Assert.assertTrue(total > 0,
                "Basket total is not greater than zero! Total: " + total);
    }

    @When("I add the second available book to basket")
    public void iAddSecondAvailableBookToBasket() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.addSecondAvailableBookToBasket();
    }

    @Then("the basket count should be {int}")
    public void basketCountShouldBe(int expectedCount) {
        int actualCount = searchResultsPage.getBasketCount();
        Assert.assertEquals(actualCount, expectedCount,
                "Basket count should be " + expectedCount + " but was " + actualCount);
    }

    @Then("all product prices should be valid numbers")
    public void allProductPricesShouldBeValidNumbers() {
        Assert.assertTrue(searchResultsPage.allPricesAreValidNumbers(),
                "Some products have invalid prices!");
    }

    @Then("the product detail page should show name and price")
    public void productDetailPageShouldShowNameAndPrice() {
        Assert.assertTrue(detailPage.hasNameAndPrice(),
                "Product detail page is missing name or price!");
    }

    @Then("out of stock books should not have add to basket button")
    public void outOfStockBooksShouldNotHaveAddToBasketButton() {
        Assert.assertTrue(searchResultsPage.outOfStockBooksHaveNoAddToBasketButton(),
                "Out of stock book has add to basket button!");
    }

    @When("I collect first book name and price from search results")
    public void iCollectFirstBookNameAndPrice() {
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        firstBookName = searchResultsPage.getFirstBookName();
        firstBookPrice = searchResultsPage.getFirstBookPrice();
    }

    @When("I collect second book name and price from search results")
    public void iCollectSecondBookNameAndPrice() {
        // Sepete ekledikten sonra sayfada hala arama sonuçları var
        // İkinci kitabın bilgilerini al
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
        searchResultsPage.waitForSearchResults();
        secondBookName = searchResultsPage.getSecondBookName();
        secondBookPrice = searchResultsPage.getSecondBookPrice();
        System.out.println("Second book name: " + secondBookName);
        System.out.println("Second book price: " + secondBookPrice);
    }

    @When("I navigate to basket page")
    public void iNavigateToBasketPage() {
        basketPage = new BasketPage(DriverManager.getDriver());
    }

    @Then("basket should contain the correct books")
    public void basketShouldContainCorrectBooks() {
        List<String> basketItems = basketPage.getBasketItemNames();
        System.out.println("Expected: " + firstBookName + ", " + secondBookName);
        System.out.println("Actual basket: " + basketItems);
        Assert.assertTrue(basketItems.stream().anyMatch(item ->
                        item.contains(firstBookName) || firstBookName.contains(item)),
                "First book not found in basket! Expected: " + firstBookName);
        Assert.assertTrue(basketItems.stream().anyMatch(item ->
                        item.contains(secondBookName) || secondBookName.contains(item)),
                "Second book not found in basket! Expected: " + secondBookName);
    }

    @Then("basket item prices should match search result prices")
    public void basketItemPricesShouldMatch() {
        List<Double> basketPrices = basketPage.getBasketItemPrices();
        System.out.println("Expected prices: " + firstBookPrice + ", " + secondBookPrice);
        System.out.println("Actual basket prices: " + basketPrices);
        Assert.assertTrue(basketPrices.contains(firstBookPrice) ||
                        basketPrices.stream().anyMatch(p -> Math.abs(p - firstBookPrice) < 0.01),
                "First book price mismatch! Expected: " + firstBookPrice);
        Assert.assertTrue(basketPrices.contains(secondBookPrice) ||
                        basketPrices.stream().anyMatch(p -> Math.abs(p - secondBookPrice) < 0.01),
                "Second book price mismatch! Expected: " + secondBookPrice);
    }

    @Then("basket total should equal sum of item prices")
    public void basketTotalShouldEqualSumOfItemPrices() {
        double expectedTotal = firstBookPrice + secondBookPrice;
        double actualTotal = basketPage.getBasketTotal();
        System.out.println("Expected total: " + expectedTotal);
        System.out.println("Actual total: " + actualTotal);
        Assert.assertEquals(actualTotal, expectedTotal, 0.01,
                "Basket total mismatch! Expected: " + expectedTotal + " Actual: " + actualTotal);
    }

    @When("I search for a keyword with {int} characters")
    public void iSearchForKeywordWithCharacters(int length) {
        homePage = new HomePage(DriverManager.getDriver());
        StringBuilder longKeyword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            longKeyword.append("a");
        }
        homePage.searchFor(longKeyword.toString());
        searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
    }

    @Then("a validation message for maximum length should be displayed")
    public void aValidationMessageForMaximumLengthShouldBeDisplayed() {
        // We assert that the site SHOULD have a max length validation message.
        // Since Kitapyurdu does not validate this properly and just searches,
        // this test will naturally FAIL, which is the intended demonstration of a bug!
        boolean isValidationShown = false;
        try {
            // Trying to find any common validation message element
            isValidationShown = searchResultsPage.noResultsMessageDisplayed() && 
                DriverManager.getDriver().getPageSource().contains("uzun");
        } catch (Exception e) {
            isValidationShown = false;
        }
        
        Assert.assertTrue(isValidationShown, 
            "BUG DETECTED: Uygulama cok uzun (300+ karakter) arama kelimelerini dogrulamadan kabul ediyor! " +
            "Beklenen: 'Arama kelimesi cok uzun' tarzi bir uyari gosterilmesiydi.");
    }

    @When("I enter {string} as quantity")
    public void iEnterAsQuantity(String qty) {
        basketPage.updateQuantity(qty);
    }

    @Then("the basket should show an error message for invalid quantity")
    public void theBasketShouldShowAnErrorMessageForInvalidQuantity() {
        boolean isErrorShown = basketPage.isQuantityErrorMessageDisplayed();
        Assert.assertTrue(isErrorShown,
            "BUG DETECTED: Sepet miktari olarak gecersiz/negatif bir deger (-1) girilmesine ragmen " +
            "sistem kullaniciyi duzgun bir hata mesajiyla uyarmadi. Gecersiz miktar islemi yonetilemiyor!");
    }
}