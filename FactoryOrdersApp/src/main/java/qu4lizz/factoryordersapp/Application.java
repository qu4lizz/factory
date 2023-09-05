package qu4lizz.factoryordersapp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import qu4lizz.factoryordersapp.gui.LoginController;
import qu4lizz.factoryordersapp.utils.Logger;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

public class Application extends javafx.application.Application {
    public static Stage mainStage;
    public static String fxmlPath = "fxml" + File.separator;
    public static String iconPath = "icon" + File.separator;
    public static Image icon;

    @Override
    public void start(Stage stage) throws IOException {
        Logger.logger.log(Level.INFO, "Started");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath + "login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Factory Orders");
        icon = new Image(Application.class.getResource(iconPath + "factory.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;

        stage.setOnCloseRequest(event -> {
            LoginController.service.endCommunication();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}