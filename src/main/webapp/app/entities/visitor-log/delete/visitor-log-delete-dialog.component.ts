import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVisitorLog } from '../visitor-log.model';
import { VisitorLogService } from '../service/visitor-log.service';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  templateUrl: './visitor-log-delete-dialog.component.html',
  imports: [TranslateDirective, AlertErrorComponent, SharedModule, FormsModule],
})
export class VisitorLogDeleteDialogComponent {
  visitorLog?: IVisitorLog;

  protected visitorLogService = inject(VisitorLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.visitorLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
