import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVisitorLog } from '../visitor-log.model';
import { VisitorLogService } from '../service/visitor-log.service';
import { VisitorLogFormService, VisitorLogFormGroup } from './visitor-log-form.service';

@Component({
  selector: 'jhi-visitor-log-update',
  standalone: true,
  templateUrl: './visitor-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VisitorLogUpdateComponent implements OnInit {
  isSaving = false;
  visitorLog: IVisitorLog | null = null;

  editForm: VisitorLogFormGroup = this.visitorLogFormService.createVisitorLogFormGroup();

  constructor(
    protected visitorLogService: VisitorLogService,
    protected visitorLogFormService: VisitorLogFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visitorLog }) => {
      this.visitorLog = visitorLog;
      if (visitorLog) {
        this.updateForm(visitorLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visitorLog = this.visitorLogFormService.getVisitorLog(this.editForm);
    if (visitorLog.id !== null) {
      this.subscribeToSaveResponse(this.visitorLogService.update(visitorLog));
    } else {
      this.subscribeToSaveResponse(this.visitorLogService.create(visitorLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisitorLog>>): void {
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

  protected updateForm(visitorLog: IVisitorLog): void {
    this.visitorLog = visitorLog;
    this.visitorLogFormService.resetForm(this.editForm, visitorLog);
  }
}
