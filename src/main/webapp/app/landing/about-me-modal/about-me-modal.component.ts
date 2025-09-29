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
  mediaFiles: AboutMeMedia[]; // Use the specific interface here
}

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
//  @Input() item!: { id: number; title: string; description?: string };
  defaultMedia = '/content/images/default-profile.jpg';
  private http = inject(HttpClient);
//  aboutMe: AboutMeDetail | null = null;
  private translateService = inject(TranslateService);

  constructor() {
    super();
    // Set a default title while loading
    this.title = this.translateService.instant('landing.navAbout');
  }

  ngOnInit(): void {
    this.http.get<AboutMeDetail>(`/api/about-me/details`).subscribe({
      next: data => {
        this.content = this.content = this.decodeHtml(data.contentHtml ?? '');
        const formattedMedia: MediaItem[] = (data.mediaFiles || []).map(apiMediaItem => ({
          url: apiMediaItem.mediaUrl,
          caption: apiMediaItem.caption,
          id: apiMediaItem.id,
          type: this.getMediaType(apiMediaItem.mediaUrl)
        }));

        this.mediaUrls = formattedMedia.length > 0
          ? formattedMedia
          : [{ url: 'profile-picture.jpg', caption: 'About Zoran Stepanoski' }];
      },
      error: () => this.close(),
    });
  }

  // Helper function to decode HTML entities
  private decodeHtml(html: string): string {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }
}
