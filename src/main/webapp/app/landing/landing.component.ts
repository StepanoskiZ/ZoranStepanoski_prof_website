import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { Component, OnInit, HostListener } from '@angular/core';
// import { LanguageSelectorComponent } from '../layouts/navbar/language-selector/language-selector.component';

declare var AOS: any;

@Component({
  selector: 'app-landing',
  standalone: true,
  //   imports: [CommonModule, SharedModule, LanguageSelectorComponent],
  imports: [CommonModule, SharedModule],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  navbarScrolled = false;

  // =================== Services array ===================
  services = [
    { icon: 'assets/images/service1.png', title: 'landing.service1Title', description: 'landing.service1Desc', delay: 100 },
    { icon: 'assets/images/service2.png', title: 'landing.service2Title', description: 'landing.service2Desc', delay: 200 },
    { icon: 'assets/images/service3.png', title: 'landing.service3Title', description: 'landing.service3Desc', delay: 300 },
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
}
