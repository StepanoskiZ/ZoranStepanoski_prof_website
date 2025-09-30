import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { TranslateService } from '@ngx-translate/core';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

export interface AboutMeMedia {
  id?: number;
  mediaUrl: string;
  caption?: string;
}

export interface AboutMeDetail {
  contentHtml: string;
  mediaFiles: AboutMeMedia[];
}

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
  defaultMedia = '/content/images/default-profile.jpg';
  private http = inject(HttpClient);
  private translateService = inject(TranslateService);

  constructor() {
    super();
    this.title = this.translateService.instant('landing.navAbout');
  }
}
