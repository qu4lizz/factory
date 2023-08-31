package qu4lizz.distributorapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import qu4lizz.distributorapp.gui.PopUpController;
import qu4lizz.distributorapp.utils.Logger;

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
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath + "company_name.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setTitle("Distributor Client");
        icon = new Image(Application.class.getResource(iconPath + "factory.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}