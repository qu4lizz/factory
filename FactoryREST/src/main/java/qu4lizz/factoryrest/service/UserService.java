package qu4lizz.factoryrest.service;

import qu4lizz.factoryrest.dao.UserDAO;
import qu4lizz.factoryrest.exceptions.ActivationNotApprovedException;
import qu4lizz.factoryrest.exceptions.BlockedUserException;
import qu4lizz.factoryrest.model.LoginDTO;
import qu4lizz.factoryrest.model.User;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userDAO.getUsers();
    }

    private void addUser(User newUser) {
        List<User> userList = userDAO.getUsers();
        userList.add(newUser);
        userDAO.saveUsers(userList);
    }

    public User getByUsername(String username) {
        return userDAO.getUsers().stream().filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
    }

    public User login(LoginDTO user) throws BlockedUserException, CredentialException, ActivationNotApprovedException {
        User existingUser = getByUsername(user.getUsername());
        if (existingUser != null) {
            if (existingUser.getPassword().equals(user.getPassword())) {
                if (existingUser.isBlocked())
                    throw new BlockedUserException();
                else if (!existingUser.isActivated())
                    throw new ActivationNotApprovedException();
                else
                    return existingUser;
            } else {
                throw new CredentialException("Wrong password");
            }
        } else {
            throw new CredentialException("User not found");
        }
    }

    public boolean register(User user) {
        User existingUser = getByUsername(user.getUsername());
        if (existingUser == null) {
            addUser(user);
            return true;
        }
        return false;
    }

    public boolean activate(String username) {
        List<User> users = userDAO.getUsers();

        for (var user : users) {
            if (user.getUsername().equals(username)) {
                user.setActivated(true);
                userDAO.saveUsers(users);
                return true;
            }
        }

        return false;
    }

    public boolean setBlocked(String username, boolean isBlocked) {
        List<User> users = userDAO.getUsers();

        for (var user : users) {
            if (user.getUsername().equals(username)) {
                user.setBlocked(isBlocked);
                userDAO.saveUsers(users);
                return true;
            }
        }

        return false;
    }
}
