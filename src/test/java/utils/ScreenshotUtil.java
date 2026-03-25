package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - captures screenshots for reporting and debugging.
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = ConfigReader.get("screenshot.dir") != null
            ? ConfigReader.get("screenshot.dir")
            : "target/screenshots/";

    private ScreenshotUtil() {}

    /** Capture and return as byte array (for Cucumber scenario attach) */
    public static byte[] captureAsBytes() {
        WebDriver d = DriverFactory.getDriver();
        if (d == null) return new byte[0];
        return ((TakesScreenshot) d).getScreenshotAs(OutputType.BYTES);
    }

    /** Capture and return as Base64 string (for Extent Reports inline) */
    public static String captureAsBase64() {
        WebDriver d = DriverFactory.getDriver();
        if (d == null) return "";
        return ((TakesScreenshot) d).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Save screenshot to file and return absolute path.
     * Useful for Extent Reports file-based attachment.
     */
    public static String captureAndSave(String testName) {
        WebDriver d = DriverFactory.getDriver();
        if (d == null) return "";

        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";

            File src = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));
            System.out.println("[Screenshot] Saved: " + fileName);
            return fileName;
        } catch (IOException e) {
            System.err.println("[Screenshot] Failed to save: " + e.getMessage());
            return "";
        }
    }
}
