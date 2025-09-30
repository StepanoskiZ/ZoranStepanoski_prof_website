import { Directive, ElementRef, ViewChild, OnInit, inject, Input } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { FullscreenMediaModalComponent } from '../fullscreen-media-modal/fullscreen-media-modal.component';
import { Lightbox, LightboxModule } from 'ngx-lightbox';

export interface MediaItem {
  id?: number;
  url: string;
  type?: 'IMAGE' | 'VIDEO' | 'UNKNOWN';
  caption?: string;
}

@Directive()
export abstract class BaseMediaModalComponent implements OnInit {
  @ViewChild('mediaContainer', { static: false }) mediaContainerRef!: ElementRef<HTMLDivElement>;
  @Input() title = '';
  private _content = '';
  sanitizedContent: SafeHtml = '';

  private _mediaUrls: MediaItem[] = [];

  protected lightbox = inject(Lightbox);

  @Input()
  set content(value: string) {
    console.log('[AboutMeModalComponent] content Input received:', value);
    this._content = value ?? '';
    // sanitize immediately whenever content is assigned
    this.sanitizedContent = this.sanitizer.bypassSecurityTrustHtml(this._content);
  }

  get content(): string {
    return this._content;
  }

  @Input()
  set mediaUrls(incomingMedia: any[]) {
    if (!incomingMedia || incomingMedia.length === 0) {
      this._mediaUrls = [];
      this.currentMediaIndex = 0;
      return;
    }

    // Process the incoming array of objects from the API
    this._mediaUrls = incomingMedia.map((apiItem, index) => {
      // The API sends 'mediaUrl', but the template uses 'url'. We map it here.
      const url = apiItem.mediaUrl ?? apiItem.url;

      return {
        id: apiItem.id ?? index,
        url: url,
        caption: apiItem.caption,
        // Use the helper function to determine the type. This is now safe.
        type: this.getMediaType(url),
      };
    });

    // Sort the media by ID
    this._mediaUrls.sort((a, b) => (a.id ?? 0) - (b.id ?? 0));

    // Reset the index if needed
    if (this.currentMediaIndex >= this._mediaUrls.length) {
      this.currentMediaIndex = 0;
    }
  }

  get mediaUrls(): MediaItem[] {
    return this._mediaUrls;
  }

  get currentMediaCaption(): string {
    return this._mediaUrls[this.currentMediaIndex]?.caption ?? '';
  }

  currentMediaIndex = 0;

  protected activeModal = inject(NgbActiveModal);
  protected modalService = inject(NgbModal);
  protected sanitizer = inject(DomSanitizer);

  private lastWheelTime = 0;
  private readonly wheelDebounceTime = 200;

  ngOnInit(): void {
    // nothing else required — setters already handle normalization & sanitization
  }

  get hasMultipleMedia(): boolean {
    return this._mediaUrls.length > 1;
  }

  get currentUrl(): string {
    return this._mediaUrls[this.currentMediaIndex]?.url ?? '';
  }

  switchMedia(index: number) {
    const list = this._mediaUrls;
    if (!list?.length) {
      return;
    }

    const length = list.length;
    this.currentMediaIndex = ((index % length) + length) % length;
  }

  onMediaWheel(event: WheelEvent) {
    event.preventDefault();
    const now = Date.now();
    if (now - this.lastWheelTime < this.wheelDebounceTime) return;
    this.lastWheelTime = now;

    if (event.deltaY > 0) this.switchMedia(this.currentMediaIndex + 1);
    else this.switchMedia(this.currentMediaIndex - 1);
  }

  getMediaType(url: string): 'IMAGE' | 'VIDEO' | 'UNKNOWN' {
    const ext = (url ?? '').split('.').pop()?.toLowerCase();
    if (!ext) return 'UNKNOWN';
    if (['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].includes(ext)) return 'IMAGE';
    if (['mp4', 'webm', 'ogg'].includes(ext)) return 'VIDEO';
    return 'UNKNOWN';
  }

  getFullMediaPath(url: string, fallback = '/content/media/default-profile.jpg'): string {
    if (!url) {
      return fallback; // Return a fallback if the URL is empty
    }
    if (url.startsWith('/') || url.startsWith('http')) {
      return url;
    }
    return `/content/media/${url}`;
  }

  openFullscreenMedia(): void {
    if (!this.mediaUrls || this.mediaUrls.length === 0) return;

    const currentItem = this.mediaUrls[this.currentMediaIndex];

    // Check the type of the CURRENT media item
    if (currentItem.type === 'IMAGE') {
      // --- For IMAGES: Use the new lightbox ---
      const album = this.mediaUrls
        .filter(item => item.type === 'IMAGE') // Create an album of just the images
        .map(item => ({
          src: this.getFullMediaPath(item.url),
          caption: item.caption,
          thumb: this.getFullMediaPath(item.url) // Thumbnail for the gallery view
        }));

      // Find the index of the current image within the new image-only album
      const currentImageIndex = album.findIndex(img => img.src === this.getFullMediaPath(currentItem.url));

      this.lightbox.open(album, currentImageIndex);

    } else if (currentItem.type === 'VIDEO') {
      // --- For VIDEOS: Use your existing custom modal ---
      const modalRef = this.modalService.open(FullscreenMediaModalComponent, {
        centered: true,
        fullscreen: true,
        windowClass: 'fullscreen-modal-window',
      });

      modalRef.componentInstance.mediaUrl = this.getFullMediaPath(currentItem.url);
      modalRef.componentInstance.mediaType = 'VIDEO';
      modalRef.componentInstance.mediaAlt = currentItem.caption || '';
    }
  }

  close() {
    this.activeModal.dismiss();
  }
}
