import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProjectMedia } from '../project-media.model';
import { ProjectMediaService } from '../service/project-media.service';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  templateUrl: './project-media-delete-dialog.component.html',
  imports: [TranslateDirective, AlertErrorComponent, SharedModule, FormsModule],
})
export class ProjectMediaDeleteDialogComponent {
  projectMedia?: IProjectMedia;

  protected projectMediaService = inject(ProjectMediaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
