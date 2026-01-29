module nueats {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    opens nueats.main to javafx.fxml;
    opens nueats.controllers to javafx.fxml;
    opens nueats.models to javafx.base;
    
    exports nueats.main;
    exports nueats.controllers;
    exports nueats.models;
    exports nueats.utils;
}
