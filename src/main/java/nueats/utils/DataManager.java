package nueats.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nueats.models.MenuItem;
import nueats.models.Order;
import nueats.models.User;

public class DataManager {
    private static DataManager instance;
    private User currentUser;
    private ObservableList<MenuItem> menuItems;
    private ObservableList<Order> orders;
    private ObservableList<User> users;
    
    // Database connection details - UPDATE THESE WITH YOUR CREDENTIALS
    private static final String DB_URL = "jdbc:mysql://localhost:3306/nueats_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // <<< PUT YOUR MYSQL PASSWORD HERE
    
    private Connection connection;
    
    private DataManager() {
        menuItems = FXCollections.observableArrayList();
        orders = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        
        // Try to connect to database, if fails use in-memory data
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectToDatabase();
            loadDataFromDatabase();
            System.out.println("✓ Connected to database successfully!");
        } catch (Exception e) {
            System.out.println("⚠ Database not available, using in-memory data: " + e.getMessage());
            initializeSampleData();
        }
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    private void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    private void loadDataFromDatabase() {
        try {
            loadMenuItemsFromDB();
            loadUsersFromDB();
            loadOrdersFromDB();
        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
            initializeSampleData();
        }
    }
    
    private void loadMenuItemsFromDB() throws SQLException {
        String query = "SELECT * FROM menu_items";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            MenuItem item = new MenuItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getInt("stock"),
                rs.getString("image_url")
            );
            menuItems.add(item);
        }
        rs.close();
        stmt.close();
    }
    
    private void loadUsersFromDB() throws SQLException {
        String query = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            User user = new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("user_type"),
                rs.getString("phone"),
                rs.getString("student_id"),
                rs.getString("hostel"),
                rs.getString("department")
            );
            users.add(user);
        }
        rs.close();
        stmt.close();
    }
    
    private void loadOrdersFromDB() throws SQLException {
        String query = "SELECT * FROM orders";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            int userId = rs.getInt("user_id");
            User user = users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElse(null);
            
            if (user != null) {
                Order order = new Order(
                    rs.getInt("id"),
                    user,
                    FXCollections.observableArrayList(),
                    rs.getDouble("total")
                );
                order.setStatus(rs.getString("status"));
                order.setPaymentMethod(rs.getString("payment_method"));
                orders.add(order);
            }
        }
        rs.close();
        stmt.close();
    }
    
    private void initializeSampleData() {
        System.out.println("Loading sample data...");
        
        // Default users
        users.add(new User(1, "Admin", "admin@nueats.com", "admin123", "Admin"));
        users.add(new User(2, "John Doe", "john@nueats.com", "password", "Student", 
                          "1234567890", "S12345", "Hostel A", "Computer Science"));
        
        // Menu items - Paths verified from C:\Users\judep\Documents\TaraNa\NUEATS\src\main\resources\nueats\image
        // Available: Burger.png, Coke.png, Sprite.jpg, Water.png, Pancitcanton.png, image.png, ic_cart.png, ic_delivery.png
        
        try {
            menuItems.add(new MenuItem(1, "Classic Burger", 
                "Delicious beef burger with fresh vegetables and special sauce", 
                80.0, "Food", 25, 
                getClass().getResource("/nueats/image/Burger.png").toExternalForm()));
                
            menuItems.add(new MenuItem(2, "Coca Cola", 
                "Refreshing cold Coca Cola beverage", 
                30.0, "Beverages", 50, 
                getClass().getResource("/nueats/image/Coke.png").toExternalForm()));
                
            menuItems.add(new MenuItem(3, "Sprite", 
                "Lemon-lime flavored refreshing drink", 
                30.0, "Beverages", 45, 
                getClass().getResource("/nueats/image/Sprite.jpg").toExternalForm()));
                
            menuItems.add(new MenuItem(4, "Mineral Water", 
                "Pure drinking water 500ml", 
                20.0, "Beverages", 100, 
                getClass().getResource("/nueats/image/Water.png").toExternalForm()));
                
            menuItems.add(new MenuItem(5, "Pancit Canton", 
                "Filipino stir-fried noodles with vegetables", 
                60.0, "Food", 30, 
                getClass().getResource("/nueats/image/Pancitcanton.png").toExternalForm()));
                
            menuItems.add(new MenuItem(6, "Hotdog", 
                "Best selling hotdog with ketchup and mustard", 
                120.0, "Food", 20, 
                getClass().getResource("/nueats/image/hotdog.png").toExternalForm()));
                
            menuItems.add(new MenuItem(7, "Sharwarma", 
                "savory shawarma wrap with garlic sauce", 
                50.0, "Snacks", 40, 
                getClass().getResource("/nueats/image/Shawarma.png").toExternalForm()));
                
            menuItems.add(new MenuItem(8, "Carbonara", 
                "Carbonara pasta with creamy sauce", 
                90.0, "Food", 15, 
                getClass().getResource("/nueats/image/Carbonara.png").toExternalForm()));
                
            menuItems.add(new MenuItem(9, "Pesto Pasta", 
                "Pesto pasta with fresh basil sauce", 
                70.0, "Food", 18, 
                getClass().getResource("/nueats/image/Pesto.png").toExternalForm()));
                
            menuItems.add(new MenuItem(10, "Taco", 
                "Mexican-style taco with seasoned meat and vegetables", 
                40.0, "Snacks", 35, 
                getClass().getResource("/nueats/image/taco.jpg").toExternalForm()));
                
            System.out.println("✓ Loaded " + menuItems.size() + " menu items with images");
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    
    public User authenticateUser(String email, String password) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
    
    public boolean userExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equals(email));
    }
    
    public void addUser(User user) {
        user.setId(users.stream().mapToInt(User::getId).max().orElse(0) + 1);
        users.add(user);
        if (connection != null) {
            try {
                saveUserToDB(user);
            } catch (SQLException e) {
                System.out.println("Error saving user: " + e.getMessage());
            }
        }
    }
    
    private void saveUserToDB(User user) throws SQLException {
        String query = "INSERT INTO users (id, name, email, password, user_type, phone, student_id, hostel, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getPassword());
        pstmt.setString(5, user.getUserType());
        pstmt.setString(6, user.getPhone());
        pstmt.setString(7, user.getStudentId());
        pstmt.setString(8, user.getHostel());
        pstmt.setString(9, user.getDepartment());
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public ObservableList<MenuItem> getMenuItems() { return menuItems; }
    
    public void addMenuItem(MenuItem item) {
        item.setId(menuItems.stream().mapToInt(MenuItem::getId).max().orElse(0) + 1);
        menuItems.add(item);
        if (connection != null) {
            try {
                saveMenuItemToDB(item);
            } catch (SQLException e) {
                System.out.println("Error saving item: " + e.getMessage());
            }
        }
    }
    
    public void updateMenuItem(MenuItem item) {
        if (connection != null) {
            try {
                updateMenuItemInDB(item);
            } catch (SQLException e) {
                System.out.println("Error updating item: " + e.getMessage());
            }
        }
    }
    
    public void deleteMenuItem(MenuItem item) {
        menuItems.remove(item);
        if (connection != null) {
            try {
                deleteMenuItemFromDB(item.getId());
            } catch (SQLException e) {
                System.out.println("Error deleting item: " + e.getMessage());
            }
        }
    }
    
    private void saveMenuItemToDB(MenuItem item) throws SQLException {
        String query = "INSERT INTO menu_items (id, name, description, price, category, stock, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, item.getId());
        pstmt.setString(2, item.getName());
        pstmt.setString(3, item.getDescription());
        pstmt.setDouble(4, item.getPrice());
        pstmt.setString(5, item.getCategory());
        pstmt.setInt(6, item.getStock());
        pstmt.setString(7, item.getImageUrl());
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    private void updateMenuItemInDB(MenuItem item) throws SQLException {
        String query = "UPDATE menu_items SET name=?, description=?, price=?, category=?, stock=?, image_url=? WHERE id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, item.getName());
        pstmt.setString(2, item.getDescription());
        pstmt.setDouble(3, item.getPrice());
        pstmt.setString(4, item.getCategory());
        pstmt.setInt(5, item.getStock());
        pstmt.setString(6, item.getImageUrl());
        pstmt.setInt(7, item.getId());
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    private void deleteMenuItemFromDB(int id) throws SQLException {
        String query = "DELETE FROM menu_items WHERE id=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public ObservableList<Order> getOrders() { return orders; }
    
    public ObservableList<Order> getOrdersByUser(User user) {
        List<Order> userOrders = orders.stream()
                .filter(o -> o.getUser().getId() == user.getId())
                .toList();
        return FXCollections.observableArrayList(userOrders);
    }
    
    public void addOrder(Order order) {
        orders.add(order);
        if (connection != null) {
            try {
                saveOrderToDB(order);
            } catch (SQLException e) {
                System.out.println("Error saving order: " + e.getMessage());
            }
        }
    }
    
    private void saveOrderToDB(Order order) throws SQLException {
        String query = "INSERT INTO orders (id, user_id, total, status, payment_method, order_date) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, order.getId());
        pstmt.setInt(2, order.getUser().getId());
        pstmt.setDouble(3, order.getTotal());
        pstmt.setString(4, order.getStatus());
        pstmt.setString(5, order.getPaymentMethod());
        pstmt.setTimestamp(6, Timestamp.valueOf(order.getOrderDate()));
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public int getNextOrderId() {
        return orders.stream().mapToInt(Order::getId).max().orElse(0) + 1;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
