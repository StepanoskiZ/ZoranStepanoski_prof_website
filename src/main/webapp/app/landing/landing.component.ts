import { Component, OnInit, HostListener, AfterViewInit, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { ContactFormComponent } from './contact-form/contact-form.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core'; // <-- Import services

declare var AOS: any;

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, SharedModule, ContactFormComponent],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit, AfterViewInit {
  navbarScrolled = false;
  activeSection = 'home';
  cvDownloadLink = 'content/cv/CV_EN.pdf'; // Default to English CV

  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;
  private observer!: IntersectionObserver;

  // =================== Services array ===================
  services = [
    {
      icon: '../../../content/images/service1.jpg',
      title: 'landing.service1Title',
      description: 'landing.service1Desc',
      delay: 100,
    },
    {
      icon: '../../../content/images/service2.jpg',
      title: 'landing.service2Title',
      description: 'landing.service2Desc',
      delay: 200,
    },
    {
      icon: '../../../content/images/service3.jpg',
      title: 'landing.service3Title',
      description: 'landing.service3Desc',
      delay: 300,
    },
  ];
  // ========================================================

  // Inject TranslateService
  constructor(private translateService: TranslateService) {
    // Listen for language changes to update the CV link dynamically
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this.updateCvLink(event.lang);
    });
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.navbarScrolled = window.scrollY > 50;
  }

  ngOnInit(): void {
    // Set the initial link based on the current language when the component loads
    this.updateCvLink(this.translateService.currentLang);

    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: true,
    });
  }

  ngAfterViewInit(): void {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: 0.4, // 40% of the section must be visible
    };

    this.observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          this.activeSection = entry.target.id;
        }
      });
    }, options);

    // Start observing each section
    this.sections.forEach(section => {
      this.observer.observe(section.nativeElement);
    });
  }

  // Method to change the application's language
  changeLanguage(languageKey: string): void {
    this.translateService.use(languageKey);
  }

  // Helper method to set the correct CV link based on language
  private updateCvLink(lang: string): void {
    if (lang === 'sr') {
      this.cvDownloadLink = 'content/cv/CV_SR.pdf';
    } else {
      this.cvDownloadLink = 'content/cv/CV_EN.pdf';
    }
  }
}
