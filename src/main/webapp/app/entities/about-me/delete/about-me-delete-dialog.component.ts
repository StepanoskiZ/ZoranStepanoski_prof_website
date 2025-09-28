import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAboutMe } from '../about-me.model';
import { AboutMeService } from '../service/about-me.service';

@Component({
  standalone: true,
  templateUrl: './about-me-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AboutMeDeleteDialogComponent {
  aboutMe?: IAboutMe;

  constructor(
    protected aboutMeService: AboutMeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aboutMeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
