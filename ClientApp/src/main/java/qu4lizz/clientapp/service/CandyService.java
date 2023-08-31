package qu4lizz.clientapp.service;

import com.google.gson.reflect.TypeToken;
import qu4lizz.clientapp.model.Candy;
import qu4lizz.clientapp.utils.ConfigUtil;
import qu4lizz.clientapp.utils.GsonUtil;
import qu4lizz.clientapp.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class CandyService {
    private Properties properties;

    public CandyService() {
        properties = ConfigUtil.getProperties();
    }
    public List<Candy> getItems() throws IOException {
        URL url = new URL(properties.getProperty("items_url"));
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

            Type itemListType = new TypeToken<ArrayList<Candy>>() {}.getType();

            return GsonUtil.gson.fromJson(jsonResponse, itemListType);
        } else {
            Logger.logger.log(Level.SEVERE, "Response code " + responseCode);
        }

        connection.disconnect();

        return null;
    }

}
