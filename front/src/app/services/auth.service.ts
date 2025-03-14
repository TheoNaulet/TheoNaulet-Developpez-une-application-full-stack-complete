import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, switchMap, map, catchError } from 'rxjs/operators';

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
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, { username, email, password }).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          this.authStateSubject.next(true);
          // After registration, fetch user info
          this.fetchCurrentUser().subscribe();
        }
      })
    );
  }

  // Connexion
  login(emailOrUsername: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { emailOrUsername, password }).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          this.authStateSubject.next(true);
        }
      }),
      switchMap(response => {
        // After login, fetch user info
        return this.fetchCurrentUser().pipe(
          map(() => response),
          catchError(error => {
            console.error('Error fetching user after login:', error);
            return of(response);
          })
        );
      })
    );
  }

  // Fetch current user info
  fetchCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`).pipe(
      tap(user => {
        if (user && user.id) {
          localStorage.setItem('userId', user.id.toString());
        }
      }),
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of(null);
      })
    );
  }

  // Get current user ID
  getCurrentUserId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }

  // Déconnexion
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    this.authStateSubject.next(false);
  }
}
