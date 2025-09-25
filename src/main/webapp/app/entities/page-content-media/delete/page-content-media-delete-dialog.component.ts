import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPageContentMedia } from '../page-content-media.model';
import { PageContentMediaService } from '../service/page-content-media.service';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  templateUrl: './page-content-media-delete-dialog.component.html',
  imports: [TranslateDirective, AlertErrorComponent, SharedModule, FormsModule],
})
export class PageContentMediaDeleteDialogComponent {
  pageContentMedia?: IPageContentMedia;

  protected pageContentMediaService = inject(PageContentMediaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageContentMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
