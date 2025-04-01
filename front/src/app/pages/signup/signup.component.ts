import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent {
  
  constructor(private router: Router, private authService: AuthService) {}
  user={
    username: '',
    email: '',
    password: '',
  }

  goBack() {
    this.router.navigate(['/']);
  }

  onSignup() {
    this.authService.register(this.user.username, this.user.email, this.user.password).subscribe({
      next: (response) => {
        this.router.navigate(['/articles']);
      },
      error: (error) => {
        console.error('Erreur lors de l’inscription', error);
      }
    });
  }
}
