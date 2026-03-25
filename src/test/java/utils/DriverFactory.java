package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory - manages WebDriver lifecycle with ThreadLocal
 * for safe parallel test execution.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {}

    /**
     * Initialise the WebDriver for the current thread.
     * Browser and headless mode are read from config / system properties.
     */
    public static void initDriver() {
        String browser  = ConfigReader.get("browser");
        if (browser == null || browser.isEmpty()) browser = "chrome";

        boolean headless = ConfigReader.getBool("headless", false);

        WebDriver d;

        switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                opts.addArguments("--width=1920", "--height=1080");
                d = new FirefoxDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) {
                    opts.addArguments(
                        "--headless=new",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--disable-gpu",
                        "--window-size=1920,1080"
                    );
                } else {
                    opts.addArguments("--start-maximized");
                }
                opts.addArguments("--disable-notifications", "--disable-infobars");
                d = new ChromeDriver(opts);
            }
        }

        int pageLoad = ConfigReader.getInt("page.load.timeout", 30);
        int implicit = ConfigReader.getInt("implicit.wait", 10);

        d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));

        if (!headless) {
            d.manage().window().maximize();
        }

        driver.set(d);
        System.out.println("[DriverFactory] Launched " + browser + " (headless=" + headless + ")");
    }

    /** Returns the WebDriver for the current thread */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /** Quits the driver and removes the thread-local reference */
    public static void quitDriver() {
        WebDriver d = driver.get();
        if (d != null) {
            try {
                d.quit();
            } catch (Exception e) {
                System.err.println("[DriverFactory] Error quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}
