package org.tourism.purchase_service.dto;

import java.util.UUID;

public class OrderItemDTO {

    private UUID id;
    private String tourId;
    private String tourName;
    private double price;

    public OrderItemDTO() {}

    public OrderItemDTO(UUID id, String tourId, String tourName, double price) {
        this.id = id;
        this.tourId = tourId;
        this.tourName = tourName;
        this.price = price;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }
    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}