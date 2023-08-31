package qu4lizz.distributorapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import qu4lizz.distributorapp.Application;
import qu4lizz.distributorapp.utils.Logger;

import java.io.*;
import java.util.logging.Level;

public class CompanyNameController {

    @FXML
    private TextField textField;
    public static String companyName;

    @FXML
    void okOnMouseClicked(MouseEvent event) throws IOException {
        companyName = textField.getText();
        if (companyName == null || companyName.isBlank()) {
            Logger.logger.log(Level.INFO, "Invalid name");
            PopUpController.showStage("Error", "Invalid name");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "distributor_controller.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Application.mainStage.setScene(scene);
        Application.mainStage.centerOnScreen();
    }

}
