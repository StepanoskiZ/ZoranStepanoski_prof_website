import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBusinessServiceMedia } from '../business-service-media.model';

@Component({
  standalone: true,
  selector: 'jhi-business-service-media-detail',
  templateUrl: './business-service-media-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BusinessServiceMediaDetailComponent {
  @Input() businessServiceMedia: IBusinessServiceMedia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
