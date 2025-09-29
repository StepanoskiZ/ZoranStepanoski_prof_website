/*
import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['./about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
  defaultMedia = '/content/images/default-profile.jpg';

  protected translateService = inject(TranslateService);

  constructor() {
    super();
    this.title = this.translateService.instant('landing.navAbout');
  }

  @Input()
  override set content(html: string) {
    super.content = html;
  }
}
*/
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { TranslateService } from '@ngx-translate/core';

// Define the structure of the data we expect from the API
export interface AboutMeDetail {
  contentHtml: string;
  mediaFiles: MediaItem[];
}

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['./about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent implements OnInit {
  defaultMedia = '/content/images/default-profile.jpg';
  private http = inject(HttpClient);
  private translateService = inject(TranslateService);

  constructor() {
    super();
    this.title = this.translateService.instant('landing.navAbout');
  }

  ngOnInit(): void {
    // Fetch the "About Me" details from the backend
    this.http.get<AboutMeDetail>('/api/about-me').subscribe({
      next: data => {
        // Use the parent's 'content' setter to sanitize the HTML
        this.content = data.contentHtml ?? '';

        // Use the parent's 'mediaUrls' setter to normalize the media
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'profile-picture.jpg' }];
      },
      error: err => {
        console.error('[AboutMeModalComponent] Failed to load about-me data', err);
        this.close();
      },
    });
  }
}
