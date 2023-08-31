package qu4lizz.factoryapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qu4lizz.factoryapp.Application;

import java.io.IOException;

public class PopUpController {
    private static Stage stage;

    @FXML
    private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    void OKMouseClicked(MouseEvent event) {
        stage.close();
    }

    public static void showStage(String title, String message) {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "popup_controller.fxml"));

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle(title);
        stage.getIcons().add(Application.icon);
        stage.setScene(scene);
        PopUpController controller = fxmlLoader.getController();
        controller.setMessage(message);
        stage.showAndWait();
        stage.setMinHeight(420);
        stage.setMinWidth(666);
    }
}

