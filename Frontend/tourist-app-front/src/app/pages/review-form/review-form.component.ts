import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-review-form',
  templateUrl: './review-form.component.html',
  styleUrls: ['./review-form.component.css']
})
export class ReviewFormComponent implements OnInit {
  reviewModel = {
    rating: 5,
    comment: '',
    touristId: '',
    visitDate: '',
    tourId: ''
  };

  uploadedFiles: File[] = [];
  loadedReviews: any[] = [];

  constructor(
    private reviewService: ReviewService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.reviewModel.touristId = this.authService.getUserId() || '';
    this.reviewModel.tourId = this.route.snapshot.paramMap.get('tourId') || '';
  }

  onFileChange(event: any): void {
    if (event.target.files && event.target.files.length > 0) {
      this.uploadedFiles = Array.from(event.target.files);
    }
  }

  submitReview(): void {
    if (!this.reviewModel.visitDate) {
      alert('Please enter the visit date.');
      return;
    }

    this.reviewService.submitReview(this.reviewModel, this.uploadedFiles).subscribe({
      next: () => {
        alert('Review successfully submitted!');
        this.reviewModel.comment = '';
        this.reviewModel.rating = 5;
        this.uploadedFiles = [];
      },
      error: () => alert('Error submitting review.')
    });
  }


  goBack(): void {
    this.router.navigate(['/my-tours']);
  }
}