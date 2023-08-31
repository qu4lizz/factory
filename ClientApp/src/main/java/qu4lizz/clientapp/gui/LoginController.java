package qu4lizz.clientapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import qu4lizz.clientapp.Application;
import qu4lizz.clientapp.model.LoginDTO;
import qu4lizz.clientapp.model.User;
import qu4lizz.clientapp.service.UserService;
import qu4lizz.clientapp.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ToggleButton toggleButton;

    //login
    @FXML
    private AnchorPane loginPane;
    @FXML
    private TextField usernameLoginTextField;
    @FXML
    private PasswordField passwordLoginTextField;

    //register
    @FXML
    private AnchorPane registerPane;
    @FXML
    private TextField addressRegisterTextField;
    @FXML
    private TextField companyNameRegisterTextField;
    @FXML
    private PasswordField passwordRegisterTextField;
    @FXML
    private TextField phoneNumberRegisterTextField;
    @FXML
    private PasswordField repeatPasswordRegisterTextField;
    @FXML
    private TextField usernameRegisterTextField;

    private AnchorPane currentPane;
    public static User user;
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPane = loginPane;
        try {
            userService.checkREST();
            System.out.println("REST API Working");
            Logger.logger.log(Level.INFO, "REST API Working");
        }
        catch (IOException e) {
            System.out.println("REST API Not Working");
            Logger.logger.log(Level.SEVERE, "REST API Not Working");
        }
    }

    @FXML
    void toggleOnMouseClicked(MouseEvent event) {
        if (currentPane == loginPane) {
            currentPane = registerPane;
            toggleButton.setText("Login Instead");
            loginPane.setVisible(false);
            registerPane.setVisible(true);
        } else {
            currentPane = loginPane;
            toggleButton.setText("New Registration");
            loginPane.setVisible(true);
            registerPane.setVisible(false);
        }

        for (TextField textField : new TextField[]{usernameLoginTextField, passwordLoginTextField,
                                                    addressRegisterTextField, companyNameRegisterTextField,
                                                    phoneNumberRegisterTextField, passwordRegisterTextField,
                                                    repeatPasswordRegisterTextField, usernameRegisterTextField})
        {
            textField.clear();
        }
    }

    @FXML
    void loginOnMouseClicked(MouseEvent event) throws IOException {
        String username = usernameLoginTextField.getText();
        String password = passwordLoginTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            PopUpController.showStage("Error", "Please fill in all the fields");
            return;
        }

        try {
            boolean valid = userService.login(new LoginDTO(username, password));
            if (!valid) {
                PopUpController.showStage("Error", "Invalid username or password");
                return;
            }
            user = userService.getUser(username);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
            PopUpController.showStage("Error", "Could not connect to server");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "orders_controller.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Application.mainStage.setScene(scene);
        Application.mainStage.centerOnScreen();
    }


    @FXML
    void registerOnMouseClicked(MouseEvent event) {
        String companyName = companyNameRegisterTextField.getText();
        String address = addressRegisterTextField.getText();
        String phoneNumber = phoneNumberRegisterTextField.getText();
        String username = usernameRegisterTextField.getText();
        String password = passwordRegisterTextField.getText();
        String repeatPassword = repeatPasswordRegisterTextField.getText();

        if (companyName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            PopUpController.showStage("Error", "Please fill in all the fields");
            return;
        }
        if (!password.equals(repeatPassword)) {
            PopUpController.showStage("Error", "Passwords do not match");
            return;
        }

        try {
            boolean valid = userService.register(new User(username, password, companyName, address, phoneNumber));
            if (!valid) {
                PopUpController.showStage("Error", "Username already exists");
            }
            else {
                PopUpController.showStage("Info", "Account needs to be approved, wait for it");
            }
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, "Can't send POST to server", e.getMessage());
            PopUpController.showStage("Error", "Could not connect to server");
        }
    }
}
