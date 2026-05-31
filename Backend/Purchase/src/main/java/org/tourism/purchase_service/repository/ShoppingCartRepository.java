package org.tourism.purchase_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tourism.purchase_service.model.ShoppingCart;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
    Optional<ShoppingCart> findByTouristId(String touristId);
}