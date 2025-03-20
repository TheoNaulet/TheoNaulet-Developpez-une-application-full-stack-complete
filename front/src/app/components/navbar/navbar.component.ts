import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  isAuthenticated: boolean = false;
  showFullNav: boolean = false;
  isMobile: boolean = window.innerWidth <= 768; 
  private authSubscription: Subscription | null = null;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    // Initial check
    this.isAuthenticated = this.authService.isLoggedIn();
    
    // Subscribe to auth state changes
    this.authSubscription = this.authService.authState$.subscribe(
      isAuthenticated => {
        this.isAuthenticated = isAuthenticated;
      }
    );
  }

  ngOnDestroy() {
    // Clean up subscription when component is destroyed
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  get shouldShowNavbar(): boolean {
    const route = this.router.url;
    if (route === '/') return false; 
    if (route === '/login' || route === '/signup') return true; 
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
