# Automation Assignment API

This repository contains an **API automation framework** focusing on **user authentication and client
management**. The framework is designed for **readability, maintainability, and data-driven testing**, and includes both
positive and negative test scenarios.

---

## **Technologies Used**

* **Java 17+** – Programming language
* **JUnit 5** – Test framework
* **RestAssured** – API testing and validation
* **Allure** – Reporting and attachments for test results
* **Jackson** – JSON parsing for environment and user configuration
* **Maven** – Build and dependency management

---

## **Project Structure**

```

 src
├─ main
│  ├─ java/ch/swisssign
│  │  └─ base/BaseApiTest.java         # Abstract base class for API tests
│  │  └─ client/SwissSignApiClient.java # API client for SwissSign endpoints
│  │  └─ utils/ConfigLoader.java       # Configuration loader for environments and users
│  └─ resources/environments.json      # Environment-specific base URLs
└─ test
   ├─ java/ch/swisssign/tests          # Test classes
   └─ resources
      ├─ allure.properties             # Test reporting configuration
      └─ testdata
         ├─ users.csv                  # CSV for data-driven login tests
         └─ users.json                 # JSON for data-driven login tests

target                                  # Build output and test reports
└─ allure-results                       # Generated test results after execution
```
---

## **Prerequisites**

* **Java 17+** installed
* **Maven 3+** installed
* **Git** installed
* **Internet access** to reach the API endpoints
* (**Optional, for local report viewing**) **Allure CLI** installed – https://docs.qameta.io/allure/#_installing_a_commandline  


---

## **Getting Started**

1. **Clone the repository**

```
git clone https://github.com/pavol-pavol/AutomationTestAssignmentAPI
cd AutomationTestAssignmentAPI
```

2. **Install dependencies using Maven**

```bash
mvn clean install

```

* Note: running mvn clean install will also compile the code, run all tests, and install the package to your local Maven repository.

3. **Run tests**

* Run all tests:

```bash
mvn clean test
```

* Run tests for a specific environment (e.g., prod):

```bash
mvn clean test -Denv=preprod
```

4. **Data-driven tests**

* Some tests read user credentials from `/testdata/users.csv` or `users.json`.
* These tests validate multiple login scenarios automatically.

5. **View Allure reports**

There are two options to view reports:

Serve the report using Maven (temporary local server):
```bash
mvn allure:serve
```

* Includes request/response attachments, JWT tokens, and failure screenshots.

Or generate a static HTML report (requires Allure CLI):

```
allure generate target/allure-results -o target/allure-report --clean
```
Or generate single file allure report that can be easily shared
```
allure generate target/allure-results -o target/allure-report --clean --single-file

```
---

## **Test Scenarios**

### Positive Flows

* User login with valid credentials
* Fetch client information with valid token

### Negative Flows

* Login with invalid email
* Login with invalid password
* Fetch client info without a token

### Data-Driven Tests

* Login attempts for multiple users defined in CSV or JSON

---

## **Notes**

* Supports **environment switching** via `-Denv=<env>`.
* Uses **AllureRestAssured** for enhanced reporting.
* Follows **clean design patterns** for maintainability.
* Easily extendable for additional endpoints and scenarios.

---
