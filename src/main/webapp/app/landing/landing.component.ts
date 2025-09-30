import { Component, OnInit, AfterViewInit, ElementRef, QueryList, ViewChildren, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { ActiveSectionService } from '../layouts/active-section.service';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProjectDetailModalComponent } from './project-detail-modal/project-detail-modal.component';
import { AboutMeModalComponent } from './about-me-modal/about-me-modal.component';
import { CurriculumVitaeDetailModalComponent } from './curriculum-vitae-detail-modal/curriculum-vitae-detail-modal.component';
import { environment } from 'environments/environment';

declare var AOS: any;

export interface ProjectCard {
  id: number;
  title: string;
  description: string;
  firstMediaUrl?: string;
  firstMediaType?: 'IMAGE' | 'VIDEO';
}

export interface CvCard {
  id: number;
  companyName: string;
  jobDescriptionHTML: string;
  startDate?: string;
  endDate?: string;
  firstMediaUrl?: string;
}

export interface AboutMeCard {
  id: number;
  contentHtml: string;
  firstMediaUrl: string | null;
}

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [
    CommonModule,
    SharedModule,
    NgbDropdownModule,
    ContactFormComponent,
    RouterModule,
    AboutMeModalComponent,
    ProjectDetailModalComponent,
    CurriculumVitaeDetailModalComponent,
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit, AfterViewInit, OnDestroy {
  cvDownloadLink = 'content/cv/CV_EN.pdf';
  projects: ProjectCard[] = [];
  isLoadingProjects = true;
  aboutContentPreview: SafeHtml = '';
  aboutMediaUrls: string[] = [];
  aboutMeId: number | null = null;
  cvEntries: CvCard[] = [];
  isLoadingCv = true;
  isLoadingAbout = false;
  isAdminEnv = environment.isAdminEnv;

  private readonly http = inject(HttpClient);
  private readonly modalService = inject(NgbModal);
  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;
  private observer!: IntersectionObserver;
  private readonly activeSectionService = inject(ActiveSectionService);

  services = [
    { icon: '../../../content/media/service1.jpg', title: 'landing.service1Title', description: 'landing.service1Desc', delay: 100 },
    { icon: '../../../content/media/service2.jpg', title: 'landing.service2Title', description: 'landing.service2Desc', delay: 200 },
    { icon: '../../../content/media/service3.jpg', title: 'landing.service3Title', description: 'landing.service3Desc', delay: 300 },
  ];

  constructor(
    private translateService: TranslateService,
    private sanitizer: DomSanitizer,
  ) {
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this.updateCvLink(event.lang);
    });
  }

  ngOnInit(): void {
    this.updateCvLink(this.translateService.currentLang);
    this.loadProjects();
    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: true,
    });
    this.loadAboutContent();
    this.loadCvEntries();
  }

  // --- HELPER FUNCTIONS ---
  private decodeHtml(html: string): string {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }

  private loadAboutContent(): void {
    this.isLoadingAbout = true;
    this.http.get<AboutMeCard>('/api/about-me/card').subscribe({
      next: data => {
        console.log('✅ Received "About Me" CARD data from API:', data);
        const decodedHtml = this.decodeHtml(data.contentHtml || '');
        this.aboutContentPreview = this.sanitizer.bypassSecurityTrustHtml(decodedHtml);
        this.aboutMediaUrls = data.firstMediaUrl ? [data.firstMediaUrl] : [];
        this.aboutMeId = data.id;

        this.isLoadingAbout = false;
      },
      error: err => {
        console.error('❌ Failed to load About Me content', err);
        this.isLoadingAbout = false;
      },
    });
  }

  openAboutModal(): void {
    if (this.isLoadingAbout || this.aboutMeId === null) {
      console.warn('About Me data is not ready yet.');
      return;
    }
    const modalRef = this.modalService.open(AboutMeModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });
    modalRef.componentInstance.item = { id: this.aboutMeId };
  }

  private loadProjects(): void {
    this.isLoadingProjects = true;
    this.http.get<any[]>('/api/projects/cards').subscribe({
      next: (data: any[]) => {
        console.log('✅ Raw projects from backend:', data);
        this.projects = data
          .map((p, index) => ({
            id: p.id ?? p.projectId ?? index,
            title: p.title ?? '(Untitled project)',
            description: p.description ?? '',
            firstMediaUrl: p.firstMediaUrl ?? null,
            firstMediaType: p.firstMediaType ?? null,
          }))
          .sort((a, b) => a.id - b.id);
        console.log('✅ Normalized projects stored in this.projects:', this.projects);
        this.isLoadingProjects = false;
      },
      error: err => {
        console.error('❌ Failed to load projects:', err);
        this.isLoadingProjects = false;
      },
    });
  }

  openProjectMediaModal(projectId: number): void {
    console.log('🔎 openProjectMediaModal called with projectId:', projectId);
    const project = this.projects.find(p => p.id === projectId);
    if (!project) {
      console.error(`❌ No project found for id ${projectId}. Projects array:`, this.projects);
      return;
    }
    console.log('✅ Found project for modal:', project);
    const modalRef = this.modalService.open(ProjectDetailModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });
    modalRef.componentInstance.item = project;
  }

  private loadCvEntries(): void {
    this.isLoadingCv = true;
    this.http.get<CvCard[]>('/api/curriculum-vitae/cards').subscribe({
      next: (data: CvCard[]) => {
        this.cvEntries = data;
        this.isLoadingCv = false;
      },
      error: err => {
        console.error('❌ Failed to load CV entries:', err);
        this.isLoadingCv = false;
      },
    });
  }

  openCvDetailModal(cvId: number): void {
    const cvEntry = this.cvEntries.find(cv => cv.id === cvId);
    if (!cvEntry) {
      console.error(`No CV entry found for id ${cvId}.`);
      return;
    }
    const modalRef = this.modalService.open(CurriculumVitaeDetailModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });
    modalRef.componentInstance.item = cvEntry;
  }

  ngAfterViewInit(): void {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: [0, 0.25, 0.5, 0.75, 1.0],
    };
    const visibilityMap = new Map<string, number>();
    this.observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        visibilityMap.set(entry.target.id, entry.intersectionRatio);
      });
      let mostVisibleSectionId = '';
      let maxRatio = 0;
      visibilityMap.forEach((ratio, id) => {
        if (ratio > maxRatio) {
          maxRatio = ratio;
          mostVisibleSectionId = id;
        }
      });
      if (mostVisibleSectionId) {
        this.activeSectionService.setActiveSection(mostVisibleSectionId);
      }
    }, options);
    this.sections.forEach(section => {
      this.observer.observe(section.nativeElement);
    });
  }

  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  private updateCvLink(lang: string): void {
    this.cvDownloadLink = lang === 'sr' ? 'content/cv/CV_SR.pdf' : 'content/cv/CV_EN.pdf';
  }

  getFullMediaPath(url: string | null | undefined, fallback = '/content/media/default-project.jpg'): string {
    if (!url) {
      return fallback;
    }
    if (url.startsWith('/') || url.startsWith('http')) {
      return url;
    }
    return `/content/media/${url}`;
  }

  getMediaType(url?: string): 'IMAGE' | 'VIDEO' | 'UNKNOWN' {
    const ext = url?.split('.').pop()?.toLowerCase();
    if (!ext) return 'UNKNOWN';
    if (['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].includes(ext)) return 'IMAGE';
    if (['mp4', 'webm', 'ogg'].includes(ext)) return 'VIDEO';
    return 'UNKNOWN';
  }
}
