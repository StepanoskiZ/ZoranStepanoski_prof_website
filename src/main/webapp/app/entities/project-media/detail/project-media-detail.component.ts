import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProjectMedia } from '../project-media.model';

@Component({
  selector: 'jhi-project-media-detail',
  templateUrl: './project-media-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProjectMediaDetailComponent {
  projectMedia = input<IProjectMedia | null>(null);

  previousState(): void {
    window.history.back();
  }
}
