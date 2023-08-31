package qu4lizz.factoryordersapp.service;

import qu4lizz.factoryordersapp.Application;
import qu4lizz.factoryordersapp.utils.ConfigUtil;
import qu4lizz.factoryordersapp.utils.SecureServerProtocol;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.Properties;

public class SecureServerService {
    private static final String HOST;
    private static final int PORT;
    private static final String KEY_STORE_PATH;
    private static final String KEY_STORE_PASSWORD;
    private SSLSocket socket;
    private BufferedReader in;
    private PrintWriter out;

    static {
        Properties properties = ConfigUtil.getProperties();
        PORT = Integer.parseInt(properties.getProperty("secure_server_port"));
        HOST = properties.getProperty("secure_server_host");
        KEY_STORE_PASSWORD = properties.getProperty("keystore_password");

        String tmp = properties.getProperty("keystore_path");
        KEY_STORE_PATH = Application.class.getResource(tmp).getFile();
    }

    public SecureServerService() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
            trustManagerFactory.init(trustStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            socket = (SSLSocket) sslSocketFactory.createSocket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean login(String name) throws IOException {
        String message = SecureServerProtocol.LOGIN.getValue() + SecureServerProtocol.SEPARATOR.getValue() + name;
        out.println(message);

        String response = in.readLine();

        return SecureServerProtocol.SUCCESS.getValue().equals(response);
    }

    public boolean sendOrderInfo(String info) throws IOException {
        String message = SecureServerProtocol.ORDER.getValue() + SecureServerProtocol.SEPARATOR.getValue() + info;

        out.println(message);

        String response = in.readLine();

        return SecureServerProtocol.SUCCESS.getValue().equals(response);
    }

    public void endCommunication() {
        String message = SecureServerProtocol.END.getValue();
        out.println(message);

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
