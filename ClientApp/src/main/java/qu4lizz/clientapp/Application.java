package qu4lizz.clientapp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import qu4lizz.clientapp.gui.CandyController;
import qu4lizz.clientapp.utils.Logger;

import java.io.IOException;
import java.util.logging.Level;

public class Application extends javafx.application.Application {
    public static String fxmlPath = "fxml/";
    public static String iconPath = "icon/";
    public static Image icon;
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        Logger.logger.log(Level.INFO, "Started");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath + "client_login_controller.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Factory Client");
        icon = new Image(Application.class.getResource(iconPath + "factory.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;

        stage.setOnCloseRequest((event) -> {
            CandyController.isActive.set(false);
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}