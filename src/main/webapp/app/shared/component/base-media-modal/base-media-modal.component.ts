import { Directive, ElementRef, ViewChild, OnInit, inject, Input } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { FullscreenMediaModalComponent } from '../fullscreen-media-modal/fullscreen-media-modal.component';

export interface MediaItem {
  id?: number;
  url: string;
  type?: 'IMAGE' | 'VIDEO';
  caption?: string;
}

@Directive()
export abstract class BaseMediaModalComponent implements OnInit {
  @ViewChild('mediaContainer', { static: false }) mediaContainerRef!: ElementRef<HTMLDivElement>;
  @Input() title = '';
  private _content = '';
  sanitizedContent: SafeHtml = '';

  private _mediaUrls: MediaItem[] = [];

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
  set mediaUrls(v: MediaItem[] | string[]) {
    if (!v || v.length === 0) {
      this._mediaUrls = [];
      this.currentMediaIndex = 0;
      return;
    }
    if (typeof v[0] === 'string') {
      this._mediaUrls = (v as string[]).map((u, i) => ({ id: i, url: u, caption: '' }));
    } else {
      this._mediaUrls = (v as any[]).map((m, i) => ({
        id: m.id ?? i,
        url: m.mediaUrl ?? m.url,
        type: m.type,
        caption: m.caption,
      }));
    }
    this._mediaUrls.forEach(item => {
      if (!item.type) item.type = this.getMediaType(item.url) === 'VIDEO' ? 'VIDEO' : 'IMAGE';
    });
    this._mediaUrls.sort((a, b) => (a.id ?? 0) - (b.id ?? 0));
    if (this.currentMediaIndex >= this._mediaUrls.length) this.currentMediaIndex = 0;
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

  getFullMediaPath(url: string, fallback = '/content/images/default-profile.jpg'): string {
    if (!url) return fallback;
    if (url.startsWith('/') || url.startsWith('http')) return url;
    const type = this.getMediaType(url);
    if (type === 'IMAGE') return `/content/images/${url}`;
    if (type === 'VIDEO') return `/content/videos/${url}`;
    return fallback;
  }

  openFullscreenMedia() {
    if (!this._mediaUrls || this._mediaUrls.length === 0) return;
    const url = this.getFullMediaPath(this.currentUrl);
    const type = this.getMediaType(url);

    const modalRef = this.modalService.open(FullscreenMediaModalComponent, {
      centered: true,
      fullscreen: true,
      windowClass: 'fullscreen-modal-window',
    });

    modalRef.componentInstance.mediaUrl = url;
    modalRef.componentInstance.mediaType = type;
    modalRef.componentInstance.mediaAlt = '';
  }

  close() {
    this.activeModal.dismiss();
  }
}
