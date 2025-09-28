import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';
import { CurriculumVitaeMediaService } from '../service/curriculum-vitae-media.service';

@Component({
  standalone: true,
  templateUrl: './curriculum-vitae-media-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CurriculumVitaeMediaDeleteDialogComponent {
  curriculumVitaeMedia?: ICurriculumVitaeMedia;

  constructor(
    protected curriculumVitaeMediaService: CurriculumVitaeMediaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.curriculumVitaeMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
