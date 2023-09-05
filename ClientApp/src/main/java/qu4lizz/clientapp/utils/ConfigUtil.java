package qu4lizz.clientapp.utils;

import qu4lizz.clientapp.Application;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

public class ConfigUtil {
    public static String CONFIG_FOLDER = "config" + File.separator;
    private static final String CONFIG_FILE_PATH = CONFIG_FOLDER + "config.properties";
    private static Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileReader(Application.class.getResource(CONFIG_FILE_PATH).getFile()));
            } catch (IOException ex) {
                Logger.logger.log(Level.SEVERE, ex.getMessage());
            }
        }
        return properties;
    }
}