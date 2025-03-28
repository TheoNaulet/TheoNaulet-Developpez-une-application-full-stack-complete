import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Subject } from 'rxjs';
import { takeUntil, filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  isAuthenticated: boolean = false;
  showFullNav: boolean = false;
  isMobile: boolean = window.innerWidth <= 768; 
  private destroy$ = new Subject<void>();

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    // Initial check
    this.isAuthenticated = this.authService.isLoggedIn();
    
    // Subscribe to auth state changes
    this.authService.authState$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isAuthenticated => {
        this.isAuthenticated = isAuthenticated;
        
        // Si l'utilisateur se déconnecte, fermer le menu mobile
        if (!isAuthenticated) {
          this.showFullNav = false;
        }
      });
    
    // Fermer le menu mobile lors d'un changement de route
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.showFullNav = false;
      });
  }

  ngOnDestroy() {
    // Clean up subscriptions
    this.destroy$.next();
    this.destroy$.complete();
  }

  get shouldShowNavbar(): boolean {
    const route = this.router.url;
    // Ne pas afficher la navbar sur la page d'accueil
    if (route === '/') return false; 
    return true; 
  }

  toggleMenu() {
    this.showFullNav = !this.showFullNav;
  }

  closeMenu() {
    this.showFullNav = false;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  @HostListener('window:resize', ['$event'])  
  onResize() {
    this.isMobile = window.innerWidth <= 768;

    if (!this.isMobile) {
      this.showFullNav = false;
    }
  }
}
