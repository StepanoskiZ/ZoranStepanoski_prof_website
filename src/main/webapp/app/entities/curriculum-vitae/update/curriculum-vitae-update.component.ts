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
import { WorkingStatus } from 'app/entities/enumerations/working-status.model';
import { WorkingType } from 'app/entities/enumerations/working-type.model';
import { CurriculumVitaeService } from '../service/curriculum-vitae.service';
import { ICurriculumVitae } from '../curriculum-vitae.model';
import { CurriculumVitaeFormService, CurriculumVitaeFormGroup } from './curriculum-vitae-form.service';
import { QuillModule } from 'ngx-quill';
import Delta from 'quill-delta';
type DeltaStatic = Delta;

@Component({
  standalone: true,
  selector: 'jhi-curriculum-vitae-update',
  templateUrl: './curriculum-vitae-update.component.html',
  imports: [QuillModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export class CurriculumVitaeUpdateComponent implements OnInit {
  isSaving = false;
  curriculumVitae: ICurriculumVitae | null = null;
  languageValues = Object.keys(Language);
  workingStatusValues = Object.keys(WorkingStatus);
  workingTypeValues = Object.keys(WorkingType);

  editForm: CurriculumVitaeFormGroup = this.curriculumVitaeFormService.createCurriculumVitaeFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected curriculumVitaeService: CurriculumVitaeService,
    protected curriculumVitaeFormService: CurriculumVitaeFormService,
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
    this.activatedRoute.data.subscribe(({ curriculumVitae }) => {
      this.curriculumVitae = curriculumVitae;
      if (curriculumVitae) {
        this.updateForm(curriculumVitae);
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
    const curriculumVitae = this.curriculumVitaeFormService.getCurriculumVitae(this.editForm);
    if (curriculumVitae.id !== null) {
      this.subscribeToSaveResponse(this.curriculumVitaeService.update(curriculumVitae));
    } else {
      this.subscribeToSaveResponse(this.curriculumVitaeService.create(curriculumVitae));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurriculumVitae>>): void {
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

  protected updateForm(curriculumVitae: ICurriculumVitae): void {
    this.curriculumVitae = curriculumVitae;
    this.curriculumVitaeFormService.resetForm(this.editForm, curriculumVitae);
  }
}
