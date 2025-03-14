import { Component, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';
import { Subscription } from 'src/app/models/subscription.model';
import { AuthService } from 'src/app/services/auth.service';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {

  user = {
    username: '',
    email: '',
    password: ''
  };

  userId: number = 0;
  subscriptions: any[] = [];
  isLoading: boolean = true;

  constructor(
    private themeService: ThemeService,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUserId();
    this.loadUserData();
    this.loadSubscriptions();
  }

  loadUserData() {
    this.isLoading = true;
    this.authService.fetchCurrentUser().subscribe({
      next: (userData) => {
        if (userData) {
          this.user.username = userData.username || '';
          this.user.email = userData.email || '';
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading user data:', error);
        this.isLoading = false;
      }
    });
  }
  
  loadSubscriptions() {
    this.themeService.getAllSubscribedThemes(this.userId).subscribe({
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
    if (!this.user.username || !this.user.email) {
      console.error("Username and email are required");
      return;
    }

    const updateData: any = {
      username: this.user.username,
      email: this.user.email
    };
    
    // Only include password if it's provided
    if (this.user.password) {
      updateData.password = this.user.password;
    }
    
    console.log("Tentative de mise à jour du profil :", updateData);
  }
}
