package qu4lizz.clientapp.gui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import qu4lizz.clientapp.model.Candy;
import qu4lizz.clientapp.service.CandyService;
import qu4lizz.clientapp.model.OrderWrapper;
import qu4lizz.clientapp.service.MessageQueueService;
import qu4lizz.clientapp.utils.ConfigUtil;
import qu4lizz.clientapp.utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class CandyController implements Initializable {
    @FXML
    private TableView<Candy> stockTable;
    @FXML
    private TableColumn<Candy, String> stockNameTableColumn;
    @FXML
    private TableColumn<Candy, Double> stockPriceTableColumn;


    @FXML
    private TableView<Candy> orderedTable;
    @FXML
    private TableColumn<Candy, String> orderedNameTableColumn;
    @FXML
    private TableColumn<Candy, Double> orderedPriceTableColumn;
    @FXML
    private TableColumn<Candy, Double> orderedQuantityTableColumn;

    @FXML
    private TextArea promotionsTextArea;
    @FXML
    private Label sumPriceLabel;
    private final CandyService candyService = new CandyService();
    private final MessageQueueService messageQueueService = new MessageQueueService();
    private double sumPrice = 0;
    public static AtomicBoolean isActive = new AtomicBoolean(true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stockNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        stockPriceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice()));

        orderedNameTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getName()));
        orderedPriceTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPrice() * cellData.getValue().getQuantity()));
        orderedQuantityTableColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getQuantity()));

        stockTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Candy item = stockTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    SingleOrderController.showStage(item, this);
                }
            }
        });

        orderedTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Candy item = orderedTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    fromOrderToStock(item);
                }
            }
        });

        List<Candy> items = null;
        try {
            items = candyService.getItems();
            stockTable.getItems().addAll(items);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, "Could not get items from server");
            PopUpController.showStage("Error", "Could not get items from server");
        }

        getPromotions();
    }

    @FXML
    void createOrderOnMouseClicked(MouseEvent event) {
        List<Candy> items = orderedTable.getItems();
        if (items.isEmpty()) {
            PopUpController.showStage("Error", "Can't send empty order");
            return;
        }

        OrderWrapper wrapper = new OrderWrapper(items, LoginController.user.getAddress(), LoginController.user.getCompanyName());

        try {
            messageQueueService.sendOrder(wrapper);
            PopUpController.showStage("Info", "Order sent");

            items = candyService.getItems();
            stockTable.getItems().clear();
            stockTable.getItems().addAll(items);
            orderedTable.getItems().clear();

            sumPrice = 0;

        } catch (IOException | TimeoutException e) {
            Logger.logger.log(Level.SEVERE, "Order couldn't be created", e.getMessage());
        }
    }

    private void fromOrderToStock(Candy item) {
        Candy stockItem = stockTable.getItems().stream().filter(i -> i.getId() == item.getId()).findFirst().get();
        stockItem.setQuantity(stockItem.getQuantity() + item.getQuantity());
        stockTable.refresh();

        orderedTable.getItems().remove(item);
        sumPrice -= item.getPrice() * item.getQuantity();
        sumPriceLabel.setText("Total price: " + String.format("%.2f",sumPrice));
    }

    void fromStockToOrder(Candy orderItem) {
        if (orderedTable.getItems().contains(orderItem)) {
            Candy item = orderedTable.getItems().stream().filter(i -> i.getId() == orderItem.getId()).findFirst().get();
            item.setQuantity(item.getQuantity() + orderItem.getQuantity());
        } else {
            orderedTable.getItems().add(orderItem);
        }
        orderedTable.refresh();

        sumPrice += orderItem.getPrice() * orderItem.getQuantity();
        sumPriceLabel.setText("Total price: " + String.format("%.2f",sumPrice));
        stockTable.refresh();
    }

    private void getPromotions() {
        Properties properties = ConfigUtil.getProperties();
        String host = properties.getProperty("multicast_ip");
        int port = Integer.parseInt(properties.getProperty("multicast_port"));

        Thread promotionCollector = new Thread(() -> {
            try(MulticastSocket socket = new MulticastSocket(port)) {
                InetAddress inetAddress = InetAddress.getByName(host);
                socket.joinGroup(inetAddress);

                System.out.println("Multicast receiving on " + host + ":" + port);
                while (isActive.get()) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String text = new String(packet.getData(), 0, packet.getLength());
                    Platform.runLater(() -> {
                        promotionsTextArea.setText(text);
                    });
                }
            }
            catch (IOException e) {
                Logger.logger.log(Level.SEVERE, "Multicast not working", e.getMessage());
            }
        });
        promotionCollector.start();
    }
}
