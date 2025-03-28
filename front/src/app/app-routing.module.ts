import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { ArticleComponent } from './pages/article/article.component';
import { CreateArticleComponent } from './pages/article/create-article/create-article.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { MeComponent } from './pages/me/me.component';
import { AuthGuard } from './guards/auth.guard';
import { LoginGuard } from './guards/login.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  { path: 'signup', component: SignupComponent, canActivate: [LoginGuard] },
  { path: 'articles', component: ArticlesComponent, canActivate: [AuthGuard] },
  { path: 'article/:id', component: ArticleComponent, canActivate: [AuthGuard] },
  { path: 'create-article', component: CreateArticleComponent, canActivate: [AuthGuard] },
  { path: 'themes', component: ThemesComponent, canActivate: [AuthGuard] },
  { path: 'me', component: MeComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' } // Redirection vers la page d'accueil pour les routes inconnues
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
