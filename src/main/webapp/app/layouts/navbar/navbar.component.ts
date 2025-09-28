import { Component, OnInit, inject, signal, HostListener } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import SharedModule from 'app/shared/shared.module';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { environment } from 'environments/environment';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { ActiveSectionService } from '../active-section.service';
import NavbarItem from './navbar-item.model';

@Component({
  selector: 'jhi-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, SharedModule, NgbDropdownModule],
})
export default class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = signal(true);
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: NavbarItem[] = [];
  navbarScrolled = false;
  currentFlagClass = 'fi fi-gb';
  isAdminEnv = environment.isAdminEnv;

  private readonly activeSectionService = inject(ActiveSectionService);
  activeSection = this.activeSectionService.activeSection;

  private readonly loginService = inject(LoginService);
  private readonly translateService = inject(TranslateService);
  private readonly stateStorageService = inject(StateStorageService);
  private readonly profileService = inject(ProfileService);
  private readonly router = inject(Router);

  constructor() {
    const { VERSION } = environment;
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    this.navbarScrolled = window.scrollY > 50;
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
    this.updateFlag(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed.set(true);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed.update(isNavbarCollapsed => !isNavbarCollapsed);
  }

  private updateFlag(lang: string): void {
    this.currentFlagClass = lang === 'sr' ? 'fi fi-rs' : 'fi fi-gb';
  }

  isLandingPage(): boolean {
    return this.router.url === '/';
  }

  scrollToSection(sectionId: string): void {
    this.collapseNavbar();
    if (!this.isLandingPage()) {
      this.router.navigate(['/']).then(() => {
        setTimeout(() => this.performScroll(sectionId), 100);
      });
    } else {
      this.performScroll(sectionId);
    }
  }

  private performScroll(sectionId: string): void {
    this.activeSectionService.setActiveSection(sectionId);
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
