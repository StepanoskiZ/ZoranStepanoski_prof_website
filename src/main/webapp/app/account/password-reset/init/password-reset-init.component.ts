import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, inject, signal, viewChild } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import SharedModule from 'app/shared/shared.module';

import { PasswordResetInitService } from './password-reset-init.service';
import { AlertErrorComponent } from '../../../shared/alert/alert-error.component';

@Component({
  selector: 'jhi-password-reset-init',
  standalone: true,
  imports: [SharedModule, AlertErrorComponent, FormsModule, ReactiveFormsModule],
  templateUrl: './password-reset-init.component.html',
})
export default class PasswordResetInitComponent implements AfterViewInit {
  email = viewChild<ElementRef>('email');

  success = signal(false);
  resetRequestForm;

  private readonly passwordResetInitService = inject(PasswordResetInitService);
  private readonly fb = inject(FormBuilder);

  constructor() {
    this.resetRequestForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    });
  }

  ngAfterViewInit(): void {
    const emailEl = this.email();
    if (emailEl) {
      emailEl.nativeElement.focus();
    }
  }

  requestReset(): void {
    this.passwordResetInitService.save(this.resetRequestForm.get(['email'])!.value).subscribe(() => this.success.set(true));
  }
}
