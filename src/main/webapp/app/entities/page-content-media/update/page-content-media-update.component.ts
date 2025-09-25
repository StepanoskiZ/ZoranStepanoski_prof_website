import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPageContent } from 'app/entities/page-content/page-content.model';
import { PageContentService } from 'app/entities/page-content/service/page-content.service';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';
import { PageContentMediaService } from '../service/page-content-media.service';
import { IPageContentMedia } from '../page-content-media.model';
import { PageContentMediaFormGroup, PageContentMediaFormService } from './page-content-media-form.service';

@Component({
  selector: 'jhi-page-content-media-update',
  templateUrl: './page-content-media-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PageContentMediaUpdateComponent implements OnInit {
  isSaving = false;
  pageContentMedia: IPageContentMedia | null = null;
  unifiedMediaTypeValues = Object.keys(UnifiedMediaType);

  pageContentsSharedCollection: IPageContent[] = [];

  protected pageContentMediaService = inject(PageContentMediaService);
  protected pageContentMediaFormService = inject(PageContentMediaFormService);
  protected pageContentService = inject(PageContentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PageContentMediaFormGroup = this.pageContentMediaFormService.createPageContentMediaFormGroup();

  comparePageContent = (o1: IPageContent | null, o2: IPageContent | null): boolean => this.pageContentService.comparePageContent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageContentMedia }) => {
      this.pageContentMedia = pageContentMedia;
      if (pageContentMedia) {
        this.updateForm(pageContentMedia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pageContentMedia = this.pageContentMediaFormService.getPageContentMedia(this.editForm);
    if (pageContentMedia.id !== null) {
      this.subscribeToSaveResponse(this.pageContentMediaService.update(pageContentMedia));
    } else {
      this.subscribeToSaveResponse(this.pageContentMediaService.create(pageContentMedia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageContentMedia>>): void {
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

  protected updateForm(pageContentMedia: IPageContentMedia): void {
    this.pageContentMedia = pageContentMedia;
    this.pageContentMediaFormService.resetForm(this.editForm, pageContentMedia);

    this.pageContentsSharedCollection = this.pageContentService.addPageContentToCollectionIfMissing<IPageContent>(
      this.pageContentsSharedCollection,
      pageContentMedia.pagecontent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pageContentService
      .query()
      .pipe(map((res: HttpResponse<IPageContent[]>) => res.body ?? []))
      .pipe(
        map((pageContents: IPageContent[]) =>
          this.pageContentService.addPageContentToCollectionIfMissing<IPageContent>(pageContents, this.pageContentMedia?.pagecontent),
        ),
      )
      .subscribe((pageContents: IPageContent[]) => (this.pageContentsSharedCollection = pageContents));
  }
}
