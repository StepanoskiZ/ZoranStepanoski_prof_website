import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusinessService } from 'app/entities/business-service/business-service.model';
import { BusinessServiceService } from 'app/entities/business-service/service/business-service.service';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';
import { BusinessServiceMediaService } from '../service/business-service-media.service';
import { IBusinessServiceMedia } from '../business-service-media.model';
import { BusinessServiceMediaFormService, BusinessServiceMediaFormGroup } from './business-service-media-form.service';

@Component({
  standalone: true,
  selector: 'jhi-business-service-media-update',
  templateUrl: './business-service-media-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessServiceMediaUpdateComponent implements OnInit {
  isSaving = false;
  businessServiceMedia: IBusinessServiceMedia | null = null;
  unifiedMediaTypeValues = Object.keys(UnifiedMediaType);

  businessServicesSharedCollection: IBusinessService[] = [];

  editForm: BusinessServiceMediaFormGroup = this.businessServiceMediaFormService.createBusinessServiceMediaFormGroup();

  constructor(
    protected businessServiceMediaService: BusinessServiceMediaService,
    protected businessServiceMediaFormService: BusinessServiceMediaFormService,
    protected businessServiceService: BusinessServiceService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareBusinessService = (o1: IBusinessService | null, o2: IBusinessService | null): boolean =>
    this.businessServiceService.compareBusinessService(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessServiceMedia }) => {
      this.businessServiceMedia = businessServiceMedia;
      if (businessServiceMedia) {
        this.updateForm(businessServiceMedia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessServiceMedia = this.businessServiceMediaFormService.getBusinessServiceMedia(this.editForm);
    if (businessServiceMedia.id !== null) {
      this.subscribeToSaveResponse(this.businessServiceMediaService.update(businessServiceMedia));
    } else {
      this.subscribeToSaveResponse(this.businessServiceMediaService.create(businessServiceMedia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessServiceMedia>>): void {
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

  protected updateForm(businessServiceMedia: IBusinessServiceMedia): void {
    this.businessServiceMedia = businessServiceMedia;
    this.businessServiceMediaFormService.resetForm(this.editForm, businessServiceMedia);

    this.businessServicesSharedCollection = this.businessServiceService.addBusinessServiceToCollectionIfMissing<IBusinessService>(
      this.businessServicesSharedCollection,
      businessServiceMedia.businessService,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessServiceService
      .query()
      .pipe(map((res: HttpResponse<IBusinessService[]>) => res.body ?? []))
      .pipe(
        map((businessServices: IBusinessService[]) =>
          this.businessServiceService.addBusinessServiceToCollectionIfMissing<IBusinessService>(
            businessServices,
            this.businessServiceMedia?.businessService,
          ),
        ),
      )
      .subscribe((businessServices: IBusinessService[]) => (this.businessServicesSharedCollection = businessServices));
  }
}
