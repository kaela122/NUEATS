package nueats.models;

import javafx.beans.property.*;

public class CartItem {
    private final ObjectProperty<MenuItem> menuItem;
    private final IntegerProperty quantity;
    private final DoubleProperty subtotal;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = new SimpleObjectProperty<>(menuItem);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.subtotal = new SimpleDoubleProperty(menuItem.getPrice() * quantity);
        
        // Update subtotal when quantity changes
        this.quantity.addListener((obs, oldVal, newVal) -> {
            updateSubtotal();
        });
    }

    private void updateSubtotal() {
        subtotal.set(menuItem.get().getPrice() * quantity.get());
    }

    // MenuItem
    public MenuItem getMenuItem() { return menuItem.get(); }
    public void setMenuItem(MenuItem value) { menuItem.set(value); }
    public ObjectProperty<MenuItem> menuItemProperty() { return menuItem; }

    // Quantity
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int value) { quantity.set(value); }
    public IntegerProperty quantityProperty() { return quantity; }

    // Subtotal
    public double getSubtotal() { return subtotal.get(); }
    public DoubleProperty subtotalProperty() { return subtotal; }

    public void incrementQuantity() {
        setQuantity(getQuantity() + 1);
    }

    public void decrementQuantity() {
        if (getQuantity() > 1) {
            setQuantity(getQuantity() - 1);
        }
    }
}
