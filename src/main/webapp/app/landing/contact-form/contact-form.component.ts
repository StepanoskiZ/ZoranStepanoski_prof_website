import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { TranslateDirective } from 'app/shared/language';
import { IContactMessage, NewContactMessage } from 'app/entities/contact-message/contact-message.model';
import { ContactMessageService } from 'app/entities/contact-message/service/contact-message.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-contact-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateDirective],
  templateUrl: './contact-form.component.html',
  styleUrl: './contact-form.component.scss',
})
export class ContactFormComponent {
  isSubmitting = false;
  isSuccess = false;
  isError = false;

  private fb = inject(FormBuilder);
  private contactMessageService = inject(ContactMessageService);

  editForm = this.fb.group({
    visitorName: ['', [Validators.required]],
    visitorEmail: ['', [Validators.required, Validators.email]],
    message: ['', [Validators.required, Validators.minLength(10)]],
  });

  submit(): void {
    if (this.editForm.invalid) {
      // Mark all fields as touched to show validation errors
      Object.values(this.editForm.controls).forEach(control => {
        control.markAsTouched();
      });
      return;
    }

    this.isSubmitting = true;
    this.isSuccess = false;
    this.isError = false;

    // 2. USE THE 'NewContactMessage' TYPE HERE
    const contactMessage: NewContactMessage = {
      id: null, // Explicitly set id to null for a new message
      ...this.editForm.getRawValue(),
      submittedDate: dayjs(),
    };

    this.contactMessageService.create(contactMessage).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.isSuccess = true;
        this.editForm.reset();
      },
      error: () => {
        this.isSubmitting = false;
        this.isError = true;
      },
    });
  }
}
