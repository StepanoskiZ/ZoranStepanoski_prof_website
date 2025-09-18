import { Component, OnInit, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

// Interface for our detailed project data
export interface ProjectDetail {
  id: number;
  title: string;
  description: string;
  url?: string;
  imageUrls: string[];
}

@Component({
  selector: 'jhi-project-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent],
  templateUrl: './project-detail-modal.component.html',
  styleUrls: ['./project-detail-modal.component.scss'],
})
export class ProjectDetailModalComponent implements OnInit {
  @Input() projectId!: number;

  activeModal = inject(NgbActiveModal);
  private http = inject(HttpClient);

  project: ProjectDetail | null = null;
  isLoading = true;
  currentImageIndex = 0;

  ngOnInit(): void {
    if (this.projectId) {
      this.isLoading = true;
      this.http.get<ProjectDetail>(`/api/projects/${this.projectId}/details`).subscribe({
        next: data => {
          this.project = data;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
          // Handle error, maybe close the modal
          this.close();
        },
      });
    }
  }

  close(): void {
    this.activeModal.dismiss('Cross click');
  }

  previousImage(): void {
    if (!this.project || this.project.imageUrls.length === 0) return;
    this.currentImageIndex = (this.currentImageIndex - 1 + this.project.imageUrls.length) % this.project.imageUrls.length;
  }

  nextImage(): void {
    if (!this.project || this.project.imageUrls.length === 0) return;
    this.currentImageIndex = (this.currentImageIndex + 1) % this.project.imageUrls.length;
  }
}
