import { Component, OnInit } from '@angular/core';
import { Tour } from 'src/app/models/tour';
import { TourService } from 'src/app/services/tour.service';
import { PurchaseService } from 'src/app/services/purchase.service';
import { AuthService } from 'src/app/services/auth.service';
import { OrderItem } from 'src/app/models/purchase';

@Component({
  selector: 'app-explore-tours',
  templateUrl: './explore-tours.component.html'
})
export class ExploreToursComponent implements OnInit {
  tours: any[] = [];
  currentUserId: string = '';

  constructor(
    private tourService: TourService,
    private purchaseService: PurchaseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.currentUserId = userId;
      this.loadAvailableTours();
    }
  }

  loadAvailableTours(): void {
    this.tourService.getPublishedTours().subscribe({
      next: (allTours: Tour[]) => {
        this.tours = [];
        
        allTours.forEach(tour => {
          this.purchaseService.hasPurchased(this.currentUserId, tour.id).subscribe({
            next: (hasPurchased: boolean) => {
              if (!hasPurchased) {
                this.tours.push(tour);
              }
            },
            error: (err) => console.error(err)
          });
        });
      },
      error: (err) => console.log(err)
    });
  }

  addToCart(tour: any): void {
    const item: OrderItem = { tourId: tour.id, tourName: tour.name, price: tour.price }; 
    this.purchaseService.addItemToCart(this.currentUserId, item).subscribe({
      next: () => alert(`Tour "${tour.name}" added!`),
      error: () => alert('You already have this tour.')
    });
  }
}