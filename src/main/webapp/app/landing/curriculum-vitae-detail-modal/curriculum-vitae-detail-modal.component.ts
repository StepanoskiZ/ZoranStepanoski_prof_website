import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

export interface CurriculumVitaeDetail {
  id: number;
  companyName: string;
  jobDescriptionHTML: string;
  startDate: string;
  endDate: string;
  mediaFiles: MediaItem[];
}
@Component({
  selector: 'jhi-curriculum-vitae-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../shared/component/base-media-modal/media-modal.component.html',
  styleUrls: ['./curriculum-vitae-detail-modal.component.scss'],
})
export class CurriculumVitaeDetailModalComponent extends BaseMediaModalComponent implements OnInit {
  @Input() item!: { id: number; companyName: string; jobDescriptionHTML?: string };

  defaultMedia = '/content/images/default-work.jpg';
  private http = inject(HttpClient);
  cvDetail: CurriculumVitaeDetail | null = null;

  ngOnInit(): void {
    if (!this.item?.id) {
      this.close();
      return;
    }

    this.http.get<CurriculumVitaeDetail>(`/api/curriculum-vitae/${this.item.id}/details`).subscribe({
      next: data => {
        this.cvDetail = data;
        this.content = data.jobDescriptionHTML ?? '';
        this.mediaUrls = data.mediaFiles?.length ? data.mediaFiles : [{ url: 'default-work.jpg', caption: 'Default Image' }];
        this.title = data.companyName;
      },
      error: () => this.close(),
    });
  }
}
