module nueats {
    requires javafx.controls;
    requires javafx.fxml;

    opens nueats to javafx.fxml;
    exports nueats;
}
