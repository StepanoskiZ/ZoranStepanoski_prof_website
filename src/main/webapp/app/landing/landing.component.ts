import {
  Component,
  OnInit,
  AfterViewInit,
  ElementRef,
  QueryList,
  ViewChildren,
  inject,
  OnDestroy,
  //, signal, computed, Signal
} from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { NgbDropdownModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { ActiveSectionService } from '../layouts/active-section.service';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProjectDetailModalComponent } from './project-detail-modal/project-detail-modal.component';
import { AboutMeModalComponent } from './about-me-modal/about-me-modal.component';
import { CurriculumVitaeDetailModalComponent } from './curriculum-vitae-detail-modal/curriculum-vitae-detail-modal.component';
import { BusinessServiceDetailModalComponent } from './business-service-detail-modal/business-service-detail-modal.component';
import { environment } from 'environments/environment';
import { TruncateHtmlPipe } from 'app/shared/pipes/truncate-html.pipe';
import { SafeHtmlPipe } from 'app/shared/pipes/safe-html.pipe';

declare var AOS: any;

export interface ProjectCard {
  id: number;
  title: string;
  description: string;
  status?: 'COMPLETED' | 'ONGOING';
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
  firstMediaType?: 'IMAGE' | 'VIDEO';
}

export interface AboutMeCard {
  id: number;
  contentHtml: string;
  firstMediaUrl: string | null;
  firstMediaType?: 'IMAGE' | 'VIDEO';
}

export interface BusinessServiceCard {
  id: number;
  title: string;
  description: string;
  firstMediaUrl?: string;
  firstMediaType?: 'IMAGE' | 'VIDEO';
}

export interface SkillCard {
  id: number;
  name: string;
  yearsOfExperience: number;
}

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [
    CommonModule,
    SharedModule,
    NgbDropdownModule,
    NgbPaginationModule,
    ContactFormComponent,
    RouterModule,
    AboutMeModalComponent,
    ProjectDetailModalComponent,
    CurriculumVitaeDetailModalComponent,
    SafeHtmlPipe,
    TruncateHtmlPipe,
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit, AfterViewInit, OnDestroy {
  cvDownloadLink = 'content/cv/CV_EN.pdf';

  aboutCard: AboutMeCard | null = null;
  projects: ProjectCard[] = [];
  cvEntries: CvCard[] = [];
  businessServices: BusinessServiceCard[] = [];
  skills: SkillCard[] = [];

  isLoadingProjects = true;
  isLoadingServices = true;
  isLoadingCv = true;
  isLoadingAbout = false;
  isLoadingSkills = true;

  isAdminEnv = environment.isAdminEnv;

  skillsCurrentPage = 1;
  skillsPerPage = 20; // 10 skills per column
  totalSkills = 0;

  private readonly http = inject(HttpClient);
  private readonly modalService = inject(NgbModal);
  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;
  private observer!: IntersectionObserver;
  private readonly activeSectionService = inject(ActiveSectionService);

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
    this.loadAboutContent();
    this.loadCvEntries();
    this.loadBusinessServices();
    this.loadSkillsPage(1);
    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: true,
    });
  }

  // --- HELPER FUNCTIONS ---
  private decodeHtml(html: string): string {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }

  get leftColumnSkills(): SkillCard[] {
    const midpoint = Math.ceil(this.skills.length / 2);
    return this.skills.slice(0, midpoint);
  }

  get rightColumnSkills(): SkillCard[] {
    const midpoint = Math.ceil(this.skills.length / 2);
    return this.skills.slice(midpoint);
  }

  loadSkillsPage(page: number, shouldScroll: boolean = false): void {
    this.isLoadingSkills = true;
    this.skillsCurrentPage = page;

    let params = new HttpParams()
      .set('page', (page - 1).toString())
      .set('size', this.skillsPerPage.toString())
      .set('sort', 'name,asc');

    this.http.get<SkillCard[]>('/api/skills', { params, observe: 'response' }).subscribe({
      next: (response: HttpResponse<SkillCard[]>) => {
        this.skills = response.body ?? [];
        this.totalSkills = Number(response.headers.get('X-Total-Count') || 0);
        this.isLoadingSkills = false;

        // Only scroll if the flag is true
        if (shouldScroll) {
          document.getElementById('skills')?.scrollIntoView({ behavior: 'smooth' });
        }
      },
      error: err => {
        console.error('❌ Failed to load skills:', err);
        this.isLoadingSkills = false;
      },
    });
  }

  private loadAboutContent(): void {
    this.isLoadingAbout = true;
    this.http.get<AboutMeCard>('/api/about-me/card').subscribe({
      next: data => {
        this.aboutCard = data;
        this.isLoadingAbout = false;
      },
      error: err => {
        console.error('❌ Failed to load About Me content', err);
        this.isLoadingAbout = false;
      },
    });
  }

  private loadBusinessServices(): void {
    this.isLoadingServices = true;
    // Now you can use the strong type and trust the data!
    this.http.get<BusinessServiceCard[]>('/api/business-services/cards').subscribe({
      next: data => {
        this.businessServices = data;
        this.isLoadingServices = false;
      },
      error: err => {
        console.error('❌ Failed to load services:', err);
        this.isLoadingServices = false;
      },
    });
  }

  private loadProjects(): void {
    this.isLoadingProjects = true;
    // Use the strong type here as well.
    this.http.get<ProjectCard[]>('/api/projects/cards').subscribe({
      next: data => {
        this.projects = data;
        this.isLoadingProjects = false;
      },
      error: err => {
        console.error('❌ Failed to load projects:', err);
        this.isLoadingProjects = false;
      },
    });
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

  openAboutModal(): void {
    if (this.isLoadingAbout || !this.aboutCard) {
      console.warn('About Me data is not ready yet.');
      return;
    }
    const modalRef = this.modalService.open(AboutMeModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });
    modalRef.componentInstance.item = { id: this.aboutCard.id };
  }

  openBusinessServiceModal(serviceId: number): void {
    const service = this.businessServices.find(s => s.id === serviceId);
    if (!service) {
      console.error(`❌ No business service found for id ${serviceId}.`);
      return;
    }
    const modalRef = this.modalService.open(BusinessServiceDetailModalComponent, {
      size: 'xl',
      centered: true,
      windowClass: 'project-detail-custom-modal',
    });
    modalRef.componentInstance.item = service;
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
