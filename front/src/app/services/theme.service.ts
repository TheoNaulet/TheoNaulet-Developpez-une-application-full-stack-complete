import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private apiUrl = 'http://localhost:8080/api';


  constructor(private http: HttpClient) {}

  // Inscription
  getAllThemes(): Observable<any> {
    return this.http.get(`${this.apiUrl}/themes`);
  }

  getAllSubscribedThemes(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/subscriptions/user/${userId}`);
  }

  subscribeToTheme(userId: number, themeId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/subscriptions/subscribe`, null, { 
      params: { userId: userId.toString(), themeId: themeId.toString() }
    });
  }

  unsubscribeToTheme(userId: number, themeId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/subscriptions/unsubscribe`, { 
      params: { userId: userId.toString(), themeId: themeId.toString() }
    });
    }
}
