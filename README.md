# SauceDemo Automation Framework

End-to-end test automation for [https://www.saucedemo.com](https://www.saucedemo.com)

**Stack:** Selenium 4 · Java 17 · TestNG · Maven · Cucumber 7 · Page Object Model · Extent Reports · Jenkins CI/CD

---

## Project Structure

```
saucedemo-automation/
├── pom.xml                          # Maven dependencies & build config
├── Jenkinsfile                      # Jenkins pipeline (declarative)
├── extent-config.xml                # Extent Reports theme/title config
└── src/test/
    ├── java/
    │   ├── pages/                   # Page Object Model classes
    │   │   ├── BasePage.java
    │   │   ├── LoginPage.java
    │   │   ├── InventoryPage.java
    │   │   ├── CartPage.java
    │   │   ├── CheckoutPage.java
    │   │   └── ProductDetailPage.java
    │   ├── stepdefs/                # Cucumber step definitions
    │   │   ├── Hooks.java
    │   │   ├── LoginSteps.java
    │   │   ├── InventorySteps.java
    │   │   └── CheckoutSteps.java
    │   ├── runners/
    │   │   └── TestRunner.java      # Cucumber + TestNG runner
    │   └── utils/
    │       ├── ConfigReader.java    # Reads config.properties
    │       ├── DriverFactory.java   # ThreadLocal WebDriver manager
    │       ├── ScreenshotUtil.java  # Screenshot capture helper
    │       └── ExcelDataReader.java # Apache POI Excel reader
    └── resources/
        ├── features/
        │   ├── Login.feature
        │   ├── Inventory.feature
        │   └── Checkout.feature
        ├── testdata/                # Excel files for data-driven tests
        ├── config.properties        # Environment URLs, credentials, timeouts
        ├── extent.properties        # Extent Reports output config
        └── testng.xml               # TestNG suite definition
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17+ |
| Maven | 3.8+ |
| Chrome | Latest |
| Firefox | Latest (optional) |

WebDriverManager auto-downloads the correct chromedriver/geckodriver — no manual setup needed.

---

## Run Tests

```bash
# All regression tests (headed Chrome)
mvn test

# Specific tag
mvn test -Dcucumber.filter.tags="@Login"
mvn test -Dcucumber.filter.tags="@Inventory"
mvn test -Dcucumber.filter.tags="@Checkout"

# Headless Chrome (for CI)
mvn test -Dheadless=true

# Firefox, headless
mvn test -Dbrowser=firefox -Dheadless=true

# Target a specific environment
mvn test -Denv=staging

# Combine options
mvn test -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@Regression"
```

---

## Reports

After the test run, open:

| Report | Path |
|--------|------|
| Extent Spark (interactive) | `target/ExtentReports/SparkReport.html` |
| Extent Classic HTML | `target/ExtentReports/index.html` |
| Cucumber HTML | `target/cucumber-reports/report.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| Surefire XML | `target/surefire-reports/` |
| Screenshots (failures) | `target/screenshots/` |

---

## Jenkins Setup

1. Install plugins: **Maven Integration**, **Git**, **HTML Publisher**, **Cucumber Reports**, **Pipeline**
2. **Manage Jenkins → Global Tool Configuration** — add Maven 3.9 and JDK 17
3. **New Item → Pipeline** → name: `saucedemo-automation`
4. Pipeline definition: `Pipeline script from SCM` → Git → your repo URL → branch `main`
5. Script Path: `Jenkinsfile`
6. Build Triggers: `GitHub hook trigger for GITScm polling`
7. In GitHub repo → Settings → Webhooks → `http://<jenkins-host>:8080/github-webhook/`

### Build Parameters

| Parameter | Options | Default |
|-----------|---------|---------|
| `BROWSER` | chrome, firefox | chrome |
| `ENV` | staging, prod | staging |
| `TAGS` | any Cucumber tag | @Regression |

---

## SauceDemo Test Accounts

| Username | Password | Type |
|----------|----------|------|
| `standard_user` | `secret_sauce` | Normal user |
| `locked_out_user` | `secret_sauce` | Locked (login fails) |
| `problem_user` | `secret_sauce` | Broken images |
| `performance_glitch_user` | `secret_sauce` | Slow UI |
| `error_user` | `secret_sauce` | Random errors |
| `visual_user` | `secret_sauce` | Visual bugs |

---

## Excel Data-Driven Testing

Place `.xlsx` files in `src/test/resources/testdata/`. Use `ExcelDataReader`:

```java
List<Map<String,String>> rows = ExcelDataReader.readSheet(
    "src/test/resources/testdata/LoginData.xlsx", "Login");
for (Map<String,String> row : rows) {
    String username = row.get("username");
    String password = row.get("password");
}
```

---

## Parallel Execution

Controlled by `testng.xml`:
```xml
<suite parallel="methods" thread-count="3">
```
And `TestRunner.java`:
```java
@DataProvider(parallel = true)
public Object[][] scenarios() { ... }
```

---

## Author

Built as a complete reference framework for Selenium Java + TestNG + Maven + Cucumber + POM + Extent Reports + Jenkins CI/CD.
