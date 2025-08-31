import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProjectImage } from '../project-image.model';
import { ProjectImageService } from '../service/project-image.service';

@Component({
  templateUrl: './project-image-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProjectImageDeleteDialogComponent {
  projectImage?: IProjectImage;

  protected projectImageService = inject(ProjectImageService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
