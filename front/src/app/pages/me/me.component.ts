import { Component, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';
import { Subscription } from 'src/app/models/subscription.model';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {

  user = {
    username: 'Username',
    email: 'email@email.fr',
    password: ''
  };

  subscriptions: any[] = [];

  constructor(
    private themeService: ThemeService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const userId = this.authService.getCurrentUserId();
    
    this.themeService.getAllSubscribedThemes(userId).subscribe({
      next: (response) => {
        console.log("Subscribed themes", response);
        this.subscriptions = response;
      },
      error: (error) => {
        console.error("Erreur lors de la récupération des abonnements :", error);
      }
    });
  }
  
  saveProfile() {
    console.log("Profil mis à jour :", this.user);
  }
}
