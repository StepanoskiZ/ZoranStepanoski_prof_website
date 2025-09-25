import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IVisitorLog } from '../visitor-log.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';

@Component({
  selector: 'jhi-visitor-log-detail',
  templateUrl: './visitor-log-detail.component.html',
  imports: [AlertComponent, AlertErrorComponent, SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class VisitorLogDetailComponent {
  visitorLog = input<IVisitorLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
