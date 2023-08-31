package qu4lizz.factoryapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import qu4lizz.factoryapp.secure_server.SecureServer;
import qu4lizz.factoryapp.utils.Logger;

import java.io.IOException;
import java.util.logging.Level;

public class Application extends javafx.application.Application {
    public static String fxmlPath = "fxml/";
    public static String iconPath = "icon/";
    public static Image icon;
    @Override
    public void start(Stage stage) throws IOException {
        Logger.logger.log(Level.SEVERE, "Started");

        SecureServer secureServer = new SecureServer();
        secureServer.start();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath + "factory_main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Factory");
        icon = new Image(Application.class.getResource(iconPath + "factory.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}