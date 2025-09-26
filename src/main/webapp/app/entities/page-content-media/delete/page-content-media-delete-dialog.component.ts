import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPageContentMedia } from '../page-content-media.model';
import { PageContentMediaService } from '../service/page-content-media.service';

@Component({
  standalone: true,
  templateUrl: './page-content-media-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PageContentMediaDeleteDialogComponent {
  pageContentMedia?: IPageContentMedia;

  constructor(
    protected pageContentMediaService: PageContentMediaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageContentMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
