import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:9000/api/stakeholders/';
  private tokenKey = 'token';

  private loggedInSubject = new BehaviorSubject<boolean>(!!localStorage.getItem(this.tokenKey));
  isLoggedIn$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient) { }

  register(user: any) {
    return this.http.post(`${this.baseUrl}user`, user);
  }

  login(credentials: any) {
    return this.http.post(`${this.baseUrl}login`, credentials);
  }

  setToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
    this.loggedInSubject.next(true);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.loggedInSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUserId(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log(payload);

    return payload.user_id;
  }

  getAllUsers() {
    const token = this.getToken();
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + token});

    return this.http.get(`${this.baseUrl}users`, { headers });
  }

  blockUser(userId: string) {
    const token = this.getToken();
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + token});
    
    return this.http.put(`${this.baseUrl}users/${userId}/block`, {}, { headers });
  }

  getUserById(userId: string) {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.getToken()});
    return this.http.get<any>(`${this.baseUrl}user/${userId}`, { headers });
  }

  deleteUser(userId: string) {
    const token = this.getToken();
    const headers = new HttpHeaders({ 'Authorization': 'Bearer ' + token });

    return this.http.delete(
      `${this.baseUrl}users/${userId}/delete`,
      { headers }
    );
  }
}