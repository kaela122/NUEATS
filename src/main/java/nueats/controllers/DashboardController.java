package nueats.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import nueats.utils.DataManager;
import nueats.utils.SceneManager;

public class DashboardController {

    @FXML private Label lblUsername;
    @FXML private Label lblStatus;
    @FXML private Label lblRecordCount;
    @FXML private Label lblLastUpdate;
    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnProducts;
    @FXML private Button btnAddItem;
    @FXML private Button btnSearch;
    @FXML private Button btnReports;

    private DataManager dataManager;

    @FXML
    public void initialize() {
        dataManager = DataManager.getInstance();
        
        if (dataManager.getCurrentUser() != null) {
            lblUsername.setText(dataManager.getCurrentUser().getName());
        }
        
        updateStats();
    }

    private void updateStats() {
        int itemCount = dataManager.getMenuItems().size();
        lblRecordCount.setText("Total Records: " + itemCount);
        lblStatus.setText("System Ready");
        lblLastUpdate.setText("Last Update: Just now");
    }

    @FXML
    private void handleLogout() {
        dataManager.setCurrentUser(null);
        SceneManager.switchScene("/nueats/LoginView.fxml");
    }

    @FXML
    private void goToDashboard() {
        setActiveButton(btnDashboard);
        updateStats();
    }

    @FXML
    private void goToProducts() {
        setActiveButton(btnProducts);
        showInfo("Product Management", "View and manage all menu items");
    }

    @FXML
    private void goToAddItem() {
        setActiveButton(btnAddItem);
        showInfo("Add New Product", "Add new items to the menu");
    }

    @FXML
    private void goToSearch() {
        setActiveButton(btnSearch);
        showInfo("Advanced Search", "Search and filter menu items");
    }

    @FXML
    private void goToReports() {
        setActiveButton(btnReports);
        showInfo("Reports & Analytics", "View sales reports and analytics");
    }

    @FXML
    private void quickAdd() {
        showInfo("Quick Add", "Quickly add a new menu item");
    }

    @FXML
    private void viewAll() {
        // Navigate to menu view
        SceneManager.switchScene("/nueats/Menu.fxml");
    }

    @FXML
    private void exportData() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Data");
        alert.setHeaderText("Data Export");
        alert.setContentText("Exporting " + dataManager.getMenuItems().size() + " items...");
        alert.showAndWait();
    }

    private void setActiveButton(Button activeButton) {
        // Reset all buttons
        btnDashboard.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT; -fx-border-color: transparent;");
        btnProducts.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT; -fx-border-color: transparent;");
        btnAddItem.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT; -fx-border-color: transparent;");
        btnSearch.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT; -fx-border-color: transparent;");
        btnReports.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT; -fx-border-color: transparent;");
        
        // Set active button style
        activeButton.setStyle("-fx-background-color: #4e54c8; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT;");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
