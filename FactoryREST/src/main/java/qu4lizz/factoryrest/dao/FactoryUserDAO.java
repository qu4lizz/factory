package qu4lizz.factoryrest.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import qu4lizz.factoryrest.model.FactoryUser;
import qu4lizz.factoryrest.utils.Logger;
import qu4lizz.factoryrest.utils.ConfigUtil;
import qu4lizz.factoryrest.utils.DbUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FactoryUserDAO {
    private final static String PATH = DbUtil.DATABASE_FOLDER + ConfigUtil.getProperties().getProperty("factory_user_json");

    public List<FactoryUser> getUsers() {
        List<FactoryUser> userList = new ArrayList<>();
        try (Reader reader = new FileReader(PATH)) {
            Type type = new TypeToken<List<FactoryUser>>() {}.getType();
            userList = new Gson().fromJson(reader, type);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
        return userList;
    }

    public void saveUsers(List<FactoryUser> userList) {
        try (Writer writer = new FileWriter(PATH)) {
            new Gson().toJson(userList, writer);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
