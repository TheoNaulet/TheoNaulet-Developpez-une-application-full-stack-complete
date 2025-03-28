import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-custom-input',
  templateUrl: './custom-input.component.html',
  styleUrls: ['./custom-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomInputComponent),
      multi: true
    }
  ]
})
export class CustomInputComponent implements ControlValueAccessor {
  @Input() label?: string;
  @Input() placeholder: string = ''; 
  @Input() type: string = 'text';
  @Input() borderColor: string = '#ccc';

  public innerValue: string = ''; 

  onChange = (value: string) => {};
  onTouched = () => {};

  writeValue(value: string): void {
    this.innerValue = value || ''; 
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setValue(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.innerValue = input.value;
    this.onChange(this.innerValue);
  }
}
