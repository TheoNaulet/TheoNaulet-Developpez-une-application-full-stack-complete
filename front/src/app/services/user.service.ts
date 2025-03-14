import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/auth';
  private currentUserId: number | null = null;

  constructor(private http: HttpClient) {}

  /**
   * Get the current user's information from the backend
   */
  getCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`).pipe(
      tap(user => {
        // Store the user ID for future use
        this.currentUserId = user.id;
        // Store the user ID in localStorage for persistence
        localStorage.setItem('userId', user.id.toString());
      }),
      catchError(error => {
        console.error('Error fetching current user:', error);
        return of(null);
      })
    );
  }

  /**
   * Get the current user's ID
   * Returns the cached ID if available, otherwise fetches from localStorage
   */
  getUserId(): number {
    // If we already have the ID in memory, return it
    if (this.currentUserId) {
      return this.currentUserId;
    }
    
    // Try to get from localStorage
    const storedId = localStorage.getItem('userId');
    if (storedId) {
      this.currentUserId = parseInt(storedId, 10);
      return this.currentUserId;
    }
    
    return 0;
  }
}
