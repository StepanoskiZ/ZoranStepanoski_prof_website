import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/media-modal.component.html',
  styleUrls: ['./about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
  defaultMedia = '/content/images/default-profile.jpg';

  private translateService = inject(TranslateService);

  constructor() {
    super(); // Call the parent constructor
    this.title = this.translateService.instant('landing.navAbout');
  }
}
