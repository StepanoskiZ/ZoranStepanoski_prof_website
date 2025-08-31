import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProjectImage } from '../project-image.model';

@Component({
  selector: 'jhi-project-image-detail',
  templateUrl: './project-image-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProjectImageDetailComponent {
  projectImage = input<IProjectImage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
