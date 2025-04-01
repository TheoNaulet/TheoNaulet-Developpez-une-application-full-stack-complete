import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Article } from 'src/app/models/article.model';
import { DeviceDetectorService } from 'src/app/services/device-detector.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
})
export class ArticleCardComponent implements OnInit, OnDestroy {
  @Input() article: Article = {
    id: 0,
    title: '',
    content: '',
    themeId: undefined,
    createdAt: '',
    updatedAt: ''
  };

  deviceType: 'desktop' | 'mobile' = 'desktop';
  private destroy$ = new Subject<void>();

  constructor(private deviceDetectorService: DeviceDetectorService) {}

  ngOnInit(): void {
    // Définir le type d'appareil initial
    this.deviceType = this.deviceDetectorService.getDeviceType();

    // S'abonner aux changements de taille d'écran
    this.deviceDetectorService.isMobile$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isMobile => {
        this.deviceType = isMobile ? 'mobile' : 'desktop';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
