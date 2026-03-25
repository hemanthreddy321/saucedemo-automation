package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner - entry point for Cucumber + TestNG execution.
 *
 * Run via Maven:
 *   mvn test                                          (all @Regression)
 *   mvn test -Dcucumber.filter.tags="@Login"          (only login tests)
 *   mvn test -Dbrowser=firefox -Dheadless=true        (Firefox, headless)
 */
@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = "stepdefs",
    tags      = "@Regression",
    plugin    = {
        "pretty",
        "html:target/cucumber-reports/report.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome  = true,
    dryRun      = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Enabling parallel = true runs each scenario in its own thread.
     * Thread count is controlled by testng.xml thread-count attribute.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
