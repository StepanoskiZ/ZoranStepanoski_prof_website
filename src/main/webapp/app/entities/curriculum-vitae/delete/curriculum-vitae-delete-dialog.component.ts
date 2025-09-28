import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICurriculumVitae } from '../curriculum-vitae.model';
import { CurriculumVitaeService } from '../service/curriculum-vitae.service';

@Component({
  standalone: true,
  templateUrl: './curriculum-vitae-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CurriculumVitaeDeleteDialogComponent {
  curriculumVitae?: ICurriculumVitae;

  constructor(
    protected curriculumVitaeService: CurriculumVitaeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.curriculumVitaeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
