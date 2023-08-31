package qu4lizz.factoryordersapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import qu4lizz.factoryordersapp.Application;
import qu4lizz.factoryordersapp.service.SecureServerService;
import qu4lizz.factoryordersapp.utils.Logger;

import java.io.IOException;
import java.util.logging.Level;

public class LoginController {
    public static final SecureServerService service = new SecureServerService();

    @FXML
    private TextField nameTextField;

    public static String user;

    @FXML
    void loginOnMouseClicked(MouseEvent event) {
        String name = nameTextField.getText();
        if (name != null && !name.isBlank()) {
            try {
                if (service.login(name)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "orders_controller.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Application.mainStage.setScene(scene);
                    Application.mainStage.centerOnScreen();
                    user = name;
                }
                else {
                    PopUpController.showStage("Error", "Incorrect name");
                }
            } catch (IOException e) {
                Logger.logger.log(Level.SEVERE, e.getMessage());
            }
        }
        else {
            PopUpController.showStage("Error", "Name cannot be empty");
        }
    }

}
