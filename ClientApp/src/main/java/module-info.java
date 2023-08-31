module qu4lizz.clientapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jakarta.xml.bind;
    requires com.rabbitmq.client;
    requires org.slf4j;
    requires java.logging;

    opens qu4lizz.clientapp.service to org.slf4j;
    opens qu4lizz.clientapp to javafx.fxml, com.google.gson, jakarta.xml.bind;
    opens qu4lizz.clientapp.model to jakarta.xml.bind, org.glassfish.jaxb.runtime, com.google.gson;
    exports qu4lizz.clientapp;
    exports qu4lizz.clientapp.gui;
    exports qu4lizz.clientapp.model;
    opens qu4lizz.clientapp.gui to javafx.fxml;
}