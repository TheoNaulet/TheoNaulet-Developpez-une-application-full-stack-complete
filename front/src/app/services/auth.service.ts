import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface AuthResponse {
  token: string;
  userId?: number;
  username?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private authStateSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  public authState$ = this.authStateSubject.asObservable();

  constructor(private http: HttpClient) {
    // Initialize auth state from localStorage
    this.authStateSubject.next(this.isLoggedIn());
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  // Inscription
  register(username: string, email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, { username, email, password });
  }

  // Connexion
  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          this.authStateSubject.next(true);
        }
      })
    );
  }

  // Déconnexion
  logout(): void {
    localStorage.removeItem('token');
    this.authStateSubject.next(false);
  }
}
