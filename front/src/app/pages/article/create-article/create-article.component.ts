import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ArticleService } from 'src/app/services/article.service';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {
  
  themes: any[] = []; // Liste des thèmes récupérés
  article = {
    themeId: undefined,
    title: '',
    content: ''
  };

  constructor(private location: Location, private articleService: ArticleService, private themeService: ThemeService) {}

  ngOnInit() {
    this.loadThemes();
  }

  loadThemes() {
    this.themeService.getAllThemes().subscribe({
      next: (response) => {
        this.themes = response;
        console.log("Thèmes récupérés :", this.themes);
      },
      error: (error) => {
        console.error("Erreur lors de la récupération des thèmes", error);
      }
    });
  }

  goBack() {
    this.location.back();
  }

  createArticle() {
    if (!this.article.themeId) {
      alert("Veuillez sélectionner un thème !");
      return;
    }

    console.log("Création de l'article :", this.article);

    this.articleService.createArticle(this.article).subscribe({
      next: (response) => {
        console.log("Article créé avec succès :", response);
        this.goBack();
      },
      error: (error) => {
        console.error("Erreur lors de la création de l'article", error);
      }
    });
  }
}
