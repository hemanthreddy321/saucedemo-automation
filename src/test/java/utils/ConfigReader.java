package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - reads config.properties and allows
 * system property overrides (used by Jenkins -D flags).
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found at src/test/resources/", e);
        }
    }

    /**
     * Get a config value. System property takes precedence over properties file.
     * This allows -Dbrowser=firefox to override config.
     */
    public static String get(String key) {
        String sysProp = System.getProperty(key);
        return (sysProp != null && !sysProp.isEmpty()) ? sysProp : props.getProperty(key);
    }

    /** Returns the base URL for the active environment (default: staging) */
    public static String getBaseUrl() {
        String env = get("env");
        if (env == null || env.isEmpty()) env = "staging";
        return get(env + ".url");
    }

    /** Convenience: get int value with a fallback default */
    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        try {
            return val != null ? Integer.parseInt(val.trim()) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /** Convenience: get boolean value */
    public static boolean getBool(String key, boolean defaultValue) {
        String val = get(key);
        return val != null ? Boolean.parseBoolean(val.trim()) : defaultValue;
    }
}
