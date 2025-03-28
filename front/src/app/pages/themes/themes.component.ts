import { Component, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/services/theme.service';
import { Theme } from 'src/app/models/theme.model';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss'],
})
export class ThemesComponent implements OnInit {
  constructor(private themeService: ThemeService) {}
  themes: Theme[] = [];

  ngOnInit(): void {
    this.themeService.getAllThemes().subscribe({
      next: (response: Theme[]) => {
        this.themes = response;
      },
      error: (error: Error) => {
        console.error(error);
      }
    });
  }
}
