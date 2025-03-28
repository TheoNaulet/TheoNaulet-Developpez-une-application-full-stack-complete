import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { tap, switchMap, map, catchError, takeUntil } from 'rxjs/operators';
import { User } from '../models/user.model';

interface AuthResponse {
  token: string;
  userId?: number;
  username?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService implements OnDestroy {
  private apiUrl = 'http://localhost:8080/auth';
  private authStateSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  public authState$ = this.authStateSubject.asObservable();
  private destroy$ = new Subject<void>();

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
          this.fetchCurrentUser().pipe(takeUntil(this.destroy$)).subscribe();
        }
      }),
      takeUntil(this.destroy$)
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
          }),
          takeUntil(this.destroy$)
        );
      }),
      takeUntil(this.destroy$)
    );
  }

  // Fetch current user info
  fetchCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`).pipe(
      tap(user => {
        if (user && user.id) {
          localStorage.setItem('userId', user.id.toString());
        }
      }),
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of({} as User);
      }),
      takeUntil(this.destroy$)
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
