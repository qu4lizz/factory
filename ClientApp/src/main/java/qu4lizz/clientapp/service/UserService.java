package qu4lizz.clientapp.service;

import qu4lizz.clientapp.model.LoginDTO;
import qu4lizz.clientapp.model.User;
import qu4lizz.clientapp.utils.ConfigUtil;
import qu4lizz.clientapp.utils.GsonUtil;
import qu4lizz.clientapp.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;

public class UserService {
    private Properties properties;

    public UserService() {
        properties = ConfigUtil.getProperties();
    }
    public boolean login(LoginDTO user) throws IOException {
        String jsonCredentials = GsonUtil.gson.toJson(user);

        URL url = new URL(properties.getProperty("login_url"));

        return refactor(url, jsonCredentials);
    }

    public boolean register(User user) throws IOException {
        String jsonCredentials = GsonUtil.gson.toJson(user);

        URL url = new URL(properties.getProperty("register_url"));

        return refactor(url, jsonCredentials);
    }

    private boolean refactor(URL url, String jsonCredentials) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(jsonCredentials.getBytes());
        outputStream.flush();

        int responseCode = connection.getResponseCode();

        outputStream.close();
        connection.disconnect();

        return responseCode == HttpURLConnection.HTTP_OK;
    }

    public User getUser(String username) throws IOException {
        URL url = new URL(properties.getProperty("user_url") + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();

            return GsonUtil.gson.fromJson(jsonResponse, User.class);
        } else {
            Logger.logger.log(Level.SEVERE, "Response code " + responseCode);
        }

        connection.disconnect();

        return null;
    }

    public void checkREST() throws IOException {
        URL url = new URL(properties.getProperty("user_url"));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
    }
}
