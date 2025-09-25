import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProjectMedia } from '../project-media.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'jhi-project-media-detail',
  templateUrl: './project-media-detail.component.html',
  imports: [TranslateDirective, AlertComponent, AlertErrorComponent, SharedModule, RouterModule],
})
export class ProjectMediaDetailComponent {
  projectMedia = input<IProjectMedia | null>(null);

  previousState(): void {
    window.history.back();
  }
}
