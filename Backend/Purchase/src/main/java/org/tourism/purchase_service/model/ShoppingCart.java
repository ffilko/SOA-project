package org.tourism.purchase_service.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String touristId;

    private double totalPrice;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public ShoppingCart() {}

    public ShoppingCart(String touristId) {
        this.touristId = touristId;
        this.totalPrice = 0.0;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setShoppingCart(this);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setShoppingCart(null);
        recalculateTotal();
    }

    public void clearCart() {
        items.clear();
        this.totalPrice = 0.0;
    }

    private void recalculateTotal() {
        this.totalPrice = items.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    public double getTotalPrice() { return totalPrice; }
    public List<OrderItem> getItems() { return items; }
}