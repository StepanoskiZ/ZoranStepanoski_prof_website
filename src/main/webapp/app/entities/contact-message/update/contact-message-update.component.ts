import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ContactMessageService } from '../service/contact-message.service';
import { IContactMessage } from '../contact-message.model';
import { ContactMessageFormGroup, ContactMessageFormService } from './contact-message-form.service';

@Component({
  selector: 'jhi-contact-message-update',
  templateUrl: './contact-message-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContactMessageUpdateComponent implements OnInit {
  isSaving = false;
  contactMessage: IContactMessage | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected contactMessageService = inject(ContactMessageService);
  protected contactMessageFormService = inject(ContactMessageFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContactMessageFormGroup = this.contactMessageFormService.createContactMessageFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactMessage }) => {
      this.contactMessage = contactMessage;
      if (contactMessage) {
        this.updateForm(contactMessage);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('zsWebsiteApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactMessage = this.contactMessageFormService.getContactMessage(this.editForm);
    if (contactMessage.id !== null) {
      this.subscribeToSaveResponse(this.contactMessageService.update(contactMessage));
    } else {
      this.subscribeToSaveResponse(this.contactMessageService.create(contactMessage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contactMessage: IContactMessage): void {
    this.contactMessage = contactMessage;
    this.contactMessageFormService.resetForm(this.editForm, contactMessage);
  }
}
