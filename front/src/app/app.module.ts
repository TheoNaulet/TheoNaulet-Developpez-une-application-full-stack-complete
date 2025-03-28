import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { ArticleComponent } from './pages/article/article.component';
import { CreateArticleComponent } from './pages/article/create-article/create-article.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { MeComponent } from './pages/me/me.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CustomButtonComponent } from './components/custom-button/custom-button.component';
import { CustomInputComponent } from './components/custom-input/custom-input.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { ArticleCardComponent } from './components/article-card/article-card.component';
import { ThemeCardComponent } from './components/theme-card/theme-card.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './services/auth.interceptor';
import { AuthGuard } from './services/auth.guard';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { TruncatePipe } from './pipes/truncate.pipe';

const routes: Routes = [
  { path: 'article/create', component: CreateArticleComponent,canActivate: [AuthGuard] },
  { path: 'article/:id', component: ArticleComponent,canActivate: [AuthGuard] },
  {path: 'articles', component: ArticlesComponent,canActivate: [AuthGuard]},
  { path: 'themes', component: ThemesComponent,canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'me', component: MeComponent },
];

@NgModule({
  declarations: [AppComponent, HomeComponent, ArticleComponent, CreateArticleComponent, ThemesComponent, LoginComponent, SignupComponent, MeComponent, CustomButtonComponent, CustomInputComponent, NavbarComponent, ArticlesComponent, ArticleCardComponent, ThemeCardComponent, MeComponent, NotFoundComponent, TruncatePipe],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    FormsModule,
    MatIconModule,       
    MatFormFieldModule,  
    MatInputModule,
    MatButtonModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule, CustomButtonComponent],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },],
  bootstrap: [AppComponent],
})
export class AppModule {}
