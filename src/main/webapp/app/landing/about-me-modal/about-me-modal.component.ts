import { Component, inject, OnInit } from '@angular/core';
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

  ngOnInit(): void {
    // This is the component's responsibility now.
    this.http.get<AboutMeDetail>('/api/about-me/details').subscribe({
      next: data => {
        this.content = this.decodeHtml(data.contentHtml ?? '');

        const formattedMedia = (data.mediaFiles || []).map((apiItem: AboutMeMedia) => ({
          url: apiItem.mediaUrl,
          caption: apiItem.caption,
          id: apiItem.id,
        }));

        this.mediaUrls = formattedMedia.length > 0
          ? formattedMedia
          : [{ url: 'profile-picture.jpg', caption: 'About Zoran Stepanoski' }];
      },
      error: (err) => {
        console.error('❌ Failed to load About Me details in modal:', err);
        this.close();
      },
    });
  }

  private decodeHtml(html: string): string {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }
}
