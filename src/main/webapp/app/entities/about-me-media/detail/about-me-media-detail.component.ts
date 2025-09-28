import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAboutMeMedia } from '../about-me-media.model';

@Component({
  standalone: true,
  selector: 'jhi-about-me-media-detail',
  templateUrl: './about-me-media-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AboutMeMediaDetailComponent {
  @Input() aboutMeMedia: IAboutMeMedia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
