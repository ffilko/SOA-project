package org.tourism.purchase_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourism.purchase_service.dto.OrderItemDTO;
import org.tourism.purchase_service.dto.ShoppingCartDTO;
import org.tourism.purchase_service.model.OrderItem;
import org.tourism.purchase_service.model.ShoppingCart;
import org.tourism.purchase_service.model.TourPurchaseToken;
import org.tourism.purchase_service.repository.ShoppingCartRepository;
import org.tourism.purchase_service.repository.TourPurchaseTokenRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final ShoppingCartRepository cartRepository;
    private final TourPurchaseTokenRepository tokenRepository;

    public PurchaseService(ShoppingCartRepository cartRepository, TourPurchaseTokenRepository tokenRepository) {
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
    }

    private ShoppingCart getCartEntity(String touristId) {
        return cartRepository.findByTouristId(touristId)
                .orElseGet(() -> cartRepository.save(new ShoppingCart(touristId)));
    }

    public ShoppingCartDTO getCart(String touristId) {
        ShoppingCart cart = getCartEntity(touristId);
        return mapToDTO(cart);
    }

    public ShoppingCartDTO addItemToCart(String touristId, OrderItemDTO itemDTO) {
        ShoppingCart cart = getCartEntity(touristId);

        if (tokenRepository.existsByTouristIdAndTourId(touristId, itemDTO.getTourId())) {
            throw new IllegalArgumentException("Tourist already has this tour");
        }

        OrderItem newItem = new OrderItem(itemDTO.getTourId(), itemDTO.getTourName(), itemDTO.getPrice());
        cart.addItem(newItem);

        ShoppingCart savedCart = cartRepository.save(cart);
        return mapToDTO(savedCart);
    }

    public ShoppingCartDTO removeItemFromCart(String touristId, UUID itemId) {
        ShoppingCart cart = getCartEntity(touristId);

        OrderItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found."));

        cart.removeItem(itemToRemove);
        ShoppingCart savedCart = cartRepository.save(cart);
        return mapToDTO(savedCart);
    }

    @Transactional
    public void checkout(String touristId) {
        ShoppingCart cart = getCartEntity(touristId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        for (OrderItem item : cart.getItems()) {
            TourPurchaseToken token = new TourPurchaseToken(touristId, item.getTourId());
            tokenRepository.save(token);
        }

        cart.clearCart();
        cartRepository.save(cart);
    }

    public boolean hasPurchasedTour(String touristId, String tourId) {
        return tokenRepository.existsByTouristIdAndTourId(touristId, tourId);
    }

    private ShoppingCartDTO mapToDTO(ShoppingCart cart) {
        List<OrderItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new OrderItemDTO(item.getId(), item.getTourId(), item.getTourName(), item.getPrice()))
                .collect(Collectors.toList());

        return new ShoppingCartDTO(cart.getId(), cart.getTouristId(), cart.getTotalPrice(), itemDTOs);
    }
}