package org.tourism.purchase_service.dto;

import java.util.List;
import java.util.UUID;

public class ShoppingCartDTO {

    private UUID id;
    private String touristId;
    private double totalPrice;
    private List<OrderItemDTO> items;

    public ShoppingCartDTO() {}

    public ShoppingCartDTO(UUID id, String touristId, double totalPrice, List<OrderItemDTO> items) {
        this.id = id;
        this.touristId = touristId;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}