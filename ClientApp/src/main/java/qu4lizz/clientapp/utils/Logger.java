package qu4lizz.clientapp.utils;

import qu4lizz.clientapp.Application;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class Logger {
    private static final String LOGGER_PATH;
    public static java.util.logging.Logger logger;

    static {
        LOGGER_PATH = ConfigUtil.getProperties().getProperty("logger_path") + "client.log";
        try {
            logger = java.util.logging.Logger.getLogger(Application.class.getName());
            Handler handler = new FileHandler(LOGGER_PATH);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
