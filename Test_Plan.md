# Example Test Plan
**DOCUMENT INFORMATION**
*   **Project Name:** Kitapyurdu Test Automation Project
*   **Project Number:** PRJ-2026-05
*   **Author:** QA Automation Team
*   **Issue Date:** 08/05/2026

**DOCUMENT HISTORY**
| Version | Date | Summary of change | Author |
| :--- | :--- | :--- | :--- |
| 1.0 | 08/05/2026 | Initial Test Plan Release | QA Automation Team |

**APPROVALS**
| Name | Title | Signature | Escalation |
| :--- | :--- | :--- | :--- |
| Tuba | Project Manager | | |
| Tuba | Test Manager | | |

---

## 1. HOW TO USE THIS DOCUMENT
This document is a Software Test Plan for the Kitapyurdu e-commerce test automation project. It captures testing requirements, the context of the project, project goals, system functionality to be tested, risks, and the procedures/tools to be used. The objective is to ensure a well-tested product by actively participating in the Software Development Life Cycle (SDLC) and defining testing estimates, test environments, and resources.

## 2. INTRODUCTION
Kitapyurdu is a major online bookstore that experiences heavy user traffic. Ensuring the stability, accuracy, and reliability of the platform is critical, especially during high-demand periods like the start of academic terms or promotional campaigns.
This testing project focuses on automating the core end-to-end (E2E) user journeys, from searching for books to sorting, validating prices, adding items to the basket, and verifying basket calculations. The automation framework will use Selenium WebDriver, Java, Cucumber (BDD), and Allure Reporting to run these tests efficiently and provide comprehensive visual reports.

## 3. OBJECTIVES
The main objectives of this testing phase are:
*   **Functional Verification:** To ensure that the core functionality (Search, Sort, Product Details, Basket operations) performs according to the User Requirements.
*   **Bug Identification:** To find and report functional boundaries or validation gaps (e.g., negative basket quantities, excessively long search strings).
*   **Automated Regression:** To build a robust regression suite that can be run continuously using Maven without human intervention.
*   **Clear Reporting:** To generate clear, business-friendly reports using Allure, ensuring stakeholders can easily interpret test results.

## 4. SCOPE

### 4.1 Test items
The following areas of the Kitapyurdu platform are in scope for system testing:

*   **Navigation & Homepage:** Loading the homepage and navigating through categories.
*   **Search Engine:** Performing valid, invalid, empty, and extremely long searches.
*   **Search Results:** Validating UI elements (image, name, price) on product cards.
*   **Filtering & Sorting:** Sorting by "Ucuzdan Pahalıya" (Price Ascending) and verifying the mathematical accuracy of the sorting algorithm.
*   **Product Details:** Validating the data integrity between search results and the detailed product page.
*   **Basket/Cart Module:** Adding available items, validating basket totals, and testing negative scenarios (like adding out-of-stock items or entering negative quantities).

### 4.2 Features to be tested
Due to the time and resource pressures on this release, only the high-risk core user journeys will be covered:
*   Homepage loading and Category navigation.
*   Data-driven search functionality (Biyografi, Roman, Tarih).
*   Price sorting and mathematical verification of ascending prices.
*   Basket count updates and total price calculations (Sum of items = Basket Total).
*   Negative validations (Boundary value analysis for search inputs, negative quantity inputs in the basket).

### 4.3 Features not to be tested
The following features will not be tested in this phase:
*   User Registration and Login flow.
*   Payment Gateway integration and Checkout process.
*   Levels of concurrent users (Load/Performance testing is out of scope).
*   Cross-browser compatibility (Testing will be strictly executed on Google Chrome).

## 5. APPROACH
A Behavior-Driven Development (BDD) approach will be used, allowing developers, testers, and business analysts to collaborate using standard Gherkin syntax (Given, When, Then). 

**Tools to be used:**
*   **Java 21** – Core programming language.
*   **Selenium WebDriver (4.x)** – Web browser automation.
*   **Cucumber** – BDD framework for readable test scripts.
*   **TestNG & Maven** – Test execution and dependency management.
*   **Allure** – Test management and HTML reporting.

### 5.1 Test Scripts/Documentation
Cucumber Feature files will serve as the living documentation and test scripts for this project. They are located in `src/test/resources/features/kitapyurdu.feature`.

### 5.2 Schedule
| Milestone Task | Effort (Days) | Start Date | End Date |
| :--- | :--- | :--- | :--- |
| Test Plan Sign off | 1 | 08/05/2026 | 08/05/2026 |
| BDD Script Creation | 2 | 09/05/2026 | 10/05/2026 |
| Test Automation (Coding) | 3 | 11/05/2026 | 13/05/2026 |
| Test Execution & Reporting | 1 | 14/05/2026 | 14/05/2026 |

