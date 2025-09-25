import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPageContentMedia } from '../page-content-media.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'jhi-page-content-media-detail',
  templateUrl: './page-content-media-detail.component.html',
  imports: [TranslateDirective, AlertComponent, AlertErrorComponent, SharedModule, RouterModule],
})
export class PageContentMediaDetailComponent {
  pageContentMedia = input<IPageContentMedia | null>(null);

  previousState(): void {
    window.history.back();
  }
}
