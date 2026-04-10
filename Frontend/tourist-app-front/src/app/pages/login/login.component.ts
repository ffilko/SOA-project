import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  credentials = {
    username: '',
    password: ''
  };

  constructor(
  private authService: AuthService,
  private router: Router
) {}

  login() {
  this.authService.login(this.credentials).subscribe({
    next: (res: any) => {
      this.authService.setToken(res.token);
      this.router.navigate(['/profile']);
    },
    error: () => console.log("Login failed")
  });
}
}