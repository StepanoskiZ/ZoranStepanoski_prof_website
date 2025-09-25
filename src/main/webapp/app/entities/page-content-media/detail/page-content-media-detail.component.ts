import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPageContentMedia } from '../page-content-media.model';

@Component({
  selector: 'jhi-page-content-media-detail',
  templateUrl: './page-content-media-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PageContentMediaDetailComponent {
  pageContentMedia = input<IPageContentMedia | null>(null);

  previousState(): void {
    window.history.back();
  }
}
