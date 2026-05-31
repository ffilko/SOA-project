package org.tourism.purchase_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TourPurchaseToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String touristId;
    private String tourId;
    private LocalDateTime purchaseDate;

    public TourPurchaseToken() {}

    public TourPurchaseToken(String touristId, String tourId) {
        this.touristId = touristId;
        this.tourId = tourId;
        this.purchaseDate = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getTouristId() { return touristId; }
    public String getTourId() { return tourId; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
}