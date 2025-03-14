import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  isAuthenticated: boolean = false;
  showFullNav: boolean = false;
  isMobile: boolean = window.innerWidth <= 768; 

  constructor(private router: Router) {}

  ngOnInit() {
    this.isAuthenticated = !!localStorage.getItem('token');
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.isMobile = window.innerWidth <= 768;
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

  logout() {
    this.isAuthenticated = false;
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
