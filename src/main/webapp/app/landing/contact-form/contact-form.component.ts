import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { NewContactMessage } from 'app/entities/contact-message/contact-message.model';
import { ContactMessageService } from 'app/entities/contact-message/service/contact-message.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-contact-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SharedModule],
  templateUrl: './contact-form.component.html',
  styleUrl: './contact-form.component.scss',
})
export class ContactFormComponent {
  isSubmitting = false;
  isError = false;
  profanityError = false;

  private fb = inject(FormBuilder);
  private contactMessageService = inject(ContactMessageService);
  private modalService = inject(NgbModal);

  editForm = this.fb.group({
    visitorName: ['', [Validators.required]],
    visitorEmail: ['', [Validators.required, Validators.email]],
    message: ['', [Validators.required, Validators.minLength(10)]],
  });

  submit(successModalTemplate: any): void {
    this.isError = false;
    this.profanityError = false;

    // First, check standard validation (required fields, email format, etc.)
    if (this.editForm.invalid) {
      Object.values(this.editForm.controls).forEach(control => control.markAsTouched());
      return;
    }

    // --- START OF PROFANITY CHECK (ON SUBMIT) ---
    const forbiddenWords = [
      // English
      'dick',
      'kock suck',
      'kock sucker',
      'kock',
      'blowjob',
      'pussy',
      'ass',
      'asshole',
      'bitch',
      'cunt',
      'fuck',
      // Serbian (Cyrillic and Latin)
      'kurac',
      'qrac',
      'курац',
      'пичка',
      'пичкица',
      'пичкице',
      'pichka',
      'jebem',
      'јебем',
      'govno',
      'говно',
      'sranje',
      'срање',
      'serem',
      'sranje',
      'pisa',
      'pisam',
      'pisas',
      'piša',
      'пиша',
      'pišam',
      'пишам',
      'pišaš',
      'пишаш',
      'picka',
      'pička',
      'пичка',
      'pickica',
      'pičkica',
      'pickice',
      'pičkice',
      'jebacu',
      'jebaću',
      'јебаћу',
    ];

    const messageValue = this.editForm.get('message')?.value ?? '';
    // 1. YES, WE CONVERT THE WHOLE MESSAGE TO LOWERCASE HERE
    const lowerCaseMessage = messageValue.toLowerCase();

    const hasForbiddenWord = forbiddenWords.some(word => lowerCaseMessage.includes(word));

    if (hasForbiddenWord) {
      this.profanityError = true;
      return;
    }
    this.isSubmitting = true;

    const contactMessage: NewContactMessage = {
      id: null,
      ...this.editForm.getRawValue(),
      submittedDate: dayjs(),
    };

    this.contactMessageService.create(contactMessage).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.editForm.reset();
        this.modalService.open(successModalTemplate, { centered: true });
      },
      error: () => {
        this.isSubmitting = false;
        this.isError = true;
      },
    });
  }
}
