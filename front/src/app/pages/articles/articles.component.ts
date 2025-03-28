import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleService } from 'src/app/services/article.service';
import { AuthService } from 'src/app/services/auth.service';
import { Article } from 'src/app/models/article.model';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss'],
})
export class ArticlesComponent implements OnInit {
  
  articles: Article[] = [];
  isDescending: boolean = true;

  constructor(
    private router: Router, 
    private articleService: ArticleService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    console.log("ArticlesComponent initialized");
    this.loadArticles();
  }

  loadArticles() {
    const userId = this.authService.getCurrentUserId();

    this.articleService.getArticlesBySubscribedThemes(userId).subscribe({
      next: (response: Article[]) => {
        console.log("Articles reçus :", response);
        this.articles = response; 
      },
      error: (error: Error) => {
        console.error("Erreur lors de la récupération des articles", error);
      }
    });
  }

  sortByDate() {
    this.articles.sort((a, b) => {
      return this.isDescending
        ? new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime() 
        : new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
    });
  
    this.isDescending = !this.isDescending;
  }

  goToCreateArticle() {
    this.router.navigate(['/article/create']);
  }
}
