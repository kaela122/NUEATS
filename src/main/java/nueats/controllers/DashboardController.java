package nueats.controllers;

import java.io.File;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nueats.models.MenuItem;
import nueats.models.Order;
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
    private TableView<MenuItem> menuTable;
    private TableView<Order> orderTable;

    @FXML
    public void initialize() {
        dataManager = DataManager.getInstance();
        
        if (dataManager.getCurrentUser() != null) {
            lblUsername.setText(dataManager.getCurrentUser().getName());
        }
        
        updateStats();
        showDashboardOverview();
    }

    private void updateStats() {
        int itemCount = dataManager.getMenuItems().size();
        int orderCount = dataManager.getOrders().size();
        lblRecordCount.setText("Items: " + itemCount + " | Orders: " + orderCount);
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
        showDashboardOverview();
    }

    @FXML
    private void goToProducts() {
        setActiveButton(btnProducts);
        showProductManagement();
    }

    @FXML
    private void goToAddItem() {
        setActiveButton(btnAddItem);
        showAddItemForm();
    }

    @FXML
    private void goToSearch() {
        setActiveButton(btnSearch);
        showProductManagement();
    }

    @FXML
    private void goToReports() {
        setActiveButton(btnReports);
        showReports();
    }

    private void showDashboardOverview() {
        VBox overview = new VBox(20);
        overview.setPadding(new Insets(20));
        overview.setAlignment(Pos.TOP_LEFT);
        
        Label title = new Label("Dashboard Overview");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        
        VBox itemCard = createStatCard("Total Items", String.valueOf(dataManager.getMenuItems().size()), "#3498db");
        VBox orderCard = createStatCard("Total Orders", String.valueOf(dataManager.getOrders().size()), "#2ecc71");
        VBox revenueCard = createStatCard("Total Revenue", "₱" + String.format("%.2f", calculateTotalRevenue()), "#e74c3c");
        
        statsBox.getChildren().addAll(itemCard, orderCard, revenueCard);
        
        Label actionsLabel = new Label("Quick Actions");
        actionsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        HBox actionsBox = new HBox(15);
        Button btnQuickAdd = new Button("Add New Item");
        btnQuickAdd.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 15 30; -fx-font-size: 14px;");
        btnQuickAdd.setOnAction(e -> goToAddItem());
        
        Button btnViewMenu = new Button("View Menu");
        btnViewMenu.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 15 30; -fx-font-size: 14px;");
        btnViewMenu.setOnAction(e -> SceneManager.switchScene("/nueats/Menu.fxml"));
        
        Button btnViewOrders = new Button("View Orders");
        btnViewOrders.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-padding: 15 30; -fx-font-size: 14px;");
        btnViewOrders.setOnAction(e -> showOrderManagement());
        
        actionsBox.getChildren().addAll(btnQuickAdd, btnViewMenu, btnViewOrders);
        
        overview.getChildren().addAll(title, statsBox, actionsLabel, actionsBox);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(overview);
    }

    private VBox createStatCard(String label, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefWidth(250);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private double calculateTotalRevenue() {
        return dataManager.getOrders().stream().mapToDouble(Order::getTotal).sum();
    }

    private void showProductManagement() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("Product Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(300);
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        refreshBtn.setOnAction(e -> refreshProductTable());
        
        searchBox.getChildren().addAll(searchField, refreshBtn);
        
        menuTable = new TableView<>();
        menuTable.setItems(dataManager.getMenuItems());
        
        TableColumn<MenuItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        
        TableColumn<MenuItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(120);
        
        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);
        
        TableColumn<MenuItem, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(80);
        
        TableColumn<MenuItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(200);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            
            {
                editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                
                editBtn.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    showEditItemDialog(item);
                });
                
                deleteBtn.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    deleteMenuItem(item);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
        
        menuTable.getColumns().addAll(idCol, nameCol, categoryCol, priceCol, stockCol, actionsCol);
        
        container.getChildren().addAll(title, searchBox, menuTable);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
    }

    private void showEditItemDialog(MenuItem item) {
        Dialog<MenuItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Menu Item");
        dialog.setHeaderText("Edit: " + item.getName());
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(item.getName());
        TextField descField = new TextField(item.getDescription());
        TextField priceField = new TextField(String.valueOf(item.getPrice()));
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Food", "Beverages", "Snacks");
        categoryCombo.setValue(item.getCategory());
        TextField stockField = new TextField(String.valueOf(item.getStock()));
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryCombo, 1, 3);
        grid.add(new Label("Stock:"), 0, 4);
        grid.add(stockField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                item.setName(nameField.getText());
                item.setDescription(descField.getText());
                item.setPrice(Double.parseDouble(priceField.getText()));
                item.setCategory(categoryCombo.getValue());
                item.setStock(Integer.parseInt(stockField.getText()));
                return item;
            }
            return null;
        });
        
        Optional<MenuItem> result = dialog.showAndWait();
        result.ifPresent(updatedItem -> {
            dataManager.updateMenuItem(updatedItem);
            menuTable.refresh();
            updateStats();
            showSuccess("Item updated successfully!");
        });
    }

    private void deleteMenuItem(MenuItem item) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Item");
        confirmation.setHeaderText("Delete " + item.getName() + "?");
        confirmation.setContentText("This cannot be undone.");
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataManager.deleteMenuItem(item);
            menuTable.setItems(dataManager.getMenuItems());
            updateStats();
            showSuccess("Item deleted!");
        }
    }

    private void refreshProductTable() {
        menuTable.setItems(dataManager.getMenuItems());
        menuTable.refresh();
        updateStats();
    }

    private void showAddItemForm() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setMaxWidth(600);
        
        Label title = new Label("Add New Menu Item");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        TextField nameField = new TextField();
        nameField.setPromptText("Item name");
        nameField.setPrefWidth(300);
        
        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);
        descField.setPrefWidth(300);
        
        TextField priceField = new TextField();
        priceField.setPromptText("0.00");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Food", "Beverages", "Snacks");
        categoryCombo.setPromptText("Select category");
        categoryCombo.setPrefWidth(300);
        
        TextField stockField = new TextField();
        stockField.setPromptText("0");
        
        TextField imageField = new TextField();
        imageField.setPromptText("/nueats/image/image.png");
        imageField.setText("/nueats/image/image.png");
        
        Button browseBtn = new Button("Browse");
        browseBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(SceneManager.getPrimaryStage());
            if (file != null) {
                imageField.setText(file.getAbsolutePath());
            }
        });
        
        HBox imageBox = new HBox(10, imageField, browseBtn);
        
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Description:"), 0, 1);
        form.add(descField, 1, 1);
        form.add(new Label("Price (₱):"), 0, 2);
        form.add(priceField, 1, 2);
        form.add(new Label("Category:"), 0, 3);
        form.add(categoryCombo, 1, 3);
        form.add(new Label("Stock:"), 0, 4);
        form.add(stockField, 1, 4);
        form.add(new Label("Image:"), 0, 5);
        form.add(imageBox, 1, 5);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveBtn = new Button("Save Item");
        saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14px;");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String category = categoryCombo.getValue();
                int stock = Integer.parseInt(stockField.getText().trim());
                String image = imageField.getText().trim();
                
                if (name.isEmpty() || category == null) {
                    showError("Please fill all required fields!");
                    return;
                }
                
                MenuItem newItem = new MenuItem(0, name, desc, price, category, stock, image);
                dataManager.addMenuItem(newItem);
                
                showSuccess("Item added successfully!");
                nameField.clear();
                descField.clear();
                priceField.clear();
                categoryCombo.setValue(null);
                stockField.clear();
                imageField.setText("/nueats/image/image.png");
                updateStats();
                
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for price and stock!");
            }
        });
        
        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14px;");
        clearBtn.setOnAction(e -> {
            nameField.clear();
            descField.clear();
            priceField.clear();
            categoryCombo.setValue(null);
            stockField.clear();
            imageField.setText("/nueats/image/image.png");
        });
        
        buttonBox.getChildren().addAll(saveBtn, clearBtn);
        
        container.getChildren().addAll(title, form, buttonBox);
        
        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        
        contentArea.getChildren().clear();
        contentArea.getChildren().add(scroll);
    }

    private void showOrderManagement() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("Order Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        orderTable = new TableView<>();
        orderTable.setItems(dataManager.getOrders());
        
        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Order, String> userCol = new TableColumn<>("Customer");
        userCol.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getUser().getName()));
        userCol.setPrefWidth(150);
        
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getFormattedDate()));
        dateCol.setPrefWidth(150);
        
        orderTable.getColumns().addAll(idCol, userCol, totalCol, statusCol, dateCol);
        
        container.getChildren().addAll(title, orderTable);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
    }

    private void showReports() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        
        Label title = new Label("Reports & Analytics");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        GridPane reportGrid = new GridPane();
        reportGrid.setHgap(20);
        reportGrid.setVgap(20);
        reportGrid.setAlignment(Pos.CENTER);
        
        VBox salesCard = createReportCard("Total Sales", "₱" + String.format("%.2f", calculateTotalRevenue()), "#2ecc71");
        VBox ordersCard = createReportCard("Total Orders", String.valueOf(dataManager.getOrders().size()), "#3498db");
        VBox itemsCard = createReportCard("Menu Items", String.valueOf(dataManager.getMenuItems().size()), "#e67e22");
        VBox avgCard = createReportCard("Avg Order", "₱" + String.format("%.2f", calculateAverageOrderValue()), "#9b59b6");
        
        reportGrid.add(salesCard, 0, 0);
        reportGrid.add(ordersCard, 1, 0);
        reportGrid.add(itemsCard, 0, 1);
        reportGrid.add(avgCard, 1, 1);
        
        container.getChildren().addAll(title, reportGrid);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
    }

    private VBox createReportCard(String label, String value, String color) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; -fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;");
        card.setPrefSize(300, 200);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
        
        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private double calculateAverageOrderValue() {
        if (dataManager.getOrders().isEmpty()) return 0.0;
        return calculateTotalRevenue() / dataManager.getOrders().size();
    }

    private void setActiveButton(Button activeButton) {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT;";
        String activeStyle = "-fx-background-color: #4e54c8; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-alignment: CENTER_LEFT;";
        
        btnDashboard.setStyle(defaultStyle);
        btnProducts.setStyle(defaultStyle);
        btnAddItem.setStyle(defaultStyle);
        if (btnSearch != null) btnSearch.setStyle(defaultStyle);
        btnReports.setStyle(defaultStyle);
        
        activeButton.setStyle(activeStyle);
    }

    @FXML
    private void quickAdd() {
        goToAddItem();
    }

    @FXML
    private void viewAll() {
        SceneManager.switchScene("/nueats/Menu.fxml");
    }

    @FXML
    private void exportData() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Data");
        alert.setHeaderText("Data Export");
        alert.setContentText("Exporting " + dataManager.getMenuItems().size() + " items and " + dataManager.getOrders().size() + " orders...");
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
