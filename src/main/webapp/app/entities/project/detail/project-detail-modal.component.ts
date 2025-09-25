/*
import { Component, OnInit, Input, inject, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { FullscreenMediaModalComponent } from '../../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';
import { SafeHtml, DomSanitizer } from '@angular/platform-browser';

export interface ProjectDetail {
  id: number;
  title: string;
  description: string;
  mediaUrls: string[];
}

@Component({
  selector: 'jhi-project-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: './project-detail-modal.component.html',
  styleUrls: ['./project-detail-modal.component.scss'],
})
export class ProjectDetailModalComponent implements OnInit {
  @ViewChild('mediaContainer') mediaContainerRef!: ElementRef<HTMLDivElement>;
  @Input() item!: any;

  activeModal = inject(NgbActiveModal);
  private http = inject(HttpClient);
  private modalService = inject(NgbModal);

  project: ProjectDetail | null = null;
  sanitizedContent: SafeHtml | null = null;
  isLoading = true;

  currentMediaIndex = 0;
  private lastWheelTime = 0;
  private readonly wheelDebounceTime = 200;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    console.log('ProjectDetailModalComponent initialized with item?.id:', this.item?.id);
    if (!this.item?.id) {
      console.error('❌ projectId not provided — modal will stay loading!');
      return;
    }
    this.isLoading = true;
    this.http.get<ProjectDetail>(`/api/projects/${this.item.id}/details`).subscribe({
      next: (data) => {
        this.project = data; // keep original string
        this.sanitizedContent = this.sanitizer.bypassSecurityTrustHtml(data.description);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.close();
      },
    });
  }

  onMediaWheel(event: WheelEvent): void {
    event.preventDefault();
    const now = Date.now();
    if (now - this.lastWheelTime < this.wheelDebounceTime) return;
    this.lastWheelTime = now;

    if (event.deltaY > 0) this.switchMedia(this.currentMediaIndex + 1);
    else this.switchMedia(this.currentMediaIndex - 1);
  }

  getMediaType(url: string): 'IMAGE' | 'VIDEO' | 'UNKNOWN' {
    const ext = url.split('.').pop()?.toLowerCase();
    if (!ext) return 'UNKNOWN';
    if (['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].includes(ext)) return 'IMAGE';
    if (['mp4', 'webm', 'ogg'].includes(ext)) return 'VIDEO';
    return 'UNKNOWN';
  }

  getFullMediaPath(url: string): string {
    console.log('getFullMediaPath called with url:', url);
    return this.getMediaType(url) === 'IMAGE'
      ? `/content/images/${url}`
      : this.getMediaType(url) === 'VIDEO'
      ? `/content/videos/${url}`
      : `/content/images/default-project.jpg`;
  }

  openFullscreenMedia(): void {
    if (!this.project) return;
    const url = this.project.mediaUrls[this.currentMediaIndex];
    const type = this.getMediaType(url);

    const modalRef = this.modalService.open(FullscreenMediaModalComponent, {
      centered: true,
      fullscreen: true,
      windowClass: 'fullscreen-modal-window',
    });

    modalRef.componentInstance.mediaUrl = this.getFullMediaPath(url);
    modalRef.componentInstance.mediaType = type;
    modalRef.componentInstance.mediaAlt = this.project.title;
  }

  close(): void {
    this.activeModal.dismiss();
  }

  get hasMultipleMedia(): boolean {
    return !!this.project?.mediaUrls?.length && this.project.mediaUrls.length > 1;
  }

  switchMedia(index: number) {
    if (!this.project?.mediaUrls?.length) return;

    // normalize index (wrap-around safely)
    const length = this.project.mediaUrls.length;
    const normalizedIndex = ((index % length) + length) % length; // handles negative indices too

    const container = this.mediaContainerRef.nativeElement;
    container.classList.add('fade-out');

    setTimeout(() => {
      this.currentMediaIndex = normalizedIndex;
      container.classList.remove('fade-out');
    }, 150); // half of transition duration
  }
}
*/

import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { BaseMediaModalComponent, MediaItem } from '../../../shared/component/base-media-modal/base-media-modal.component';
import { FullscreenMediaModalComponent } from '../../../shared/component/fullscreen-media-modal/fullscreen-media-modal.component';

export interface ProjectDetail {
  id: number;
  title: string;
  description: string;
  mediaUrls: string[];
}

@Component({
  selector: 'jhi-project-detail-modal',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FullscreenMediaModalComponent],
  templateUrl: '../../../shared/component/base-media-modal/media-modal.component.html',
  styleUrls: ['./project-detail-modal.component.scss'],
})
export class ProjectDetailModalComponent extends BaseMediaModalComponent implements OnInit {
  @Input() item!: { id: number; title: string; description?: string };

  private http = inject(HttpClient);
  project: ProjectDetail | null = null;

  ngOnInit(): void {
    if (!this.item?.id) return;

    this.http.get<ProjectDetail>(`/api/projects/${this.item.id}/details`).subscribe({
      next: data => {
        this.project = data;

        // set content and mediaUrls using base class setters (they will sanitize + normalize)
        this.content = data.description ?? '';
        this.mediaUrls = data.mediaUrls?.length ? data.mediaUrls : ['default-project.jpg'];
      },
      error: () => this.close(),
    });
  }

  // override to use project-specific fallback image
  override getFullMediaPath(url: string): string {
    return super.getFullMediaPath(url, '/content/images/default-project.jpg');
  }

  openFullscreenMedia(): void {
    if (!this.mediaUrls.length || !this.project) return;

    const modalRef = this.modalService.open(FullscreenMediaModalComponent, {
      centered: true,
      fullscreen: true,
      windowClass: 'fullscreen-modal-window',
    });

    modalRef.componentInstance.mediaUrl = this.getFullMediaPath(this.currentUrl);
    modalRef.componentInstance.mediaType = this.getMediaType(this.currentUrl);
    modalRef.componentInstance.mediaAlt = this.project.title;
  }
}
