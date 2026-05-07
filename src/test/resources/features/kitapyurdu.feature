Feature: Kitapyurdu Website Tests

  # ===== POZİTİF TESTLER =====

  Scenario: NAV-01 Homepage loads successfully
    Given I open Kitapyurdu homepage
    Then the page title should contain "Kitapyurdu"

  Scenario: NAV-02 Search for a book
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    Then search results should be displayed

  Scenario: NAV-03 Navigate to Edebiyat category
    Given I open Kitapyurdu homepage
    When I click on "Edebiyat" category
    Then the category page should load

  Scenario: PRICE-01 Book detail page shows price
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I click on the first result
    Then the product price should be displayed

  Scenario: SORT-01 Sort results by price ascending
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I sort by "Ucuzdan Pahalıya"
    Then results should be sorted by price ascending

  Scenario: CART-01 Add book to basket
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I add the first available book to basket
    Then the basket count should increase

  Scenario: STOCK-01 In stock book has add to basket button
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I click on the first result
    Then the product should show stock status

  # ===== NEGATİF TESTLER =====

  Scenario: NEG-01 Search with invalid keyword shows no results
    Given I open Kitapyurdu homepage
    When I search for "xyzxyzxyz123abc"
    Then no results message should be displayed

  Scenario: NEG-02 Empty search does not crash
    Given I open Kitapyurdu homepage
    When I search for ""
    Then the page should still be functional


  Scenario: UI-01 Search results show product image, name and price
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    Then each product card should display image, name and price


    # ===== Dilanur yeni TESTLER =====

  Scenario: PRICE-02 Discounted price is correctly displayed
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I click on the first result
    Then the discounted price should be less than original price


  Scenario: CART-02 Basket validation with two books
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I collect first book name and price from search results
    And I add the first available book to basket
    And I collect second book name and price from search results
    And I add the second available book to basket
    And I navigate to basket page
    Then basket should contain the correct books
    And basket item prices should match search result prices
    And basket total should equal sum of item prices

  Scenario: CART-03 Adding two books increases basket count to 2
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I add the first available book to basket
    And I add the second available book to basket
    Then the basket count should be 2

  Scenario: FILTER-01 Price filter returns products in range
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I sort by "Ucuzdan Pahalıya"
    Then all product prices should be valid numbers


  Scenario: DETAIL-01 Product detail page shows complete information
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    And I click on the first result
    Then the product detail page should show name and price

  Scenario: NEG-03 Out of stock book cannot be added to basket
    Given I open Kitapyurdu homepage
    When I search for "Biyografi"
    Then out of stock books should not have add to basket button