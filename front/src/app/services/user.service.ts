import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private authApiUrl = 'http://localhost:8080/auth';
  private usersApiUrl = 'http://localhost:8080/api/users';
  private currentUserId: number | null = null;
  private userSubject = new BehaviorSubject<any>(null);
  public user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Get current user from API
   */
  getCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.authApiUrl}/me`).pipe(
      tap(user => {
        if (user && user.id) {
          this.setCurrentUserId(user.id);
          this.userSubject.next(user);
        }
      }),
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of(null);
      })
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
  updateUserProfile(userId: number, userData: any): Observable<any> {
    return this.http.put<any>(`${this.usersApiUrl}/${userId}`, userData).pipe(
      tap(updatedUser => {
        this.userSubject.next(updatedUser);
      })
    );
  }
}
