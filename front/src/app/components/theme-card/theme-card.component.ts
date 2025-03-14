import { Component, Input } from '@angular/core';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent {
  @Input() theme: any;
  userId: number = 26; 

  constructor(private themeService: ThemeService) {}

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
