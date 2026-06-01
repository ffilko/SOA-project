import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { TourService } from '../../services/tour.service';

@Component({
  selector: 'app-my-tours',
  templateUrl: './my-tours.component.html',
  styleUrls: ['./my-tours.component.css']
})
export class MyToursComponent implements OnInit {
  completedTours: any[] = [];
  selectedTourDetails: any = null;

  constructor(
    private router: Router,
    private reviewService: ReviewService,
    private tourService: TourService // DODATO
  ) {}

  ngOnInit(): void {
    this.reviewService.getCompletedTours().subscribe({
      next: (data) => {
        this.completedTours = data || [];
      },
      error: (err) => {
        console.error(err);
        alert('Error loading tours from backend.');
      }
    });
  }

  goToReview(tourId: string): void {
    this.router.navigate(['/review', tourId]);
  }

  viewDetails(tourId: string): void {
    if (this.selectedTourDetails && this.selectedTourDetails.id === tourId) {
      this.selectedTourDetails = null;
      return;
    }
    this.tourService.getPurchasedTourDetails(tourId).subscribe({
      next: (data) => {
        this.selectedTourDetails = data;
      },
      error: (err) => console.error('Greska pri ucitavanju detalja ture.', err)
    });
  }
}