import { Component } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss'],
})
export class ThemesComponent {
  constructor( private themeService: ThemeService) {}
  themes: string[] = [];

  ngOnInit() {
    this.themeService.getAllThemes().subscribe({
      next: (response) => {
        this.themes = response;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }
}
