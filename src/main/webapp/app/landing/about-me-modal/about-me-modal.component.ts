import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/media-modal.component.html',
  styleUrls: ['./about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
  project?: any = null;

  // override fallback image if desired:
  override getFullMediaPath(url: string): string {
    return super.getFullMediaPath(url, '/content/images/default-profile.jpg');
  }
}
