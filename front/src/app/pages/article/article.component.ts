import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ArticleService } from 'src/app/services/article.service';
import { AuthService } from 'src/app/services/auth.service';
import { Article, ArticleDisplay, Comment, CommentDisplay } from 'src/app/models/article.model';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  articleId: number | null = null;
  article: ArticleDisplay = {
    title: '',
    date: '',
    author: '',
    theme: '',
    content: ''
  }; 
  comments: CommentDisplay[] = []; 
  newComment: string = "";
  currentUserId: number = 0;

  constructor(
    private route: ActivatedRoute, 
    private location: Location, 
    private articleService: ArticleService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.getCurrentUserId();
    this.loadArticleData();
  }

  getCurrentUserId(): void {
    this.currentUserId = this.authService.getCurrentUserId();
  }

  loadArticleData(): void {
    if (!this.articleId) return;

    this.articleService.getArticleById(this.articleId).subscribe({
      next: (response: Article) => {
        this.article = {
          title: response.title,
          date: response.createdAt,
          author: response.authorUsername || '',
          theme: response.themeTitle ? response.themeTitle : "Aucun thème", 
          content: response.content,
        };

        // Update comments
        this.comments = response.comments?.map((comment: Comment) => ({
          username: comment.senderUsername || '',
          content: comment.content
        })) || [];
      },
      error: (error: Error) => {
        console.error("Erreur lors de la récupération de l'article", error);
      }
    });
  }

  goBack() {
    this.location.back();
  }

  addComment() {
    if (this.newComment.trim() === "" || !this.articleId) return;
  
    this.articleService.addComment(this.articleId, this.currentUserId, this.newComment).subscribe({
      next: (response: Comment) => {
  
        this.comments.push({
          username: response.senderUsername || "Moi",
          content: this.newComment
        });
  
        this.newComment = "";
      },
      error: (error: Error) => {
        console.error("Erreur lors de l'ajout du commentaire", error);
      }
    });
  }
}
