package qu4lizz.factoryrest.service;

import qu4lizz.factoryrest.dao.FactoryUserDAO;
import qu4lizz.factoryrest.model.FactoryUser;

import java.util.List;

public class FactoryUserService {
    private FactoryUserDAO factoryUserDAO = new FactoryUserDAO();

    public FactoryUserService() { }

    public List<FactoryUser> getFactoryUsers() {
        return factoryUserDAO.getUsers();
    }

    public boolean checkLogin(String name) {
        var users = factoryUserDAO.getUsers();

        for (var user : users) {
            if (user.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
