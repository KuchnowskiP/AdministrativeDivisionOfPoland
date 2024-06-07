package pl.edu.pwr.database.administrativedivisionofpoland;

import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    static {
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
