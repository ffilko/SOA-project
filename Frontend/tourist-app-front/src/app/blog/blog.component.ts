import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { FollowService } from '../services/follow.service';

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html'
})
export class BlogComponent implements OnInit {

  blog: any = null;
  comments: any[] = [];
  newComment = '';
  canComment = false;
  currentUserId: string | null = null;
  errorMsg = '';

  usernameCache: { [userId: string]: string } = {};

  private blogBaseUrl = 'http://localhost:8081/api/blogs';
  private stakeholdersUrl = 'http://localhost:8080';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private authService: AuthService,
    private followService: FollowService
  ) {}

  ngOnInit() {
    this.currentUserId = this.authService.getUserId();
    const blogId = this.route.snapshot.paramMap.get('id');
    if (!blogId || !this.currentUserId) return;

    this.loadBlog(blogId);
    this.loadComments(blogId);
  }

  private headers() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.authService.getToken() });
  }

  fetchUsername(userId: string) {
  if (this.usernameCache[userId]) return;
  this.authService.getUserById(userId).subscribe({
    next: user => {
      this.usernameCache = { ...this.usernameCache, [userId]: user.username };
    },
    error: () => {
      this.usernameCache = { ...this.usernameCache, [userId]: userId };
    }
  });
}

  getUsername(userId: string): string {
    return this.usernameCache[userId] || userId;
  }

  loadBlog(blogId: string) {
    this.http.get<any>(`${this.blogBaseUrl}/${blogId}`, { headers: this.headers() })
      .subscribe(data => {
        this.blog = data;
        this.fetchUsername(data.userId);
        this.followService.isFollowing(this.currentUserId!, data.userId)
          .subscribe(result => this.canComment = result);
      });
  }

  loadComments(blogId: string) {
    this.http.get<any[]>(`${this.blogBaseUrl}/${blogId}/comments`, { headers: this.headers() })
      .subscribe(data => {
        this.comments = data;
        data.forEach(c => this.fetchUsername(c.userId));
      });
  }

  submitComment() {
    if (!this.newComment.trim() || !this.blog) return;

    const body = { userId: this.currentUserId, content: this.newComment };
    this.http.post(`${this.blogBaseUrl}/${this.blog.id}/comments`, body, { headers: this.headers() })
      .subscribe({
        next: () => {
          this.newComment = '';
          this.loadComments(this.blog.id);
        },
        error: () => this.errorMsg = 'You can only comment on blogs of users you follow.'
      });
  }
}