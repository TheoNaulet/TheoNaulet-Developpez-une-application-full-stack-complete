import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { takeUntil, map, catchError } from 'rxjs/operators';
import { Theme } from '../models/theme.model';
import { Subscription } from '../models/subscription.model';

// Interface pour représenter les données brutes reçues de l'API
interface SubscriptionResponse {
  id: number;
  createdAt?: string;
  themeId?: number;
  theme?: {
    id: number;
    title: string;
    description: string;
    isSubscribed?: boolean;
  };
  // Propriétés potentielles si l'item est lui-même un thème
  title?: string;
  description?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ThemeService implements OnDestroy {
  private apiUrl = 'http://localhost:8080/api';
  private destroy$ = new Subject<void>();

  constructor(private http: HttpClient) {}

  // Inscription
  getAllThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes`).pipe(
      takeUntil(this.destroy$)
    );
  }

  getAllSubscribedThemes(userId: number): Observable<Subscription[]> {
    return this.http.get<SubscriptionResponse[]>(`${this.apiUrl}/subscriptions/user/${userId}`).pipe(
      map(response => {
        // Transformer les données pour s'assurer qu'elles correspondent à l'interface Subscription
        return response.map(item => {
          // Si l'item a déjà une structure valide avec une propriété theme
          if (item.theme && typeof item.theme === 'object' && item.theme.title) {
            // S'assurer que isSubscribed est défini
            item.theme.isSubscribed = true;
            return item as Subscription;
          }
          
          // Si l'item est lui-même un thème (sans wrapper Subscription)
          if (item.title && item.description) {
            return {
              id: item.id || 0,
              createdAt: item.createdAt || new Date().toISOString(),
              theme: {
                id: item.id,
                title: item.title,
                description: item.description,
                isSubscribed: true
              }
            } as Subscription;
          }
          
          // Si l'item a une structure différente (par exemple, themeId, themeTitle, etc.)
          return {
            id: item.id || 0,
            createdAt: item.createdAt || new Date().toISOString(),
            theme: {
              id: item.themeId || 0,
              title: item.title || 'Sans titre',
              description: item.description || 'Aucune description',
              isSubscribed: true
            }
          } as Subscription;
        });
      }),
      catchError(error => {
        console.error('Error fetching subscriptions:', error);
        return [];
      }),
      takeUntil(this.destroy$)
    );
  }

  subscribeToTheme(userId: number, themeId: number): Observable<Subscription> {
    return this.http.post<Subscription>(`${this.apiUrl}/subscriptions/subscribe`, null, { 
      params: { userId: userId.toString(), themeId: themeId.toString() }
    }).pipe(
      takeUntil(this.destroy$)
    );
  }

  unsubscribeToTheme(userId: number, themeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/subscriptions/unsubscribe`, { 
      params: { userId: userId.toString(), themeId: themeId.toString() }
    }).pipe(
      takeUntil(this.destroy$)
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
