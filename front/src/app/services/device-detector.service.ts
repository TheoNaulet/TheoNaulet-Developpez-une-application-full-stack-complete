import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, fromEvent } from 'rxjs';
import { debounceTime, distinctUntilChanged, startWith } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DeviceDetectorService {
  private readonly MOBILE_BREAKPOINT = 768;
  private isMobileSubject = new BehaviorSubject<boolean>(window.innerWidth <= this.MOBILE_BREAKPOINT);
  
  public isMobile$: Observable<boolean> = this.isMobileSubject.asObservable();

  constructor() {
    this.initResizeListener();
  }

  private initResizeListener(): void {
    fromEvent(window, 'resize')
      .pipe(
        debounceTime(300),
        startWith(null),
        distinctUntilChanged()
      )
      .subscribe(() => {
        const isMobile = window.innerWidth <= this.MOBILE_BREAKPOINT;
        this.isMobileSubject.next(isMobile);
      });
  }

  public isMobile(): boolean {
    return window.innerWidth <= this.MOBILE_BREAKPOINT;
  }

  public getDeviceType(): 'desktop' | 'mobile' {
    return this.isMobile() ? 'mobile' : 'desktop';
  }
}
