import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICurriculumVitae } from 'app/entities/curriculum-vitae/curriculum-vitae.model';
import { CurriculumVitaeService } from 'app/entities/curriculum-vitae/service/curriculum-vitae.service';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';
import { CurriculumVitaeMediaService } from '../service/curriculum-vitae-media.service';
import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';
import { CurriculumVitaeMediaFormService, CurriculumVitaeMediaFormGroup } from './curriculum-vitae-media-form.service';

@Component({
  standalone: true,
  selector: 'jhi-curriculum-vitae-media-update',
  templateUrl: './curriculum-vitae-media-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CurriculumVitaeMediaUpdateComponent implements OnInit {
  isSaving = false;
  curriculumVitaeMedia: ICurriculumVitaeMedia | null = null;
  unifiedMediaTypeValues = Object.keys(UnifiedMediaType);

  curriculumVitaesSharedCollection: ICurriculumVitae[] = [];

  editForm: CurriculumVitaeMediaFormGroup = this.curriculumVitaeMediaFormService.createCurriculumVitaeMediaFormGroup();

  constructor(
    protected curriculumVitaeMediaService: CurriculumVitaeMediaService,
    protected curriculumVitaeMediaFormService: CurriculumVitaeMediaFormService,
    protected curriculumVitaeService: CurriculumVitaeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCurriculumVitae = (o1: ICurriculumVitae | null, o2: ICurriculumVitae | null): boolean =>
    this.curriculumVitaeService.compareCurriculumVitae(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ curriculumVitaeMedia }) => {
      this.curriculumVitaeMedia = curriculumVitaeMedia;
      if (curriculumVitaeMedia) {
        this.updateForm(curriculumVitaeMedia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const curriculumVitaeMedia = this.curriculumVitaeMediaFormService.getCurriculumVitaeMedia(this.editForm);
    if (curriculumVitaeMedia.id !== null) {
      this.subscribeToSaveResponse(this.curriculumVitaeMediaService.update(curriculumVitaeMedia));
    } else {
      this.subscribeToSaveResponse(this.curriculumVitaeMediaService.create(curriculumVitaeMedia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurriculumVitaeMedia>>): void {
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

  protected updateForm(curriculumVitaeMedia: ICurriculumVitaeMedia): void {
    this.curriculumVitaeMedia = curriculumVitaeMedia;
    this.curriculumVitaeMediaFormService.resetForm(this.editForm, curriculumVitaeMedia);

    this.curriculumVitaesSharedCollection = this.curriculumVitaeService.addCurriculumVitaeToCollectionIfMissing<ICurriculumVitae>(
      this.curriculumVitaesSharedCollection,
      curriculumVitaeMedia.curriculumVitae,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.curriculumVitaeService
      .query()
      .pipe(map((res: HttpResponse<ICurriculumVitae[]>) => res.body ?? []))
      .pipe(
        map((curriculumVitaes: ICurriculumVitae[]) =>
          this.curriculumVitaeService.addCurriculumVitaeToCollectionIfMissing<ICurriculumVitae>(
            curriculumVitaes,
            this.curriculumVitaeMedia?.curriculumVitae,
          ),
        ),
      )
      .subscribe((curriculumVitaes: ICurriculumVitae[]) => (this.curriculumVitaesSharedCollection = curriculumVitaes));
  }
}
