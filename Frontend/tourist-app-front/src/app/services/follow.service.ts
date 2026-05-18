import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class FollowService {

  private baseUrl = 'http://localhost:9000/api/follow';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private headers() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.authService.getToken() });
  }

  follow(followerId: string, followingId: string) {
    return this.http.post(`${this.baseUrl}/${followerId}/${followingId}`, {}, { headers: this.headers() });
  }

  unfollow(followerId: string, followingId: string) {
    return this.http.delete(`${this.baseUrl}/${followerId}/${followingId}`, { headers: this.headers() });
  }

  getFollowing(userId: string) {
    return this.http.get<string[]>(`${this.baseUrl}/${userId}/following`, { headers: this.headers() });
  }

  getRecommendations(userId: string) {
    return this.http.get<string[]>(`${this.baseUrl}/${userId}/recommendations`, { headers: this.headers() });
  }

  isFollowing(followerId: string, followingId: string) {
    return this.http.get<boolean>(`${this.baseUrl}/check/${followerId}/${followingId}`, { headers: this.headers() });
  }
}