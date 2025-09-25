import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPageContent } from '../page-content.model';
import { PageContentService } from '../service/page-content.service';

@Component({
  templateUrl: './page-content-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PageContentDeleteDialogComponent {
  pageContent?: IPageContent;

  protected pageContentService = inject(PageContentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageContentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
