import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShoppingCart, OrderItem } from '../models/purchase';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {
  
  private baseUrl = 'http://localhost:9000/api/purchases';

  constructor(
    private http: HttpClient, 
    private authService: AuthService
  ) { }

  private headers() {
    return new HttpHeaders({ 
      'Authorization': 'Bearer ' + this.authService.getToken() 
    });
  }

  getCart(touristId: string): Observable<ShoppingCart> {
    return this.http.get<ShoppingCart>(
      `${this.baseUrl}/cart/${touristId}`, 
      { headers: this.headers() }
    );
  }

  addItemToCart(touristId: string, item: OrderItem): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(
      `${this.baseUrl}/cart/${touristId}/items`, 
      item, 
      { headers: this.headers() }
    );
  }

  removeItem(touristId: string, itemId: string): Observable<ShoppingCart> {
    return this.http.delete<ShoppingCart>(
      `${this.baseUrl}/cart/${touristId}/items/${itemId}`, 
      { headers: this.headers() }
    );
  }

  checkout(touristId: string): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/cart/${touristId}/checkout`, 
      {}, 
      { 
        headers: this.headers(), 
        responseType: 'text' 
      }
    );
  }

  hasPurchased(touristId: string, tourId: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.baseUrl}/check/${touristId}/${tourId}`, 
      { headers: this.headers() }
    );
  }
}