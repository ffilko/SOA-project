import { Component, OnInit } from '@angular/core';
import { PurchaseService } from '../../services/purchase.service';
import { ShoppingCart } from '../../models/purchase';
import { AuthService } from '../../services/auth.service'; 

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html'
})
export class ShoppingCartComponent implements OnInit {
  cart: ShoppingCart | null = null;
  currentTouristId: string = '';

  constructor(
    private purchaseService: PurchaseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId(); 
    
    if (userId) {
      this.currentTouristId = userId;
      this.loadCart();
    } else {
      console.error('User not logged in.');
    }
  }

  loadCart(): void {
    if (!this.currentTouristId) return;

    this.purchaseService.getCart(this.currentTouristId).subscribe({
      next: (data) => {
        this.cart = data;
      },
      error: (err) => console.error('Greška pri učitavanju korpe', err)
    });
  }

  removeItem(itemId: string | undefined): void {
    if (!itemId || !this.currentTouristId) return;
    
    this.purchaseService.removeItem(this.currentTouristId, itemId).subscribe({
      next: (updatedCart) => {
        this.cart = updatedCart;
      },
      error: (err) => console.error('Greška pri brisanju stavke', err)
    });
  }

  checkout(): void {
    if (!this.currentTouristId) return;

    this.purchaseService.checkout(this.currentTouristId).subscribe({
      next: (message) => {
        alert(message);
        this.loadCart(); 
      },
      error: (err) => {
        alert('Došlo je do greške pri kupovini.');
        console.error(err);
      }
    });
  }
}