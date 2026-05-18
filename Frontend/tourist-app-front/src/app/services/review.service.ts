import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReviewService {
  private apiUrl = 'http://localhost:8083/api/reviews';
  private toursUrl = 'http://localhost:8083/api/tours';

  constructor(private http: HttpClient) {}
  getCompletedTours(): Observable<any[]> {
    return this.http.get<any[]>(this.toursUrl);
  }

  submitReview(reviewData: any, selectedFiles: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('rating', reviewData.rating.toString());
    formData.append('comment', reviewData.comment);
    formData.append('touristId', reviewData.touristId);
    formData.append('visitDate', reviewData.visitDate);
    formData.append('tourId', reviewData.tourId);

    for (let file of selectedFiles) {
      formData.append('files', file, file.name);
    }
    return this.http.post(this.apiUrl, formData);
  }

  getReviewsByTour(tourId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tour/${tourId}`);
  }
}