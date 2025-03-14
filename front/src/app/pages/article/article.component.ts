import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  articleId: number | null = null;
  article: any = {}; 
  comments: any[] = []; 
  newComment: string = "";

  constructor(private route: ActivatedRoute, private location: Location, private articleService: ArticleService) {}

  ngOnInit(): void {
    this.articleId = Number(this.route.snapshot.paramMap.get('id'));

    this.articleService.getArticleById(this.articleId).subscribe({
      next: (response: any) => {
        console.log("Article reçu :", response);
        
        this.article = {
          title: response.title,
          date: response.createdAt,
          author: response.authorUsername,
          theme: response.theme ? response.theme.title : "Aucun thème", 
          content: response.content
        };

        // Mettre à jour les commentaires
        this.comments = response.comments.map((comment: any) => ({
          username: comment.senderUsername,
          content: comment.content
        }));
      },
      error: (error: any) => {
        console.error("Erreur lors de la récupération de l'article", error);
      }
    });
  }

  goBack() {
    this.location.back();
  }

  addComment() {
    if (this.newComment.trim() !== "") {
      this.comments.push({ username: "Moi", content: this.newComment });
      this.newComment = "";
    }
  }
}
