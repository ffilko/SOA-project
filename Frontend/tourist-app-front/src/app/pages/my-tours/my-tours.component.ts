import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReviewService } from '../../services/review.service';

@Component({
  selector: 'app-my-tours',
  templateUrl: './my-tours.component.html',
  styleUrls: ['./my-tours.component.css']
})
export class MyToursComponent implements OnInit {
  completedTours: any[] = [];

  constructor(
    private router: Router,
    private reviewService: ReviewService
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
}