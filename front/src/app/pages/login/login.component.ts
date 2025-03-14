import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  user={
    emailOrUsername:"",
    password:""
  }

  constructor(private router: Router, private authService: AuthService) {}

  goBack() {
    this.router.navigate(['/']);
  }

  onLogin() {
    this.authService.login(this.user.emailOrUsername, this.user.password).subscribe({
      next: (response) => {
        console.log('Connexion réussie', response);
        this.router.navigate(['/articles']);
      },
      error: (error) => {
        console.error('Erreur de connexion', error);
      }
    });
  }
}
