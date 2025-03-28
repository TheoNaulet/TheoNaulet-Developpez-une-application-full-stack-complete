import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  user = {
    emailOrUsername: "",
    password: ""
  };

  errorMessage: string | null = null; 

  constructor(private router: Router, private authService: AuthService) {}

  goBack() {
    this.router.navigate(['/']);
  }

  onLogin() {
    this.errorMessage = null;
    this.authService.login(this.user.emailOrUsername, this.user.password).subscribe({
      next: (response) => {
        console.log('Connexion réussie', response);
        this.router.navigate(['/articles']);
      },
      error: (error) => {
        console.error('Erreur de connexion', error);
        this.errorMessage = "Identifiants incorrects. Veuillez réessayer."; 
      }
    });
  }
}
