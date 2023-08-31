module qu4lizz.factoryordersapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.rabbitmq.client;
    requires jakarta.xml.bind;
    requires jakarta.mail;
    requires org.slf4j;

    opens qu4lizz.factoryordersapp to javafx.fxml;
    exports qu4lizz.factoryordersapp;
    exports qu4lizz.factoryordersapp.gui;
    exports qu4lizz.factoryordersapp.service;
    exports qu4lizz.factoryordersapp.model;
    opens qu4lizz.factoryordersapp.gui to javafx.fxml;
    opens qu4lizz.factoryordersapp.model to jakarta.xml.bind;
    opens qu4lizz.factoryordersapp.service to jakarta.mail;
}