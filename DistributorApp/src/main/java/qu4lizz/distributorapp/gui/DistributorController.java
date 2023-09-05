package qu4lizz.distributorapp.gui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import qu4lizz.distributorapp.Application;
import qu4lizz.distributorapp.utils.Logger;
import rmi.RawMaterial;
import rmi.DistributorInterface;
import rmi.DistributorServer;
import qu4lizz.distributorapp.utils.ConfigUtil;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.logging.Level;

public class DistributorController implements Initializable {

    @FXML
    private Label companyName;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TableView<RawMaterial> table;

    @FXML
    private TableColumn<RawMaterial, String> tableNameColumn;

    @FXML
    private TableColumn<RawMaterial, Double> tablePriceColumn;

    @FXML
    private TableColumn<RawMaterial, Double> tableQuantityColumn;

    private DistributorServer server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        tableQuantityColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));
        tablePriceColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice()));

        companyName.setText(CompanyNameController.companyName);
        registerDistributor();
    }

    @FXML
    void addOnMouseClicked(MouseEvent event) {
        try {
            String name = nameTextField.getText();
            double quantity = Double.parseDouble(quantityTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());

            if (name == null || name.isBlank())
                throw new IllegalArgumentException();

            RawMaterial existingMaterial = table.getItems().stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
            if (existingMaterial == null) {
                RawMaterial newMaterial = new RawMaterial(name, quantity, price);
                server.addMaterial(newMaterial);
            }
            else {
                existingMaterial.setQuantity(existingMaterial.getQuantity() + quantity);
            }
            table.refresh();

            nameTextField.clear();
            priceTextField.clear();
            quantityTextField.clear();
        }
        catch (IllegalArgumentException e) {
            Logger.logger.log(Level.WARNING, "Invalid input");
            PopUpController.showStage("Error", "Invalid input");
        }
    }

    @FXML
    void deleteOnMouseClicked(MouseEvent event) {
        RawMaterial item = table.getSelectionModel().getSelectedItem();
        if (item == null) {
            Logger.logger.log(Level.INFO, "No row selected");
            PopUpController.showStage("Error", "No row selected");
            return;
        }
        server.removeMaterial(item);
        table.refresh();
    }

    private void registerDistributor() {
        Properties properties = ConfigUtil.getProperties();
        String policy = Application.class.getResource(properties.getProperty("policy")).getPath();
        int port = Integer.parseInt(properties.getProperty("registry_port"));

        System.out.println(companyName.getText() + " registered for RMI on port " + port);

        System.setProperty("java.security.policy", policy);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            server = new DistributorServer();
            table.setItems(server.getList());
            DistributorInterface stub = (DistributorInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.rebind(companyName.getText(), stub);
        } catch (Exception e) {
            PopUpController.showStage("Error", "Couldn't register to RMI");
            Logger.logger.log(Level.SEVERE, "Couldn't register to RMI");
        }
    }
}
