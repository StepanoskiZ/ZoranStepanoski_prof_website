import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAboutMeMedia } from '../about-me-media.model';
import { AboutMeMediaService } from '../service/about-me-media.service';

@Component({
  standalone: true,
  templateUrl: './about-me-media-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AboutMeMediaDeleteDialogComponent {
  aboutMeMedia?: IAboutMeMedia;

  constructor(
    protected aboutMeMediaService: AboutMeMediaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aboutMeMediaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
