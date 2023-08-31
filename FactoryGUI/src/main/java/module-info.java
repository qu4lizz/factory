module qu4lizz.factoryapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jakarta.xml.bind;
    requires redis.clients.jedis;
    requires org.slf4j;
    requires java.rmi;
    requires java.logging;


    opens qu4lizz.factoryapp to javafx.fxml, com.google.gson, jakarta.xml.bind;
    opens qu4lizz.factoryapp.model to jakarta.xml.bind, org.glassfish.jaxb.runtime, com.google.gson;
    exports qu4lizz.factoryapp;
    exports qu4lizz.factoryapp.gui;
    exports qu4lizz.factoryapp.model;
    opens qu4lizz.factoryapp.gui to javafx.fxml;
    exports rmi;
    opens rmi to com.google.gson, jakarta.xml.bind, org.glassfish.jaxb.runtime;
}