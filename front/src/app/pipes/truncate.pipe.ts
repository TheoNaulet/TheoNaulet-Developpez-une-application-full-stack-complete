import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  transform(value: string, deviceType: 'desktop' | 'mobile' = 'desktop'): string {
    if (!value) {
      return '';
    }

    // Définir les longueurs maximales pour desktop et mobile
    const maxLength = deviceType === 'desktop' ? 150 : 100;
    
    // Tronquer le texte si nécessaire
    if (value.length > maxLength) {
      return value.substring(0, maxLength) + '...';
    }
    
    return value;
  }
}
