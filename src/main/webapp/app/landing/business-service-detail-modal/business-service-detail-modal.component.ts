import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { Lightbox, LightboxModule } from 'ngx-lightbox';

export interface BusinessServiceDetail {
  id: number;
  title: string;
  description: string;
  mediaFiles: MediaItem[];
}

@Component({
  selector: 'jhi-business-service-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent, LightboxModule], // Add other necessary imports like FaIconComponent if needed
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['./business-service-detail-modal.component.scss'], // Create this empty scss file
})
export class BusinessServiceDetailModalComponent extends BaseMediaModalComponent implements OnInit {
  @Input() item!: { id: number };
  defaultMedia = '/content/media/default-service.jpg'; // Create a default image
  private http = inject(HttpClient);

  ngOnInit(): void {
    if (!this.item?.id) {
      this.close();
      return;
    }

    this.http.get<BusinessServiceDetail>(`/api/business-services/${this.item.id}/details`).subscribe({
      next: data => {
        this.content = data.description ?? '';
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'default-service.jpg' }];
        this.title = data.title;
      },
      error: () => this.close(),
    });
  }
}
