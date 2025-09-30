import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Language } from 'app/entities/enumerations/language.model';
import { AboutMeService } from '../service/about-me.service';
import { IAboutMe } from '../about-me.model';
import { AboutMeFormService, AboutMeFormGroup } from './about-me-form.service';
import { QuillModule } from 'ngx-quill';
import Delta from 'quill-delta';
type DeltaStatic = Delta;

@Component({
  standalone: true,
  selector: 'jhi-about-me-update',
  templateUrl: './about-me-update.component.html',
  imports: [QuillModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export class AboutMeUpdateComponent implements OnInit {
  isSaving = false;
  aboutMe: IAboutMe | null = null;
  languageValues = Object.keys(Language);

  editForm: AboutMeFormGroup = this.aboutMeFormService.createAboutMeFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected aboutMeService: AboutMeService,
    protected aboutMeFormService: AboutMeFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  editorModules = {
    toolbar: [
      [{ header: [1, 2, 3, false] }],
      ['bold', 'italic', 'underline'],
      ['link', 'image', 'video', 'code-block'],
    ],
  };

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutMe }) => {
      this.aboutMe = aboutMe;
      if (aboutMe) {
        this.updateForm(aboutMe);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('zsWebsiteApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aboutMe = this.aboutMeFormService.getAboutMe(this.editForm);
    if (aboutMe.id !== null) {
      this.subscribeToSaveResponse(this.aboutMeService.update(aboutMe));
    } else {
      this.subscribeToSaveResponse(this.aboutMeService.create(aboutMe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAboutMe>>): void {
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

  protected updateForm(aboutMe: IAboutMe): void {
    this.aboutMe = aboutMe;
    this.aboutMeFormService.resetForm(this.editForm, aboutMe);
  }
}
