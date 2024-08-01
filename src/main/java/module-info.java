module com.ahocorasicktextfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.ahocorasicktextfinder.Classes to com.google.gson;
    opens com.ahocorasicktextfinder to javafx.fxml;
    exports com.ahocorasicktextfinder;
    exports com.ahocorasicktextfinder.Controller;
    opens com.ahocorasicktextfinder.Controller to javafx.fxml;
}