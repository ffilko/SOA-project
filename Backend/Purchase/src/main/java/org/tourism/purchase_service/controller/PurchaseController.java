package org.tourism.purchase_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourism.purchase_service.dto.OrderItemDTO;
import org.tourism.purchase_service.dto.ShoppingCartDTO;
import org.tourism.purchase_service.service.PurchaseService;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/cart/{touristId}")
    public ResponseEntity<ShoppingCartDTO> getCart(@PathVariable String touristId) {
        return ResponseEntity.ok(purchaseService.getCart(touristId));
    }

    @PostMapping("/cart/{touristId}/items")
    public ResponseEntity<ShoppingCartDTO> addItem(@PathVariable String touristId, @RequestBody OrderItemDTO itemDTO) {
        try {
            return ResponseEntity.ok(purchaseService.addItemToCart(touristId, itemDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/cart/{touristId}/items/{itemId}")
    public ResponseEntity<ShoppingCartDTO> removeItem(@PathVariable String touristId, @PathVariable UUID itemId) {
        return ResponseEntity.ok(purchaseService.removeItemFromCart(touristId, itemId));
    }

    @PostMapping("/cart/{touristId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable String touristId) {
        try {
            purchaseService.checkout(touristId);
            return ResponseEntity.ok("Ok");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check/{touristId}/{tourId}")
    public ResponseEntity<Boolean> hasPurchased(@PathVariable String touristId, @PathVariable String tourId) {
        return ResponseEntity.ok(purchaseService.hasPurchasedTour(touristId, tourId));
    }
}