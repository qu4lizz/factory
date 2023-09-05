package qu4lizz.factoryapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qu4lizz.factoryapp.Application;
import qu4lizz.factoryapp.utils.Logger;
import rmi.RawMaterial;

import java.io.IOException;
import java.util.logging.Level;

public class SingleOrderController {
    private static Stage stage;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label pricePerUnitLabel;

    @FXML
    private TextField quantityTextField;

    private RawMaterial item;
    private MainController candyController;

    @FXML
    void addItemOnMouseClicked(MouseEvent event) {
        try {
            double quantity = Double.parseDouble(quantityTextField.getText());

            if (quantity <= 0) throw new NumberFormatException();

            RawMaterial candy = new RawMaterial(item.getId(), item.getName(), quantity, item.getPrice());
            candyController.fromStockToOrder(candy);
            stage.close();
        } catch (NumberFormatException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
            PopUpController.showStage("Error", "Invalid quantity");
        }
    }

    private void setItemName(String name) {
        itemNameLabel.setText("Item name: " + name);
    }

    private void setPrice(double price) {
        pricePerUnitLabel.setText("Price per unit: " + String.format("%.2f",price));
    }

    public void setItem(RawMaterial item) {
        this.item = item;
    }

    public void setOrdersController(MainController candyController) {
        this.candyController = candyController;
    }

    public static void showStage(RawMaterial item, MainController candyController) {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(Application.fxmlPath + "single_order_controller.fxml"));

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("New Order");
        stage.getIcons().add(Application.icon);
        stage.setScene(scene);
        SingleOrderController controller = fxmlLoader.getController();
        controller.setItemName(item.getName());
        controller.setPrice(item.getPrice());
        controller.setItem(item);
        controller.setOrdersController(candyController);
        stage.showAndWait();
    }
}
