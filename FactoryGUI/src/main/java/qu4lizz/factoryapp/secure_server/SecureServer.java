package qu4lizz.factoryapp.secure_server;

import qu4lizz.factoryapp.utils.ConfigUtil;
import qu4lizz.factoryapp.utils.DbUtil;
import qu4lizz.factoryapp.utils.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class SecureServer extends Thread {
    private static final int PORT;
    private static final String KEY_STORE_PATH;
    private static final String KEY_STORE_PASSWORD;
    public static AtomicBoolean isRunning = new AtomicBoolean(true);

    static {
        Properties properties = ConfigUtil.getProperties();
        PORT = Integer.parseInt(properties.getProperty("secure_server_port"));
        KEY_STORE_PATH = DbUtil.DATABASE_FOLDER + properties.getProperty("keystore_path");
        KEY_STORE_PASSWORD = properties.getProperty("keystore_password");
    }

    @Override
    public void run() {
        System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
        System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);

        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(PORT);

            System.out.println("Secure server running on port " + PORT);

            while (isRunning.get()) {
                SSLSocket s = (SSLSocket) ss.accept();
                new ServerThread(s).start();
            }
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }
}