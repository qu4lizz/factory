package qu4lizz.factoryapp.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import qu4lizz.factoryapp.model.Candy;
import qu4lizz.factoryapp.model.Distributor;
import qu4lizz.factoryapp.service.PromotionService;
import rmi.RawMaterial;
import qu4lizz.factoryapp.model.User;
import qu4lizz.factoryapp.service.CandyService;
import rmi.DistributorService;
import qu4lizz.factoryapp.service.UserService;
import qu4lizz.factoryapp.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class MainController implements Initializable {
    private final UserService userService = new UserService();
    private final CandyService candyService = new CandyService();
    // users pane
    @FXML
    private AnchorPane usersPaneLeft;
    @FXML
    private AnchorPane usersPane;
    @FXML
    void usersPaneOnMouseClicked(MouseEvent event) {
        setActivePane(usersPaneLeft, usersPane);
        typeChoiceBox.setValue(types[0]);
        List<User> users = userService.getUsers().stream().filter(User::isActivated).toList();
        usersTable.getItems().clear();
        usersTable.getItems().addAll(users);
    }
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> usernameTableColumn;
    @FXML
    private TableColumn<User, String> addressTableColumn;
    @FXML
    private TableColumn<User, String> companyNameTableColumn;
    @FXML
    private TableColumn<User, String> phoneNumberTableColumn;
    @FXML
    private TableColumn<User, String> blockedTableColumn;
    private final String[] types = {"Active users", "Requests"};
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Button acceptButton;
    @FXML
    private Button refuseButton;
    @FXML
    private Button blockButton;
    @FXML
    private Button unblockButton;

    // products pane
    @FXML
    private AnchorPane productsPaneLeft;
    @FXML
    private AnchorPane productsPane;
    @FXML
    void productsPaneOnMouseClicked(MouseEvent event) {
        setActivePane(productsPaneLeft, productsPane);
        List<Candy> candies = candyService.getCandies();
        productTable.getItems().clear();
        productTable.getItems().addAll(candies);
    }
    @FXML
    private TableView<Candy> productTable;
    @FXML
    private TableColumn<Candy, String> productNameTableColumn;
    @FXML
    private TableColumn<Candy, Double> quantityTableColumn;
    @FXML
    private TableColumn<Candy, Double> priceTableColumn;

    // distributors pane
    @FXML
    private AnchorPane distributorsPaneLeft;
    @FXML
    private AnchorPane distributorsPane;
    @FXML
    void distributorsPaneOnMouseClicked(MouseEvent event) {
        setActivePane(distributorsPaneLeft, distributorsPane);
        refreshDistributorList();
        rawMaterials.clear();
        orderTable.getItems().clear();
    }
    @FXML
    private ChoiceBox<Distributor> chooseDistributorChoiceBox;

    @FXML
    private TableView<RawMaterial> stockTable;
    @FXML
    private TableColumn<RawMaterial, String> stockNameTableColumn;
    @FXML
    private TableColumn<RawMaterial, Double> stockPriceTableColumn;
    @FXML
    private TableColumn<RawMaterial, Double> stockQuantityTableColumn;

    @FXML
    private TableView<RawMaterial> orderTable;
    @FXML
    private TableColumn<RawMaterial, String> orderNameTableColumn;
    @FXML
    private TableColumn<RawMaterial, Double> orderQuantityTableColumn;
    @FXML
    private TableColumn<RawMaterial, Double> orderSumPriceTableColumn;

    private final DistributorService distributorService = new DistributorService();
    private ObservableList<Distributor> distributorList;
    private ObservableList<RawMaterial> rawMaterials;

    // promotions
    @FXML
    private AnchorPane publishPane;
    @FXML
    private AnchorPane publishPaneLeft;
    @FXML
    void promotionsPaneOnMouseClicked(MouseEvent event) {
        setActivePane(publishPaneLeft, publishPane);
    }
    @FXML
    private TextArea promotionsTextArea;
    private final PromotionService promotionService = new PromotionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getUsername()));
        addressTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getAddress()));
        companyNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getCompanyName()));
        phoneNumberTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPhone()));
        blockedTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().isBlocked() ? "Yes" : "No"));

        typeChoiceBox.getItems().addAll(Arrays.asList(types));
        typeChoiceBox.setValue(types[0]);
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(types[0])) {
                acceptButton.setVisible(false);
                refuseButton.setVisible(false);
                blockButton.setVisible(true);
                unblockButton.setVisible(true);
                List<User> users = userService.getUsers().stream().filter(User::isActivated).toList();
                usersTable.getItems().clear();
                usersTable.getItems().addAll(users);
            } else {
                acceptButton.setVisible(true);
                refuseButton.setVisible(true);
                blockButton.setVisible(false);
                unblockButton.setVisible(false);
                List<User> users = userService.getUsers().stream().filter(x -> !x.isActivated()).toList();
                usersTable.getItems().clear();
                usersTable.getItems().addAll(users);
            }
        });


        productNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        quantityTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));
        priceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice()));

        // distributor
        stockNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        stockPriceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice()));
        stockQuantityTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));

        orderNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        orderQuantityTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));
        orderSumPriceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice() * cellData.getValue().getQuantity()));


        distributorList = FXCollections.observableArrayList();
        chooseDistributorChoiceBox.setItems(distributorList);

        rawMaterials = FXCollections.observableArrayList();
        stockTable.setItems(rawMaterials);

        chooseDistributorChoiceBox.setOnMouseClicked(event -> {
            refreshDistributorList();
        });

        chooseDistributorChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null) {
                    distributorService.connect(newValue.getCompanyName());
                    rawMaterials.clear();
                    var list = distributorService.getRawMaterials();
                    rawMaterials.addAll(list);
                    orderTable.getItems().clear();
                }
            } catch (RemoteException | NotBoundException e) {
                Logger.logger.log(Level.SEVERE, e.getMessage());
            }
        });

        stockTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                RawMaterial item = stockTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    SingleOrderController.showStage(item, this);
                }
            }
        });

        orderTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                RawMaterial item = orderTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    fromOrderToStock(item);
                }
            }
        });

        setProductMaxId();
    }

    private void setProductMaxId() {
        Candy.setNextId(candyService.getMaxId());
    }

    private void refreshDistributorList() {
        distributorList.clear();
        try {
            String[] distributors = distributorService.getDistributorList();
            Arrays.stream(distributors).forEach(x -> distributorList.add(new Distributor(x)));
        } catch (RemoteException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void setActivePane(AnchorPane pane, AnchorPane paneView) {
        for (var lPane : Arrays.asList(usersPaneLeft, productsPaneLeft, distributorsPaneLeft, publishPaneLeft))
            lPane.setStyle("-fx-background-color: #5d5d5d");

        pane.setStyle("-fx-background-color: #888888");

        for (var p : Arrays.asList(usersPane, productsPane, distributorsPane, publishPane))
            p.setVisible(false);

        paneView.setVisible(true);
    }

    @FXML
    void unblockUserOnMouseClicked(MouseEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            PopUpController.showStage("Error", "No user selected");
            return;
        }
        userService.setBlocked(user.getUsername(), false);
        refreshUserTable();
    }

    @FXML
    void blockOnMouseClicked(MouseEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            PopUpController.showStage("Error", "No user selected");
            return;
        }
        userService.setBlocked(user.getUsername(), true);
        refreshUserTable();
    }

    @FXML
    void refuseOnMouseClicked(MouseEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            PopUpController.showStage("Error", "No user selected");
            return;
        }
        userService.refuseActivation(user.getUsername());
        refreshUserTable();
    }
    @FXML
    void acceptOnMouseClicked(MouseEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            PopUpController.showStage("Error", "No user selected");
            return;
        }
        userService.activate(user.getUsername());
        refreshUserTable();
    }

    private void refreshUserTable() {
        usersTable.getItems().clear();
        usersTable.getItems().addAll(userService.getUsers());
    }

    @FXML
    void createNewProductOnMouseClicked(MouseEvent event) {
        NewProductController controller = NewProductController.showStage(null);
        if (controller != null) {
            Candy candy = controller.getItem();

            candyService.addCandy(candy);
            refreshProductTable();
        }
    }
    @FXML
    void deleteProductOnMouseClicked(MouseEvent event) {
        Candy candy = productTable.getSelectionModel().getSelectedItem();

        if (candy == null) {
            PopUpController.showStage("Error", "No product selected");
            return;
        }

        candyService.deleteCandy(candy.getId());
        refreshProductTable();
    }
    @FXML
    void modifyButtonOnMouseClicked(MouseEvent event) {
        Candy candy = productTable.getSelectionModel().getSelectedItem();

        if (candy == null) {
            PopUpController.showStage("Error", "No product selected");
            return;
        }

        NewProductController controller = NewProductController.showStage(candy);

        Candy newCandy = controller.getItem();

        candyService.updateCandy(newCandy);
        refreshProductTable();
    }
    private void refreshProductTable() {
        productTable.getItems().clear();
        productTable.getItems().addAll(candyService.getCandies());
    }

    @FXML
    void createOrderOnMouseClicked(MouseEvent event) {
        List<RawMaterial> materials = orderTable.getSelectionModel().getSelectedItems();

        if (materials == null) {
            PopUpController.showStage("Error", "No products selected");
            return;
        }

        try {
            for(var material : materials)
                distributorService.buyRawMaterial(material.getId(), material.getQuantity());
        }
        catch (RemoteException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void fromOrderToStock(RawMaterial item) {
        RawMaterial stockItem = stockTable.getItems().stream().filter(i -> i.getId() == item.getId()).findFirst().get();
        stockItem.setQuantity(stockItem.getQuantity() + item.getQuantity());
        stockTable.refresh();

        orderTable.getItems().remove(item);
    }

    void fromStockToOrder(RawMaterial orderItem) {
        if (orderTable.getItems().contains(orderItem)) {
            RawMaterial item = orderTable.getItems().stream().filter(i -> i.getId() == orderItem.getId()).findFirst().get();
            item.setQuantity(item.getQuantity() + orderItem.getQuantity());
        } else {
            orderTable.getItems().add(orderItem);
        }
        RawMaterial stockItem = stockTable.getItems().stream().filter(i -> i.getId() == orderItem.getId()).findFirst().get();
        stockItem.setQuantity(stockItem.getQuantity() - orderItem.getQuantity());

        orderTable.refresh();
        stockTable.refresh();
    }

    @FXML
    void publishOnMouseClicked(MouseEvent event) {
        String text = promotionsTextArea.getText();
        try {
            promotionService.sendPromotion(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}