import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { AuthService } from '../../services/auth.service';
import { Theme } from 'src/app/models/theme.model';
import { DeviceDetectorService } from 'src/app/services/device-detector.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent implements OnInit, OnDestroy {
  @Input() theme: Theme = {
    id: 0,
    title: '',
    description: '',
    isSubscribed: false
  };
  userId: number = 0;
  deviceType: 'desktop' | 'mobile' = 'desktop';
  private destroy$ = new Subject<void>();

  constructor(
    private themeService: ThemeService,
    private authService: AuthService,
    private deviceDetectorService: DeviceDetectorService
  ) {}

  ngOnInit() {
    this.userId = this.authService.getCurrentUserId();
    
    // Définir le type d'appareil initial
    this.deviceType = this.deviceDetectorService.getDeviceType();

    // S'abonner aux changements de taille d'écran
    this.deviceDetectorService.isMobile$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isMobile => {
        this.deviceType = isMobile ? 'mobile' : 'desktop';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleSubscription() {
    if (!this.theme) {
      console.error('Theme is not defined');
      return;
    }
  
    if (this.theme.isSubscribed) {
      this.themeService.unsubscribeToTheme(this.userId, this.theme.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.theme = { ...this.theme, isSubscribed: false };
          },
          error: (err: Error) => console.error('Erreur désabonnement', err),
        });
    } else {
      this.themeService.subscribeToTheme(this.userId, this.theme.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.theme = { ...this.theme, isSubscribed: true };
          },
          error: (err: Error) => console.error('Erreur abonnement', err),
        });
    }
  }
}
