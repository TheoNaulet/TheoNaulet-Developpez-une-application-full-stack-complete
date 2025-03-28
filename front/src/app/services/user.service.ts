import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, BehaviorSubject, Subject } from 'rxjs';
import { catchError, map, tap, takeUntil } from 'rxjs/operators';
import { User, UserProfileUpdate } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService implements OnDestroy {
  private authApiUrl = 'http://localhost:8080/auth';
  private usersApiUrl = 'http://localhost:8080/api/users';
  private currentUserId: number | null = null;
  private userSubject = new BehaviorSubject<User | null>(null);
  public user$ = this.userSubject.asObservable();
  private destroy$ = new Subject<void>();

  constructor(private http: HttpClient) {}

  /**
   * Get current user from API
   */
  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.authApiUrl}/me`).pipe(
      tap(user => {
        if (user && user.id) {
          this.setCurrentUserId(user.id);
          this.userSubject.next(user);
        }
      }),
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of({} as User);
      }),
      takeUntil(this.destroy$)
    );
  }

  /**
   * Set current user ID
   */
  setCurrentUserId(id: number): void {
    this.currentUserId = id;
    localStorage.setItem('userId', id.toString());
  }

  /**
   * Get current user ID from local storage
   */
  getCurrentUserId(): number {
    if (this.currentUserId) {
      return this.currentUserId;
    }
    
    const userId = localStorage.getItem('userId');
    if (userId) {
      return parseInt(userId, 10);
    }
    
    return 0;
  }

  /**
   * Update user profile
   */
  updateUserProfile(userId: number, userData: UserProfileUpdate): Observable<User> {
    return this.http.put<User>(`${this.usersApiUrl}/${userId}`, userData).pipe(
      tap(updatedUser => {
        this.userSubject.next(updatedUser);
      }),
      takeUntil(this.destroy$)
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
