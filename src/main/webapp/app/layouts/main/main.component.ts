import { Component, OnInit, Renderer2, RendererFactory2, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
// --- 1. IMPORT DAYJS ---
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import NavbarComponent from '../navbar/navbar.component';
import FooterComponent from '../footer/footer.component';
import { HttpClient } from '@angular/common/http';
import { IVisitorLog } from 'app/entities/visitor-log/visitor-log.model';

@Component({
  selector: 'jhi-main',
  standalone: true,
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
})
export default class MainComponent implements OnInit {
  private readonly renderer: Renderer2;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);
  private readonly http = inject(HttpClient);

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe();
    this.logVisitor();
    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  private logVisitor(): void {
    // --- 2. CREATE THE OBJECT WITH THE CORRECT TYPE ---
    const visitorLog: Partial<IVisitorLog> = {
      // Use dayjs() to get the current time as a Dayjs object
      visitTimestamp: dayjs(),
      pageVisited: window.location.pathname,
      userAgent: navigator.userAgent,
    };

    this.http.post('/api/visitor-logs', visitorLog).subscribe({
      next: () => console.log('Visitor event logged successfully.'),
      error: err => console.error('Failed to log visitor event:', err),
    });
  }
}
