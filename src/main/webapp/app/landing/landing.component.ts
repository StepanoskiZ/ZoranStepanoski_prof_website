import { Component, OnInit, AfterViewInit, ElementRef, QueryList, ViewChildren, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { ActiveSectionService } from '../layouts/active-section.service';
import { IProject } from '../entities/project/project.model';
import { ProjectService } from '../entities/project/service/project.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProjectDetailModalComponent } from './project-detail-modal/project-detail-modal.component';
import { AboutMeModalComponent } from './about-me-modal/about-me-modal.component';
import { CurriculumVitaeDetailModalComponent } from './curriculum-vitae-detail-modal/curriculum-vitae-detail-modal.component';
import { BaseMediaModalComponent } from '../shared/component/base-media-modal/base-media-modal.component';
import { environment } from 'environments/environment';

declare var AOS: any;

// Define a new interface for our card data
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
  startDate?: string; // Dates will come as strings
  endDate?: string;
  firstMediaUrl?: string;
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
  aboutContent: SafeHtml = '';
  projects: ProjectCard[] = [];
  isLoadingProjects = true;
  aboutMediaUrls: string[] = [];
  isLoadingAbout = false;
  cvEntries: CvCard[] = [];
  isLoadingCv = true;

  isAdminEnv = environment.isAdminEnv;

  private _aboutContent = '';
  private readonly http = inject(HttpClient);
  //  private readonly projectService = inject(ProjectService);
  private readonly modalService = inject(NgbModal);

  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;

  private observer!: IntersectionObserver;
  private readonly activeSectionService = inject(ActiveSectionService);

  services = [
    { icon: '../../../content/images/service1.jpg', title: 'landing.service1Title', description: 'landing.service1Desc', delay: 100 },
    { icon: '../../../content/images/service2.jpg', title: 'landing.service2Title', description: 'landing.service2Desc', delay: 200 },
    { icon: '../../../content/images/service3.jpg', title: 'landing.service3Title', description: 'landing.service3Desc', delay: 300 },
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

  private loadProjects(): void {
    this.isLoadingProjects = true;
    this.http.get<any[]>('/api/projects/cards').subscribe({
      next: (data: any[]) => {
        console.log('✅ Raw projects from backend:', data);

        // Normalize data (make sure every project has an `id` property)
        this.projects = data
          .map((p, index) => {
            return {
              id: p.id ?? p.projectId ?? index, // fallback to projectId or index
              title: p.title ?? '(Untitled project)',
              description: p.description ?? '',
              firstMediaUrl: p.firstMediaUrl ?? null,
              firstMediaType: p.firstMediaType ?? null,
            };
          })
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
    console.log('Available projects:', this.projects);

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

  get aboutPreview(): SafeHtml {
    const preview = this._aboutContent?.substring(0, 300) ?? '';
    return this.sanitizer.bypassSecurityTrustHtml(preview);
  }

  setAboutContent(html: string) {
    this._aboutContent = html;
//    const preview = html.length > 300 ? html.substring(0, 300) : html;
//    this.aboutContent = this.sanitizer.bypassSecurityTrustHtml(preview);
    this.aboutContent = this.sanitizer.bypassSecurityTrustHtml(html);
  }

  private loadAboutContent(): void {
    this.isLoadingAbout = true;
    this.http.get<any>('/api/about-me').subscribe({
//    this.http.get<any>('/api/about-mes').subscribe({
      next: data => {
        this.setAboutContent(data.contentHtml);
        console.log('Received data:', data);
        // If API returns media files, normalize them
        this.aboutMediaUrls = data.mediaUrls ?? [];

        this.isLoadingAbout = false;
      },
      error: err => {
        console.error('❌ Failed to load About Me content', err);
        this.isLoadingAbout = false;
      },
    });
  }

  openAboutModal(): void {
    if (this.isLoadingAbout) {
      console.warn('About Me content is still loading.');
      return;
    }

    const modalRef = this.modalService.open(AboutMeModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });

    // Pass sanitized HTML content
    modalRef.componentInstance.content = this._aboutContent;

    // Normalize media URLs into MediaItem[]
    modalRef.componentInstance.mediaUrls = (this.aboutMediaUrls?.length ? this.aboutMediaUrls : ['profile-picture.jpg']).map(
      (u: string, i: number) => ({ id: i, url: u }),
    );
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
      windowClass: 'project-detail-custom-modal', // Reusing the same style
    });

    modalRef.componentInstance.item = cvEntry;
  }

  ngAfterViewInit(): void {
    // This is the new, smarter observer logic
    const options = {
      root: null,
      rootMargin: '0px',
      // We create a range of thresholds to check how much is visible
      threshold: [0, 0.25, 0.5, 0.75, 1.0],
    };

    // This map will store the visibility ratio of each section
    const visibilityMap = new Map<string, number>();

    this.observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        // Update the map with the current visibility ratio of the section
        visibilityMap.set(entry.target.id, entry.intersectionRatio);
      });

      // Find the section with the highest visibility ratio
      let mostVisibleSectionId = '';
      let maxRatio = 0;
      visibilityMap.forEach((ratio, id) => {
        if (ratio > maxRatio) {
          maxRatio = ratio;
          mostVisibleSectionId = id;
        }
      });

      // If we found a most visible section, update the service
      if (mostVisibleSectionId) {
        this.activeSectionService.setActiveSection(mostVisibleSectionId);
      }
    }, options);

    this.sections.forEach(section => {
      this.observer.observe(section.nativeElement);
    });
  }

  // It's good practice to disconnect the observer when the component is destroyed
  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  private updateCvLink(lang: string): void {
    this.cvDownloadLink = lang === 'sr' ? 'content/cv/CV_SR.pdf' : 'content/cv/CV_EN.pdf';
  }

  getFullMediaPath(url?: string): string {
    console.log('getFullMediaPath called with url:', url);
    return this.getMediaType(url) === 'IMAGE'
      ? `/content/images/${url}`
      : this.getMediaType(url) === 'VIDEO'
        ? `/content/videos/${url}`
        : `/content/images/default-project.jpg`;
  }

  getMediaType(url?: string): 'IMAGE' | 'VIDEO' | 'UNKNOWN' {
    const ext = url?.split('.').pop()?.toLowerCase();
    if (!ext) return 'UNKNOWN';
    if (['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].includes(ext)) return 'IMAGE';
    if (['mp4', 'webm', 'ogg'].includes(ext)) return 'VIDEO';
    return 'UNKNOWN';
  }
}
