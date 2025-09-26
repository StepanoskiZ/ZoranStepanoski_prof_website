import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPageContentMedia } from '../page-content-media.model';

@Component({
  selector: 'jhi-page-content-media-detail',
  standalone: true,
  templateUrl: './page-content-media-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PageContentMediaDetailComponent {
  @Input() pageContentMedia: IPageContentMedia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
