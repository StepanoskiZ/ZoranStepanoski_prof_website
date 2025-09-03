import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { Component, OnInit, HostListener, AfterViewInit, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { ContactFormComponent } from './contact-form/contact-form.component';

declare var AOS: any;

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, SharedModule, ContactFormComponent],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  navbarScrolled = false;
  activeSection = 'home';

  // Get a reference to all <section> elements in the template
  @ViewChildren('section', { read: ElementRef }) sections!: QueryList<ElementRef>;
  private observer!: IntersectionObserver;

  // =================== Services array ===================
  services = [
    {
      icon: 'assets/images/service1.png',
      title: 'landing.service1Title',
      description: 'landing.service1Desc',
      delay: 100,
    },
    {
      icon: 'assets/images/service2.png',
      title: 'landing.service2Title',
      description: 'landing.service2Desc',
      delay: 200,
    },
    {
      icon: 'assets/images/service3.png',
      title: 'landing.service3Title',
      description: 'landing.service3Desc',
      delay: 300,
    },
  ];
  // ========================================================

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.navbarScrolled = window.scrollY > 50;
  }

  ngOnInit(): void {
    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: true,
    });
  }

  ngAfterViewInit(): void {
    // Setup the observer after the view is initialized
    const options = {
      root: null, // observes intersections relative to the viewport
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
}
