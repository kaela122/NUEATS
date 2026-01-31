package nueats.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nueats.models.CartItem;
import nueats.models.Order;
import nueats.utils.DataManager;
import nueats.utils.SceneManager;

public class MenuController {

    @FXML private TextField searchField;
    @FXML private FlowPane menuItemsContainer;
    @FXML private VBox cartItemsContainer;
    @FXML private Label cartCountLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;
    @FXML private Label maxPriceLabel;
    @FXML private Slider priceSlider;
    @FXML private CheckBox inStockCheckBox;
    @FXML private CheckBox lowStockCheckBox;
    @FXML private Button checkoutButton;

    private DataManager dataManager;
    private ObservableList<nueats.models.MenuItem> allMenuItems;
    private ObservableList<nueats.models.MenuItem> filteredMenuItems;
    private ObservableList<CartItem> cartItems;
    private String currentCategory = "All";
    private String currentSort = "name";

    @FXML
    public void initialize() {
        dataManager = DataManager.getInstance();
        allMenuItems = dataManager.getMenuItems();
        filteredMenuItems = FXCollections.observableArrayList(allMenuItems);
        cartItems = FXCollections.observableArrayList();

        // Setup price slider
        if (priceSlider != null) {
            priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                maxPriceLabel.setText(String.format("%.0f", newVal.doubleValue()));
            });
        }

        loadMenuItems();
        updateCartDisplay();
        
        // Show welcome message for guest users
        if (dataManager.getCurrentUser() != null && 
            dataManager.getCurrentUser().getName().equals("Guest User")) {
            showGuestWelcome();
        }
    }
    
    private void showGuestWelcome() {
        Alert welcome = new Alert(Alert.AlertType.INFORMATION);
        welcome.setTitle("Welcome Guest!");
        welcome.setHeaderText("🎉 Welcome to NUEats!");
        welcome.setContentText("You're browsing as a guest. You can:\n" +
                              "• Browse all menu items\n" +
                              "• Add items to cart\n" +
                              "• Place orders\n\n" +
                              "Create an account to track your order history!");
        welcome.showAndWait();
    }

    private void loadMenuItems() {
        menuItemsContainer.getChildren().clear();
        
        for (nueats.models.MenuItem item : filteredMenuItems) {
            VBox itemCard = createMenuItemCard(item);
            menuItemsContainer.getChildren().add(itemCard);
        }

        itemCountLabel.setText("(" + filteredMenuItems.size() + " items)");
    }

    private VBox createMenuItemCard(nueats.models.MenuItem item) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefSize(250, 350);
        card.setMaxSize(250, 350);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                     "-fx-padding: 15;");

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);
        try {
            imageView.setImage(new Image(item.getImageUrl(), true));
        } catch (Exception e) {
            // Use placeholder if image fails to load
            imageView.setStyle("-fx-background-color: #e0e0e0;");
        }

        // Name
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(220);

        // Description
        Label descLabel = new Label(item.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(220);
        descLabel.setMaxHeight(40);

        // Price and Stock
        HBox priceStockBox = new HBox(10);
        priceStockBox.setAlignment(Pos.CENTER_LEFT);
        
        Label priceLabel = new Label("₱" + String.format("%.0f", item.getPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        
        Label stockLabel = new Label(item.getStock() + " left");
        stockLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + 
                           (item.getStock() > 10 ? "#27ae60" : "#e74c3c") + ";");
        
        priceStockBox.getChildren().addAll(priceLabel, new Region(), stockLabel);
        HBox.setHgrow(priceStockBox.getChildren().get(1), Priority.ALWAYS);

        // Add to Cart Button
        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                          "-fx-font-weight: bold; -fx-padding: 10 20; " +
                          "-fx-background-radius: 5; -fx-cursor: hand;");
        addButton.setPrefWidth(220);
        addButton.setOnAction(e -> addToCart(item));

        if (!item.isAvailable()) {
            addButton.setDisable(true);
            addButton.setText("Out of Stock");
            addButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        }

        card.getChildren().addAll(imageView, nameLabel, descLabel, priceStockBox, addButton);
        
        return card;
    }

    private void addToCart(nueats.models.MenuItem item) {
        // Check if item already in cart
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(ci -> ci.getMenuItem().getId() == item.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().incrementQuantity();
        } else {
            cartItems.add(new CartItem(item, 1));
        }

        updateCartDisplay();
    }

    private void updateCartDisplay() {
        cartItemsContainer.getChildren().clear();

        if (cartItems.isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic; -fx-padding: 20;");
            cartItemsContainer.getChildren().add(emptyLabel);
            checkoutButton.setDisable(true);
        } else {
            for (CartItem cartItem : cartItems) {
                VBox itemBox = createCartItemBox(cartItem);
                cartItemsContainer.getChildren().add(itemBox);
            }
            checkoutButton.setDisable(false);
        }

        // Update totals
        double subtotal = cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        double tax = subtotal * 0.05; // 5% tax
        double total = subtotal + tax;

        cartCountLabel.setText(String.valueOf(cartItems.size()));
        subtotalLabel.setText("₱" + String.format("%.2f", subtotal));
        taxLabel.setText("₱" + String.format("%.2f", tax));
        totalLabel.setText("₱" + String.format("%.2f", total));
    }

    private VBox createCartItemBox(CartItem cartItem) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-background-radius: 5;");

        Label nameLabel = new Label(cartItem.getMenuItem().getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        HBox quantityBox = new HBox(5);
        quantityBox.setAlignment(Pos.CENTER_LEFT);

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        minusBtn.setOnAction(e -> {
            cartItem.decrementQuantity();
            updateCartDisplay();
        });

        Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
        quantityLabel.setStyle("-fx-min-width: 30; -fx-alignment: center; -fx-font-weight: bold;");

        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        plusBtn.setOnAction(e -> {
            if (cartItem.getQuantity() < cartItem.getMenuItem().getStock()) {
                cartItem.incrementQuantity();
                updateCartDisplay();
            }
        });

        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;");
        removeBtn.setOnAction(e -> {
            cartItems.remove(cartItem);
            updateCartDisplay();
        });

        Label priceLabel = new Label("₱" + String.format("%.2f", cartItem.getSubtotal()));
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        quantityBox.getChildren().addAll(minusBtn, quantityLabel, plusBtn, new Region(), priceLabel, removeBtn);
        HBox.setHgrow(quantityBox.getChildren().get(3), Priority.ALWAYS);

        box.getChildren().addAll(nameLabel, quantityBox);
        return box;
    }

    @FXML
    private void handleHome(ActionEvent event) {
        currentCategory = "All";
        applyFilters();
    }

    @FXML
    private void handleMyOrders(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("My Orders");
        alert.setHeaderText("Order History");
        
        ObservableList<Order> userOrders = dataManager.getOrdersByUser(dataManager.getCurrentUser());
        
        if (userOrders.isEmpty()) {
            alert.setContentText("You haven't placed any orders yet.");
        } else {
            StringBuilder orderText = new StringBuilder();
            for (Order order : userOrders) {
                orderText.append("Order #").append(order.getId())
                         .append(" - ₱").append(String.format("%.2f", order.getTotal()))
                         .append(" - ").append(order.getStatus())
                         .append("\n");
            }
            alert.setContentText(orderText.toString());
        }
        alert.showAndWait();
    }

    @FXML
    private void handleAdmin(ActionEvent event) {
        if (dataManager.getCurrentUser() != null && 
            dataManager.getCurrentUser().getUserType().equals("Admin")) {
            SceneManager.switchScene("/nueats/DashboardView.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Admin Access Required");
            alert.setContentText("You need admin privileges to access this section.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        dataManager.setCurrentUser(null);
        SceneManager.switchScene("/nueats/LoginView.fxml");
    }

    @FXML
    private void handleSearch() {
        applyFilters();
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void handleSortByPrice(ActionEvent event) {
        currentSort = "price";
        applyFilters();
    }

    @FXML
    private void handleSortByName(ActionEvent event) {
        currentSort = "name";
        applyFilters();
    }

    @FXML
    private void showAllItems(ActionEvent event) {
        currentCategory = "All";
        applyFilters();
    }

    @FXML
    private void showFoodItems(ActionEvent event) {
        currentCategory = "Food";
        applyFilters();
    }

    @FXML
    private void showBeverageItems(ActionEvent event) {
        currentCategory = "Beverages";
        applyFilters();
    }

    @FXML
    private void showSnackItems(ActionEvent event) {
        currentCategory = "Snacks";
        applyFilters();
    }

    @FXML
    private void handlePriceFilter() {
        applyFilters();
    }

    @FXML
    private void handleAvailabilityFilter(ActionEvent event) {
        applyFilters();
    }

    private void applyFilters() {
        filteredMenuItems.clear();
        
        String searchText = searchField != null ? searchField.getText().toLowerCase() : "";
        double maxPrice = priceSlider != null ? priceSlider.getValue() : 200.0;
        boolean showInStock = inStockCheckBox != null ? inStockCheckBox.isSelected() : true;
        boolean showLowStock = lowStockCheckBox != null ? lowStockCheckBox.isSelected() : false;

        filteredMenuItems.addAll(allMenuItems.stream()
                .filter(item -> currentCategory.equals("All") || item.getCategory().equals(currentCategory))
                .filter(item -> item.getName().toLowerCase().contains(searchText) || 
                               item.getDescription().toLowerCase().contains(searchText))
                .filter(item -> item.getPrice() <= maxPrice)
                .filter(item -> {
                    if (showInStock && !showLowStock) return item.getStock() > 10;
                    if (!showInStock && showLowStock) return item.getStock() <= 10 && item.getStock() > 0;
                    if (showInStock && showLowStock) return item.getStock() > 0;
                    return true;
                })
                .sorted((a, b) -> {
                    if (currentSort.equals("price")) {
                        return Double.compare(a.getPrice(), b.getPrice());
                    } else {
                        return a.getName().compareTo(b.getName());
                    }
                })
                .collect(Collectors.toList()));

        loadMenuItems();
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (cartItems.isEmpty()) {
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Order");
        confirmation.setHeaderText("Place Your Order");
        
        double total = Double.parseDouble(totalLabel.getText().replace("₱", ""));
        confirmation.setContentText("Total Amount: ₱" + String.format("%.2f", total) + 
                                   "\n\nProceed with order?");

        Optional<ButtonType> result = confirmation.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Create order
            Order order = new Order(
                dataManager.getNextOrderId(),
                dataManager.getCurrentUser(),
                FXCollections.observableArrayList(cartItems),
                total
            );
            order.setStatus("Confirmed");
            dataManager.addOrder(order);

            // Clear cart
            cartItems.clear();
            updateCartDisplay();

            // Show success
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Order Placed");
            success.setHeaderText("Order Successful!");
            success.setContentText("Your order #" + order.getId() + " has been placed successfully!\n" +
                                 "Total: ₱" + String.format("%.2f", total));
            success.showAndWait();
        }
    }

    @FXML
    private void handleClearCart(ActionEvent event) {
        if (!cartItems.isEmpty()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Clear Cart");
            confirmation.setHeaderText("Clear All Items?");
            confirmation.setContentText("Are you sure you want to remove all items from your cart?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cartItems.clear();
                updateCartDisplay();
            }
        }
    }
}
