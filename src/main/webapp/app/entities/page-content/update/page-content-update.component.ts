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
import { PageContentService } from '../service/page-content.service';
import { IPageContent } from '../page-content.model';
import { PageContentFormGroup, PageContentFormService } from './page-content-form.service';

@Component({
  selector: 'jhi-page-content-update',
  templateUrl: './page-content-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PageContentUpdateComponent implements OnInit {
  isSaving = false;
  pageContent: IPageContent | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected pageContentService = inject(PageContentService);
  protected pageContentFormService = inject(PageContentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PageContentFormGroup = this.pageContentFormService.createPageContentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageContent }) => {
      this.pageContent = pageContent;
      if (pageContent) {
        this.updateForm(pageContent);
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
    const pageContent = this.pageContentFormService.getPageContent(this.editForm);
    if (pageContent.id !== null) {
      this.subscribeToSaveResponse(this.pageContentService.update(pageContent));
    } else {
      this.subscribeToSaveResponse(this.pageContentService.create(pageContent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageContent>>): void {
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

  protected updateForm(pageContent: IPageContent): void {
    this.pageContent = pageContent;
    this.pageContentFormService.resetForm(this.editForm, pageContent);
  }
}