### 5.3 Deliverables
Those of specific interest to testing are:
*   **Test Plan:** This document.
*   **Test Scripts:** Cucumber `.feature` files containing the automated scenarios.
*   **Test Automation Code:** Page Object Model (POM) classes and Step Definitions.
*   **Test Completion Report:** The Allure HTML report generated after the `mvn clean test` execution.

### 5.4 Defects & Metrics
#### 5.4.1 Defects
Defects will be captured during test execution and highlighted in the Allure reports. Examples of identified bugs in this release:
*   **BUG-01:** System accepts a 300-character search keyword without validation.
*   *(Previously found but resolved)*: System sorting algorithm fails to filter out empty prices dynamically.

#### 5.4.2 Test Metrics
Metrics will be published via Allure Reports, which automatically calculate:
*   Total Scenarios Executed
*   Pass/Fail/Broken Percentage
*   Execution Time per Scenario
*   Defect Severity Analysis

### 5.5 Test Suspension and Entry / Exit Criteria
#### 5.5.1 Suspension Criteria
Testing will be suspended if the Kitapyurdu homepage returns a 5xx Server Error or if a CAPTCHA entirely blocks automated browser access.

#### 5.5.2 Entry Criteria
*   The Test Plan has been ‘Signed Off’.
*   Selenium and Maven environments are successfully configured.

#### 5.5.3 Exit Criteria
*   All high-priority core scenarios (Search, Sort, Basket) have been executed.
*   No outstanding Showstopper (Priority 0) defects in the test code.
*   Allure Report is successfully generated and accessible.

### 5.6 Resources & Responsibilities
#### 5.6.1 Roles and Responsibilities
| Staff Name | Role | Deliverable or Work Package | Responsibility |
| :--- | :--- | :--- | :--- |
| Tuba | Lead QA Automation Engineer | Test Plan, Automation Code, Reports | Overall management of test activities, framework architecture, and execution. |

#### 5.6.2 Resources and Environments
1.  **Hardware:** A standard Windows PC with internet access.
2.  **Software:** Google Chrome (latest version), JDK 21, Maven.
3.  **Environment:** Production environment (`https://www.kitapyurdu.com/`).

## 6. RISKS & CONTINGENCIES
| Risk Or Issue | Risk Level | Likely Effects And Mitigation |
| :--- | :--- | :--- |
| Dynamic UI changes by Kitapyurdu | High | Elements (like buttons or dropdowns) may change IDs. *Mitigation: Use robust XPath and CSS selectors (e.g., matching partial classes instead of hardcoded IDs).* |
| CAPTCHA or Bot Protection | Medium | Automation may be blocked. *Mitigation: Disable popup blocking via ChromeOptions and avoid excessively rapid clicks using implicit/explicit waits.* |

## 7. SIGN OFF / APPROVALS
The following approve the contents and intentions of the Kitapyurdu Automation Test Plan.

| Name | Appointment | Approval Signature | Signatory Remarks |
| :--- | :--- | :--- | :--- |
| Tuba | Project Executive | | |
| Tuba | Test Manager | | |

## 8. APPENDIX
Below is an example of the test cases written in Gherkin (BDD) format, which serve as the actual executable scripts for this project.

### Example Test Case: Search & Sort Functionality
**Test Scenario ID:** SORT-01
**Test Case Description:** Sort results by price ascending – Positive test case
**Test Priority:** High

**Test Execution Steps:**
| S.No | Action | Expected Output | Test Result |
| :--- | :--- | :--- | :--- |
| 1 | Launch Kitapyurdu homepage | Homepage loads successfully | Pass |
| 2 | Enter "Biyografi" in search box and hit enter | Search results are displayed | Pass |
| 3 | Select "Ucuzdan Pahalıya" from the sort dropdown | Products reorder dynamically | Pass |
| 4 | Retrieve prices of the first two valid products | System correctly parses the price strings | Pass |
| 5 | Verify that Product 1 Price <= Product 2 Price | Prices are mathematically ascending | Pass |

### Example Test Case: Basket Add
**Test Scenario ID:** CART-01
**Test Case Description:** Add book to basket – Positive test case
**Test Priority:** High

**Test Execution Steps:**
| S.No | Action | Expected Output | Test Result |
| :--- | :--- | :--- | :--- |
| 1 | Search for "Biyografi" | Search results are displayed | Pass |
| 2 | Click the green "Sepete Ekle" button on the first available book | Basket icon updates | Pass |
| 3 | Read the basket count number | Basket count should be "1" | Pass |
