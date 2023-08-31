package qu4lizz.factoryapp.utils;

import qu4lizz.factoryapp.Application;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class Logger {
    private static final String LOGGER_PATH;
    public static java.util.logging.Logger logger;

    static {
        LOGGER_PATH = DbUtil.DATABASE_FOLDER + ConfigUtil.getProperties().getProperty("logger_path") + "factoryapp.log";
        try {
            logger = java.util.logging.Logger.getLogger(Application.class.getName());
            Handler handler = new FileHandler(LOGGER_PATH);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
