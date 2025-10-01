import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBusinessServiceMedia } from '../business-service-media.model';
import { BusinessServiceMediaService } from '../service/business-service-media.service';

@Component({
  standalone: true,
  templateUrl: './business-service-media-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BusinessServiceMediaDeleteDialogComponent {
  businessServiceMedia?: IBusinessServiceMedia;

  constructor(
    protected businessServiceMediaService: BusinessServiceMediaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessServiceMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
