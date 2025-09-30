import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { TranslateService } from '@ngx-translate/core';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

export interface AboutMeDetail {
  id: number;
  title: string; // Assuming your DTO has a title, otherwise we can use a default
  contentHtml: string;
  mediaFiles: MediaItem[];
}

@Component({
  selector: 'jhi-about-me-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['about-me-modal.component.scss'],
})
export class AboutMeModalComponent extends BaseMediaModalComponent {
  @Input() item!: { id: number; title?: string };
  defaultMedia = '/content/media/default-profile.jpg';
  private http = inject(HttpClient);
  private translateService = inject(TranslateService);

  constructor() {
    super();
    this.title = this.translateService.instant('landing.navAbout');
  }

  ngOnInit(): void {
    if (!this.item?.id) {
      console.error('AboutMeModalComponent: No item with ID provided.');
      this.close();
      return;
    }

    this.http.get<AboutMeDetail>(`/api/about-me/${this.item.id}/details`).subscribe({
      next: data => {
        this.content = this.decodeHtml(data.contentHtml ?? '');
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'profile-picture.jpg', caption: 'About Zoran Stepanoski' }];
        this.title = data.title || this.translateService.instant('landing.navAbout');
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
