module qu4lizz.distributorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.logging;


    opens qu4lizz.distributorapp to javafx.fxml;
    exports qu4lizz.distributorapp;
    exports qu4lizz.distributorapp.gui;
    exports rmi;
    opens qu4lizz.distributorapp.gui to javafx.fxml;
}