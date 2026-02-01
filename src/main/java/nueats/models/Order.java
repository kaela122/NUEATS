package nueats.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private final IntegerProperty id;
    private final ObjectProperty<User> user;
    private final ObservableList<CartItem> items;
    private final DoubleProperty total;
    private final StringProperty status;
    private final ObjectProperty<LocalDateTime> orderDate;
    private final StringProperty paymentMethod;

    public Order(int id, User user, ObservableList<CartItem> items, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.user = new SimpleObjectProperty<>(user);
        this.items = FXCollections.observableArrayList(items);
        this.total = new SimpleDoubleProperty(total);
        this.status = new SimpleStringProperty("Pending");
        this.orderDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.paymentMethod = new SimpleStringProperty("Cash");
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public User getUser() { return user.get(); }
    public void setUser(User value) { user.set(value); }
    public ObjectProperty<User> userProperty() { return user; }

    public ObservableList<CartItem> getItems() { return items; }

    public double getTotal() { return total.get(); }
    public void setTotal(double value) { total.set(value); }
    public DoubleProperty totalProperty() { return total; }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }

    public LocalDateTime getOrderDate() { return orderDate.get(); }
    public void setOrderDate(LocalDateTime value) { orderDate.set(value); }
    public ObjectProperty<LocalDateTime> orderDateProperty() { return orderDate; }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String value) { paymentMethod.set(value); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return orderDate.get().format(formatter);
    }
}
