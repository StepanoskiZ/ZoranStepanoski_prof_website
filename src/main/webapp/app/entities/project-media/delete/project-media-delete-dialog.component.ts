import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProjectMedia } from '../project-media.model';
import { ProjectMediaService } from '../service/project-media.service';

@Component({
  templateUrl: './project-media-delete-dialog.component.html',
  standalone: true,
  imports: [SharedModule, FormsModule],
})
export class ProjectMediaDeleteDialogComponent {
  projectMedia?: IProjectMedia;

  constructor(
    protected projectMediaService: ProjectMediaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
