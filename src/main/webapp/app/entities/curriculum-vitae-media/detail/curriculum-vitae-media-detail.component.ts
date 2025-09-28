import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';

@Component({
  standalone: true,
  selector: 'jhi-curriculum-vitae-media-detail',
  templateUrl: './curriculum-vitae-media-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CurriculumVitaeMediaDetailComponent {
  @Input() curriculumVitaeMedia: ICurriculumVitaeMedia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
