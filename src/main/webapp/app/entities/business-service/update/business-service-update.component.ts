import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusinessService } from '../business-service.model';
import { BusinessServiceService } from '../service/business-service.service';
import { BusinessServiceFormService, BusinessServiceFormGroup } from './business-service-form.service';
import { QuillModule } from 'ngx-quill';

@Component({
  selector: 'jhi-business-service-update',
  standalone: true,
  templateUrl: './business-service-update.component.html',
  imports: [QuillModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessServiceUpdateComponent implements OnInit {
  isSaving = false;
  businessService: IBusinessService | null = null;

  editForm: BusinessServiceFormGroup = this.businessServiceFormService.createBusinessServiceFormGroup();

  constructor(
    protected businessServiceService: BusinessServiceService,
    protected businessServiceFormService: BusinessServiceFormService,
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
    this.activatedRoute.data.subscribe(({ businessService }) => {
      this.businessService = businessService;
      if (businessService) {
        this.updateForm(businessService);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessService = this.businessServiceFormService.getBusinessService(this.editForm);
    if (businessService.id !== null) {
      this.subscribeToSaveResponse(this.businessServiceService.update(businessService));
    } else {
      this.subscribeToSaveResponse(this.businessServiceService.create(businessService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessService>>): void {
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

  protected updateForm(businessService: IBusinessService): void {
    this.businessService = businessService;
    this.businessServiceFormService.resetForm(this.editForm, businessService);
  }
}
