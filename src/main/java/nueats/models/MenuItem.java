package nueats.models;

import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final StringProperty category;
    private final IntegerProperty stock;
    private final StringProperty imageUrl;
    private final BooleanProperty available;

    public MenuItem(int id, String name, String description, double price, 
                   String category, int stock, String imageUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.stock = new SimpleIntegerProperty(stock);
        this.imageUrl = new SimpleStringProperty(imageUrl);
        this.available = new SimpleBooleanProperty(stock > 0);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Description
    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }
    public StringProperty descriptionProperty() { return description; }

    // Price
    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceProperty() { return price; }

    // Category
    public String getCategory() { return category.get(); }
    public void setCategory(String value) { category.set(value); }
    public StringProperty categoryProperty() { return category; }

    // Stock
    public int getStock() { return stock.get(); }
    public void setStock(int value) { 
        stock.set(value); 
        available.set(value > 0);
    }
    public IntegerProperty stockProperty() { return stock; }

    // Image URL
    public String getImageUrl() { return imageUrl.get(); }
    public void setImageUrl(String value) { imageUrl.set(value); }
    public StringProperty imageUrlProperty() { return imageUrl; }

    // Available
    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean value) { available.set(value); }
    public BooleanProperty availableProperty() { return available; }

    @Override
    public String toString() {
        return name.get() + " - ₹" + price.get();
    }
}
