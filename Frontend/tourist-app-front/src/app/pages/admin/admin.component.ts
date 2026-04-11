import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {
  
  users: any[] = [];
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers(){
    this.authService.getAllUsers().subscribe({
      next: (data: any) => {
        this.users = data;
      },
      error: (err) => {
        this.errorMessage = 'Error';
      }
    });
  }

  onBlockUser(userId: string) {
    if (confirm('Are you sure you want to block this user?')) {
      this.authService.blockUser(userId).subscribe({
        next: () => {
          alert('User blocked');
          this.loadUsers();
        },
        error: (err) => {
          alert('Error');
        }
      });
    }
  }
}