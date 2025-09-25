import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IVisitorLog } from '../visitor-log.model';

@Component({
  selector: 'jhi-visitor-log-detail',
  templateUrl: './visitor-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class VisitorLogDetailComponent {
  visitorLog = input<IVisitorLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
