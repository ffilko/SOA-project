import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user: any = {};

  roles = [
    { label: 'Tourist', value: 0 },
    { label: 'Guide', value: 1 }
  ];

  constructor(private authService: AuthService) {}

  register() {
    this.authService.register(this.user).subscribe({
      error: (err) => console.log(err)
    })
  }
}
