import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Article, Comment, ArticleFormData } from '../models/article.model';

@Injectable({
  providedIn: 'root',
})
export class ArticleService implements OnDestroy {
  private apiUrl = 'http://localhost:8080/api/articles';
  private commentsUrl = 'http://localhost:8080/api/comments';
  private destroy$ = new Subject<void>();

  constructor(private http: HttpClient) {}

  getArticlesBySubscribedThemes(userId: number): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/subscribed/${userId}`).pipe(takeUntil(this.destroy$));
  }

  createArticle(articleData: ArticleFormData): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, articleData).pipe(takeUntil(this.destroy$));
  }

  getArticleById(articleId: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${articleId}`).pipe(takeUntil(this.destroy$));
  }

  addComment(articleId: number, userId: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(this.commentsUrl, {
      articleId: articleId,
      userId: userId,
      content: content
    }).pipe(takeUntil(this.destroy$));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}