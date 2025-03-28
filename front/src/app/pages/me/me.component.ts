import { Component, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';
import { Subscription } from 'src/app/models/subscription.model';
import { AuthService } from 'src/app/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { UserService } from 'src/app/services/user.service';
import { User, UserProfileUpdate } from 'src/app/models/user.model';


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
  subscriptions: Subscription[] = [];
  updateSuccess: boolean = false;
  updateError: string = '';

  constructor(
    private themeService: ThemeService,
    private authService: AuthService,
    private userService: UserService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUserId();
    this.loadUserData();
    this.loadSubscriptions();
  }

  loadUserData() {
    this.userService.getCurrentUser().subscribe({
      next: (userData: User) => {
        if (userData) {
          this.user.username = userData.username || '';
          this.user.email = userData.email || '';
        }
      },
      error: (error: Error) => {
        console.error('Error loading user data:', error);
      }
    });
  }
  
  loadSubscriptions() {
    this.themeService.getAllSubscribedThemes(this.userId).subscribe({
      next: (response: Subscription[]) => {
        console.log("Subscribed themes", response);
        this.subscriptions = response;
      },
      error: (error: Error) => {
        console.error("Erreur lors de la récupération des abonnements :", error);
      }
    });
  }
  
  saveProfile() {
    this.updateSuccess = false;
    this.updateError = '';
    
    if (!this.user.username || !this.user.email) {
      this.updateError = "Le nom d'utilisateur et l'email sont obligatoires";
      return;
    }

    const updateData: UserProfileUpdate = {
      username: this.user.username,
      email: this.user.email
    };
    
    // Only include password if it's provided
    if (this.user.password) {
      updateData.password = this.user.password;
    }
    
    console.log("Tentative de mise à jour du profil :", updateData);
    
    this.userService.updateUserProfile(this.userId, updateData).subscribe({
      next: (response: User) => {
        console.log("Profil mis à jour avec succès", response);
        this.updateSuccess = true;
        // Clear password field after successful update
        this.user.password = '';
      },
      error: (error) => {
        console.error("Erreur lors de la mise à jour du profil", error);
        this.updateError = error.error?.message || "Une erreur est survenue lors de la mise à jour du profil";
      }
    });
  }
}
