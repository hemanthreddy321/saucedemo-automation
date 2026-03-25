package stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverFactory;
import utils.ScreenshotUtil;

/**
 * Hooks - Cucumber lifecycle hooks.
 * @Before  : launches browser before each scenario
 * @After   : captures screenshot on failure, then quits browser
 */
public class Hooks {

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        System.out.println("\n========================================");
        System.out.println("START: " + scenario.getName());
        System.out.println("Tags : " + scenario.getSourceTagNames());
        System.out.println("========================================");
        DriverFactory.initDriver();
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        System.out.println("\n----------------------------------------");
        System.out.println("END: " + scenario.getName() + " → " + scenario.getStatus());

        if (scenario.isFailed()) {
            System.out.println("FAILED — attaching screenshot...");
            try {
                byte[] screenshot = ScreenshotUtil.captureAsBytes();
                scenario.attach(screenshot, "image/png", "failure-" + scenario.getName());
            } catch (Exception e) {
                System.err.println("Could not capture screenshot: " + e.getMessage());
            }
        }

        DriverFactory.quitDriver();
        System.out.println("----------------------------------------\n");
    }
}
