package org.tourism.purchase_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tourism.purchase_service.model.TourPurchaseToken;
import java.util.UUID;

public interface TourPurchaseTokenRepository extends JpaRepository<TourPurchaseToken, UUID> {
    boolean existsByTouristIdAndTourId(String touristId, String tourId);
}