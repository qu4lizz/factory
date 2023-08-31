package qu4lizz.factoryapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qu4lizz.factoryapp.Application;
import qu4lizz.factoryapp.model.Candy;
import qu4lizz.factoryapp.service.CandyService;
import qu4lizz.factoryapp.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class NewProductController implements Initializable {
    private CandyService candyService = new CandyService();
    private static Stage stage;

    @FXML
    private Button createButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private ChoiceBox<String> typeChoiceBox;
    private String[] types = {"Candy"};

    private Candy candy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeChoiceBox.getItems().addAll(Arrays.asList(types));
    }

    @FXML
    void createButtonOnMouseClicked(MouseEvent event) {
        try {
            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            double quantity = Double.parseDouble(quantityTextField.getText());

            if (name.isBlank() || price < 0 || quantity < 0)
                throw new IllegalArgumentException();

            candy = new Candy(name, price, quantity);

            stage.close();
        }
        catch (IllegalArgumentException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
            PopUpController.showStage("Error", "Invalid input");
        }
    }

    public Candy getItem() {
        return candy;
    }

    public static NewProductController showStage(Candy candy) {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "new_product_controller.fxml"));

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NewProductController controller = fxmlLoader.getController();
        if (candy != null) {
            controller.nameTextField.setText(candy.getName());
            controller.priceTextField.setText(String.valueOf(candy.getPrice()));
            controller.quantityTextField.setText(String.valueOf(candy.getQuantity()));
            stage.setTitle("Edit " + candy.getName());
            controller.createButton.setText("Edit");
        }
        else {
            stage.setTitle("New Product");
        }
        stage.getIcons().add(Application.icon);
        stage.setScene(scene);
        stage.showAndWait();
        stage.setMinHeight(500);
        stage.setMinWidth(700);

        return controller;
    }
}
