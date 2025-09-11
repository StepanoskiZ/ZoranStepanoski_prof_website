import { Component, OnInit, AfterViewInit, ElementRef, QueryList, ViewChildren, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { ActiveSectionService } from '../layouts/active-section.service';

declare var AOS: any;

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, SharedModule, NgbDropdownModule, ContactFormComponent],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit, AfterViewInit, OnDestroy {
  cvDownloadLink = 'content/cv/CV_EN.pdf';

  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;

  private observer!: IntersectionObserver;
  private readonly activeSectionService = inject(ActiveSectionService);

  services = [
    { icon: '../../../content/images/service1.jpg', title: 'landing.service1Title', description: 'landing.service1Desc', delay: 100 },
    { icon: '../../../content/images/service2.jpg', title: 'landing.service2Title', description: 'landing.service2Desc', delay: 200 },
    { icon: '../../../content/images/service3.jpg', title: 'landing.service3Title', description: 'landing.service3Desc', delay: 300 },
  ];

  constructor(private translateService: TranslateService) {
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this.updateCvLink(event.lang);
    });
  }

  ngOnInit(): void {
    this.updateCvLink(this.translateService.currentLang);
    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: true,
    });
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
}
