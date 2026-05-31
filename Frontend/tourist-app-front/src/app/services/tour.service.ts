import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Tour, TourDifficulty } from '../models/tour';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { KeyPoint } from '../models/keyPoint';

@Injectable({ providedIn: 'root' })
export class TourService {

  private baseUrl = 'http://localhost:9000/api/tours';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private headers() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.authService.getToken() });
  }

  addTour(tour: Tour) {
    return this.http.post<Tour>(this.baseUrl, tour);
  }

  getToursByAuthor(authorId: string): Observable<Tour[]>{
    return this.http.get<Tour[]>(`${this.baseUrl}/author/${authorId}`); 
  }

  addKeyPoint(tourId: string, keyPoint: KeyPoint) {
    return this.http.post<Tour>(`${this.baseUrl}/${tourId}/keypoints`, keyPoint);
  }

  getAllTours(): Observable<Tour[]> {
    return this.http.get<Tour[]>(this.baseUrl, { headers: this.headers() });
  }
}