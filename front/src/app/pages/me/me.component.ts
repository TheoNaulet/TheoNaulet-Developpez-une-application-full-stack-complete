import { Component, OnInit, OnDestroy } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';
import { Subscription } from 'src/app/models/subscription.model';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { User, UserProfileUpdate } from 'src/app/models/user.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Theme } from 'src/app/models/theme.model';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit, OnDestroy {

  user = {
    username: '',
    email: '',
    password: ''
  };

  userId: number = 0;
  subscriptions: Subscription[] = [];
  updateSuccess: boolean = false;
  updateError: string = '';
  private destroy$ = new Subject<void>();

  constructor(
    private themeService: ThemeService,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUserId();
    this.loadUserData();
    this.loadSubscriptions();
  }

  loadUserData() {
    this.userService.getCurrentUser().pipe(takeUntil(this.destroy$)).subscribe({
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
    this.themeService.getAllSubscribedThemes(this.userId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response: Subscription[]) => {
        console.log("Subscribed themes processed:", response);
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
      this.updateError = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }
    
    const updateData: UserProfileUpdate = {
      username: this.user.username,
      email: this.user.email
    };
    
    if (this.user.password) {
      updateData.password = this.user.password;
    }
    
    this.userService.updateUserProfile(this.userId, updateData).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.updateSuccess = true;
        this.user.password = '';
      },
      error: (error: Error) => {
        this.updateError = 'Une erreur est survenue lors de la mise à jour du profil.';
        console.error('Error updating profile:', error);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
