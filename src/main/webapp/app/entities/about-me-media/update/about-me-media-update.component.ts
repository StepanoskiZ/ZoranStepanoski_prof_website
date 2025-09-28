import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAboutMe } from 'app/entities/about-me/about-me.model';
import { AboutMeService } from 'app/entities/about-me/service/about-me.service';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';
import { AboutMeMediaService } from '../service/about-me-media.service';
import { IAboutMeMedia } from '../about-me-media.model';
import { AboutMeMediaFormService, AboutMeMediaFormGroup } from './about-me-media-form.service';

@Component({
  standalone: true,
  selector: 'jhi-about-me-media-update',
  templateUrl: './about-me-media-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AboutMeMediaUpdateComponent implements OnInit {
  isSaving = false;
  aboutMeMedia: IAboutMeMedia | null = null;
  unifiedMediaTypeValues = Object.keys(UnifiedMediaType);

  aboutMesSharedCollection: IAboutMe[] = [];

  editForm: AboutMeMediaFormGroup = this.aboutMeMediaFormService.createAboutMeMediaFormGroup();

  constructor(
    protected aboutMeMediaService: AboutMeMediaService,
    protected aboutMeMediaFormService: AboutMeMediaFormService,
    protected aboutMeService: AboutMeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAboutMe = (o1: IAboutMe | null, o2: IAboutMe | null): boolean => this.aboutMeService.compareAboutMe(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutMeMedia }) => {
      this.aboutMeMedia = aboutMeMedia;
      if (aboutMeMedia) {
        this.updateForm(aboutMeMedia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aboutMeMedia = this.aboutMeMediaFormService.getAboutMeMedia(this.editForm);
    if (aboutMeMedia.id !== null) {
      this.subscribeToSaveResponse(this.aboutMeMediaService.update(aboutMeMedia));
    } else {
      this.subscribeToSaveResponse(this.aboutMeMediaService.create(aboutMeMedia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAboutMeMedia>>): void {
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

  protected updateForm(aboutMeMedia: IAboutMeMedia): void {
    this.aboutMeMedia = aboutMeMedia;
    this.aboutMeMediaFormService.resetForm(this.editForm, aboutMeMedia);

    this.aboutMesSharedCollection = this.aboutMeService.addAboutMeToCollectionIfMissing<IAboutMe>(
      this.aboutMesSharedCollection,
      aboutMeMedia.aboutMe,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aboutMeService
      .query()
      .pipe(map((res: HttpResponse<IAboutMe[]>) => res.body ?? []))
      .pipe(
        map((aboutMes: IAboutMe[]) => this.aboutMeService.addAboutMeToCollectionIfMissing<IAboutMe>(aboutMes, this.aboutMeMedia?.aboutMe)),
      )
      .subscribe((aboutMes: IAboutMe[]) => (this.aboutMesSharedCollection = aboutMes));
  }
}
