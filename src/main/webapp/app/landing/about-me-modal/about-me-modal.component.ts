/*
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
    this.http.get<AboutMeDetail>('/api/about-me').subscribe({
      next: data => {
        const decodedHtml = this.decodeHtml(data.contentHtml ?? '');
        this.content = decodedHtml;

        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'profile-picture.jpg' }];
      },
      error: err => {
        console.error('[AboutMeModalComponent] Failed to load about-me data', err);
        this.close();
      },
    });
  }

  // Helper function to decode HTML entities like &lt; into <
  private decodeHtml(html: string): string {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }
}
*/

import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
//import { TranslateService } from '@ngx-translate/core';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

export interface AboutMeMediaItem {
  id?: number;
  mediaUrl: string; // Or fileName
  caption?: string;
}

export interface AboutMeDetail {
  contentHtml: string;
  mediaFiles: AboutMeMediaItem[]; // Use the specific interface here
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
//  defaultMedia = '/content/images/default-profile.jpg';
  private http = inject(HttpClient);
//  aboutMe: AboutMeDetail | null = null;
  private translateService = inject(TranslateService);

  constructor() {
    super();
    // Set a default title while loading
    this.title = this.translateService.instant('landing.navAbout');
  }

  ngOnInit(): void {
    if (!this.item?.id) {
      this.close();
      return;
    }

    this.http.get<AboutMeDetail>(`/api/about-me/details`).subscribe({
      next: data => {
        this.content = this.content = this.decodeHtml(data.contentHtml ?? '');
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'profile-picture.jpg', caption: 'Zoran Stepanoski' }];
//        this.title = data.title;
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
