package qu4lizz.factoryapp.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class ConfigUtil {

    public static final String CONFIG_FOLDER = "config/";
    private static final String CONFIG_FILE_PATH = CONFIG_FOLDER + "config.properties";
    private static Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileReader(DbUtil.DATABASE_FOLDER + CONFIG_FILE_PATH));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return properties;
    }
}
