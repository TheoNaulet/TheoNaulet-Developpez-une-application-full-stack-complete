import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private apiUrl = 'http://localhost:8080/api/articles';


  constructor(private http: HttpClient) {}

  getArticlesBySubscribedThemes(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/subscribed/${userId}`);
  }

  createArticle(articleData: { themeId?: number; title: string; content: string }): Observable<any> {
    return this.http.post(this.apiUrl, articleData);
  }

  getArticleById(articleId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${articleId}`);
  }
}