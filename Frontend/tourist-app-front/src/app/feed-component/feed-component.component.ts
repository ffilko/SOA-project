import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { FollowService } from '../services/follow.service';

@Component({
  selector: 'app-feed-component',
  templateUrl: './feed-component.component.html'
})
export class FeedComponentComponent implements OnInit {

  blogs: any[] = [];
  recommendations: string[] = [];
  followingSet = new Set<string>();
  currentUserId: string | null = null;

  usernameCache: { [userId: string]: string } = {};
  commentsMap: { [blogId: string]: any[] } = {};
  expandedComments = new Set<string>();
  newCommentMap: { [blogId: string]: string } = {};

  private blogBaseUrl = 'http://localhost:8081/api/blogs';
  private stakeholdersUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private followService: FollowService
  ) {}

  ngOnInit() {
    this.currentUserId = this.authService.getUserId();
    if (!this.currentUserId) return;

    this.loadFeed();
    this.loadRecommendations();
    this.loadFollowing();
  }

  private headers() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.authService.getToken() });
  }

  loadFeed() {
    this.http.get<any[]>(`${this.blogBaseUrl}/feed/${this.currentUserId}`, { headers: this.headers() })
      .subscribe({
        next: data => {
          this.blogs = data;
          data.forEach(blog => this.fetchUsername(blog.userId));
        },
        error: () => this.blogs = []
      });
  }

  loadRecommendations() {
    this.followService.getRecommendations(this.currentUserId!).subscribe({
      next: data => {
        this.recommendations = data;
        data.forEach(id => this.fetchUsername(id));
      },
      error: () => this.recommendations = []
    });
  }

  loadFollowing() {
    this.followService.getFollowing(this.currentUserId!).subscribe({
      next: ids => this.followingSet = new Set(ids)
    });
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

  isFollowing(userId: string): boolean {
    return this.followingSet.has(userId);
  }

  follow(targetId: string) {
    this.followService.follow(this.currentUserId!, targetId).subscribe(() => {
      this.followingSet.add(targetId);
      this.loadFeed();
    });
  }

  unfollow(targetId: string) {
    this.followService.unfollow(this.currentUserId!, targetId).subscribe(() => {
      this.followingSet.delete(targetId);
      this.loadFeed();
    });
  }

  toggleComments(blogId: string) {
    if (this.expandedComments.has(blogId)) {
      this.expandedComments.delete(blogId);
    } else {
      this.expandedComments.add(blogId);
      this.loadComments(blogId);
    }
  }

  isExpanded(blogId: string): boolean {
    return this.expandedComments.has(blogId);
  }

  loadComments(blogId: string) {
    this.http.get<any[]>(`${this.blogBaseUrl}/${blogId}/comments`, { headers: this.headers() })
      .subscribe({
        next: data => {
          this.commentsMap[blogId] = data;
          data.forEach(c => this.fetchUsername(c.userId));
        },
        error: () => this.commentsMap[blogId] = []
      });
  }

  submitComment(blogId: string, authorId: string) {
    const content = this.newCommentMap[blogId]?.trim();
    if (!content) return;

    const body = { userId: this.currentUserId, content };
    this.http.post(`${this.blogBaseUrl}/${blogId}/comments`, body, { headers: this.headers() })
      .subscribe({
        next: () => {
          this.newCommentMap[blogId] = '';
          this.loadComments(blogId);
        }
      });
  }
}