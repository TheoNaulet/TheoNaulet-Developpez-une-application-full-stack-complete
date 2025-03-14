import { Component, Input, OnInit } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent implements OnInit {
  @Input() theme: any;
  userId: number = 0;

  constructor(
    private themeService: ThemeService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUserId();
  }

  toggleSubscription() {
    console.log(this.theme);
  
    if (this.theme.isSubscribed) {
      this.themeService.unsubscribeToTheme(this.userId, this.theme.id).subscribe({
        next: () => {
          this.theme = { ...this.theme, isSubscribed: false };
          console.log(`Désabonné au thème ${this.theme.id}`);
        },
        error: (err) => console.error('Erreur désabonnement', err),
      });
    } else {
      this.themeService.subscribeToTheme(this.userId, this.theme.id).subscribe({
        next: () => {
          this.theme = { ...this.theme, isSubscribed: true };
          console.log(`Abonné au thème ${this.theme.id}`);
        },
        error: (err) => console.error('Erreur abonnement', err),
      });
    }
  }
}
