import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-custom-button',
  templateUrl: './custom-button.component.html',
  styleUrls: ['./custom-button.component.scss'],
})
export class CustomButtonComponent {
  @Input() label: string = 'Bouton';
  @Input() type: 'primary' | 'secondary' | 'outline' = 'primary';
  @Input() disabled: boolean = false;
  @Input() width: string = 'auto';
  @Output() clickEvent = new EventEmitter<void>();

  onClick() {
    if (!this.disabled) {
      this.clickEvent.emit();
    }
  }
}
