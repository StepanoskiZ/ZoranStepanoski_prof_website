import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-fullscreen-media-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './fullscreen-media-modal.component.html',
  styleUrls: ['./fullscreen-media-modal.component.scss'],
})
export class FullscreenMediaModalComponent {
  @Input() mediaUrl!: string;
  @Input() mediaType!: 'IMAGE' | 'VIDEO';
  @Input() mediaAlt!: string;

  activeModal = inject(NgbActiveModal);

  close(): void {
    this.activeModal.dismiss('Cross click');
  }
}
