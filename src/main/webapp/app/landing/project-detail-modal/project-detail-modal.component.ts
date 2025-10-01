import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { Lightbox, LightboxModule } from 'ngx-lightbox';

export interface ProjectDetail {
  id: number;
  title: string;
  caption: string;
  description: string;
  mediaFiles: MediaItem[];
}

@Component({
  selector: 'jhi-project-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent, LightboxModule],
  templateUrl: '../../shared/component/base-media-modal/base-modal.component.html',
  styleUrls: ['./project-detail-modal.component.scss'],
})
export class ProjectDetailModalComponent extends BaseMediaModalComponent implements OnInit {
  @Input() item!: { id: number; title: string; description?: string };
  defaultMedia = '/content/media/default-project.jpg';
  private http = inject(HttpClient);
  project: ProjectDetail | null = null;

  ngOnInit(): void {
    if (!this.item?.id) {
      this.close();
      return;
    }

    this.http.get<ProjectDetail>(`/api/projects/${this.item.id}/details`).subscribe({
      next: data => {
        this.project = data;
        this.content = data.description ?? '';
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'default-project.jpg', caption: 'Default Image' }];
        this.title = data.title;
      },
      error: () => this.close(),
    });
  }
}
