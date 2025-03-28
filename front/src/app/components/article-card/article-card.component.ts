import { Component, Input } from '@angular/core';
import { Article } from 'src/app/models/article.model';

@Component({
  selector: 'app-article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
})
export class ArticleCardComponent {
  @Input() article: Article={
    id: 0,
    title: '',
    content: '',
    themeId: undefined,
    createdAt: '',
    updatedAt: ''
  };
}
