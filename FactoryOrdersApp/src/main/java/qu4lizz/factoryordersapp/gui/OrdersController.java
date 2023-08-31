package qu4lizz.factoryordersapp.gui;

import jakarta.xml.bind.JAXBException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import qu4lizz.factoryordersapp.model.Candy;
import qu4lizz.factoryordersapp.model.OrderWrapper;
import qu4lizz.factoryordersapp.service.MailService;
import qu4lizz.factoryordersapp.service.MessageQueueService;
import qu4lizz.factoryordersapp.service.SecureServerService;
import qu4lizz.factoryordersapp.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class OrdersController implements Initializable {
    private SecureServerService secureServerService = new SecureServerService();
    private MessageQueueService messageQueueService = new MessageQueueService();
    @FXML
    private AnchorPane orderPane;

    @FXML
    private TableView<Candy> orderTable;
    @FXML
    private TableColumn<Candy, String> nameTableColumn;
    @FXML
    private TableColumn<Candy, Double> quantityTableColumn;
    @FXML
    private TableColumn<Candy, Double> sumPriceTableColumn;

    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label dateTimeLabel;
    private OrderWrapper currentOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        quantityTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));
        sumPriceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice() * cellData.getValue().getQuantity()));
    }

    @FXML
    void takeNextOrderOnMouseClicked(MouseEvent event) {
        try {
            currentOrder = messageQueueService.takeOrder();
            if (currentOrder != null) {
                double totalPrice = currentOrder.getItems().stream().mapToDouble(x -> x.getPrice() * x.getQuantity()).sum();
                totalPriceLabel.setText("Total price: " + totalPrice);
                dateTimeLabel.setText("Order date and time: " + currentOrder.getOrderDateLocalized());

                orderTable.getItems().clear();
                orderTable.getItems().addAll(currentOrder.getItems());
            }
            else
                PopUpController.showStage("Info", "No order to take");
        } catch (IOException | TimeoutException | JAXBException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
            PopUpController.showStage("Error", e.getMessage());
        }
    }

    private void reinitView() {
        totalPriceLabel.setText("Total price:");
        dateTimeLabel.setText("Order date and time:");

        orderTable.getItems().clear();
    }

    @FXML
    void acceptOnMouseClicked(MouseEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order accepted,");

        refactorMethod(sb);
        try {
            MailService.sendMail(currentOrder.getEmail(), "Order accepted", "Your order has been accepted");
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }

        reinitView();
    }

    @FXML
    void refuseOnMouseClicked(MouseEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order refused,");

        refactorMethod(sb);
        try {
            MailService.sendMail(currentOrder.getEmail(), "Order refused", "Your order has been refused");
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }

        reinitView();
    }

    private void refactorMethod(StringBuilder sb) {
        sb.append("Order processed by:").append(LoginController.user).append(",");
        sb.append("Order date:").append(currentOrder.getOrderDate()).append(",");
        sb.append("Company name:").append(currentOrder.getCompanyName()).append(",,").append("Products:,");

        for (Candy candy : orderTable.getItems())
            sb.append(candy.getName()).append(" ").append(candy.getQuantity()).append(" ").append(candy.getPrice()).append(",");

        try {
            secureServerService.sendOrderInfo(sb.toString());
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
