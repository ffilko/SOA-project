import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class BlogService {
  private baseUrl = 'http://localhost:9000/api/blogs';

  constructor(private http: HttpClient) {}

  getFeed(userId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/feed/${userId}`);
  }

  getAll() {
    return this.http.get<any[]>(this.baseUrl);
  }

  getById(id: string) {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  addComment(blogId: string, dto: any) {
    return this.http.post(`${this.baseUrl}/${blogId}/comments`, dto);
  }

  getComments(blogId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/${blogId}/comments`);
  }
}