package nueats.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nueats.models.*;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private ObservableList<MenuItem> menuItems;
    private ObservableList<User> users;
    private ObservableList<Order> orders;
    private User currentUser;
    private int nextOrderId = 1;
    private int nextUserId = 1;

    private DataManager() {
        menuItems = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        orders = FXCollections.observableArrayList();
        initializeSampleData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Add sample menu items
        menuItems.add(new MenuItem(1, "Chicken Biryani", "Aromatic basmati rice with tender chicken pieces", 120.0, "Food", 25, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8"));
        menuItems.add(new MenuItem(2, "Vegetable Sandwich", "Fresh vegetables with cheese and sauces", 50.0, "Food", 30, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af"));
        menuItems.add(new MenuItem(3, "Masala Dosa", "Crispy dosa with spiced potato filling", 60.0, "Food", 20, "https://images.unsplash.com/photo-1630383249896-424e482df921"));
        menuItems.add(new MenuItem(4, "Paneer Tikka", "Grilled cottage cheese with Indian spices", 90.0, "Food", 15, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8"));
        menuItems.add(new MenuItem(5, "Chicken Burger", "Juicy chicken patty with fresh vegetables", 80.0, "Food", 22, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd"));
        menuItems.add(new MenuItem(6, "Veg Pulao", "Fragrant rice cooked with mixed vegetables", 70.0, "Food", 18, "https://images.unsplash.com/photo-1596560548464-f010549b84d7"));
        
        menuItems.add(new MenuItem(7, "Coffee", "Freshly brewed hot coffee", 30.0, "Beverages", 50, "https://images.unsplash.com/photo-1498804103079-a6351b050096"));
        menuItems.add(new MenuItem(8, "Mango Juice", "Fresh mango juice", 40.0, "Beverages", 35, "https://images.unsplash.com/photo-1600271886742-f049cd451bba"));
        menuItems.add(new MenuItem(9, "Lemon Tea", "Refreshing lemon-flavored tea", 25.0, "Beverages", 40, "https://images.unsplash.com/photo-1556679343-c7306c1976bc"));
        menuItems.add(new MenuItem(10, "Cold Coffee", "Chilled coffee with ice cream", 50.0, "Beverages", 28, "https://images.unsplash.com/photo-1461023058943-07fcbe16d735"));
        menuItems.add(new MenuItem(11, "Lassi", "Traditional Indian yogurt drink", 35.0, "Beverages", 32, "https://images.unsplash.com/photo-1623065422902-30a2d299bbe4"));
        
        menuItems.add(new MenuItem(12, "Samosa", "Crispy fried pastry with spiced filling", 20.0, "Snacks", 45, "https://images.unsplash.com/photo-1601050690597-df0568f70950"));
        menuItems.add(new MenuItem(13, "Pakora", "Deep-fried vegetable fritters", 30.0, "Snacks", 38, "https://images.unsplash.com/photo-1606491956689-2ea866880c84"));
        menuItems.add(new MenuItem(14, "French Fries", "Crispy golden potato fries", 40.0, "Snacks", 42, "https://images.unsplash.com/photo-1573080496219-bb080dd4f877"));
        menuItems.add(new MenuItem(15, "Nachos", "Tortilla chips with cheese dip", 55.0, "Snacks", 26, "https://images.unsplash.com/photo-1582169296194-e4d644c48063"));
        menuItems.add(new MenuItem(16, "Spring Rolls", "Crispy vegetable spring rolls", 45.0, "Snacks", 30, "https://images.unsplash.com/photo-1593504049359-74330189a345"));
        
        menuItems.add(new MenuItem(17, "Gulab Jamun", "Sweet dumplings in sugar syrup", 35.0, "Desserts", 28, "https://images.unsplash.com/photo-1589567357818-a5bb11b3e41a"));
        menuItems.add(new MenuItem(18, "Ice Cream", "Assorted flavors of ice cream", 40.0, "Desserts", 35, "https://images.unsplash.com/photo-1563805042-7684c019e1cb"));
        menuItems.add(new MenuItem(19, "Kheer", "Traditional Indian rice pudding", 30.0, "Desserts", 20, "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d"));
        menuItems.add(new MenuItem(20, "Cake Slice", "Assorted cake varieties", 50.0, "Desserts", 15, "https://images.unsplash.com/photo-1578985545062-69928b1d9587"));

        // Add sample users
        users.add(new User(nextUserId++, "Admin User", "admin@nueats.com", "admin123", "Admin", 
                          "+63 123 456 7890", "ADMIN001", "", "Administration"));
        users.add(new User(nextUserId++, "John Doe", "john@student.edu", "student123", "Student",
                          "+63 987 654 3210", "S2024001", "Hostel A, Room 101", "Computer Science"));
        users.add(new User(nextUserId++, "Dr. Smith", "smith@faculty.edu", "faculty123", "Faculty",
                          "+63 111 222 3333", "F2024001", "", "Engineering"));
    }

    // Menu Items
    public ObservableList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public void updateMenuItem(MenuItem item) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getId() == item.getId()) {
                menuItems.set(i, item);
                break;
            }
        }
    }

    public void deleteMenuItem(MenuItem item) {
        menuItems.remove(item);
    }

    public MenuItem getMenuItemById(int id) {
        return menuItems.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Users
    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        user.setId(nextUserId++);
        users.add(user);
    }

    public User authenticateUser(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean userExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    // Orders
    public ObservableList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        order.setId(nextOrderId++);
        orders.add(order);
    }

    public ObservableList<Order> getOrdersByUser(User user) {
        List<Order> userOrders = orders.stream()
                .filter(order -> order.getUser().getId() == user.getId())
                .toList();
        return FXCollections.observableArrayList(userOrders);
    }

    public int getNextOrderId() {
        return nextOrderId;
    }
}
