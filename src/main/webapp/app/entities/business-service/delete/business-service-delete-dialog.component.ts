import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBusinessService } from '../business-service.model';
import { BusinessServiceService } from '../service/business-service.service';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  templateUrl: './business-service-delete-dialog.component.html',
  imports: [TranslateDirective, AlertErrorComponent, SharedModule, FormsModule],
})
export class BusinessServiceDeleteDialogComponent {
  businessService?: IBusinessService;

  protected businessServiceService = inject(BusinessServiceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessServiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
