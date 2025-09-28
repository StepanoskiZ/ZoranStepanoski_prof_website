import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAuthority } from '../authority.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';

@Component({
  selector: 'jhi-authority-detail',
  standalone: true,
  templateUrl: './authority-detail.component.html',
  imports: [AlertComponent, AlertErrorComponent, SharedModule, RouterModule],
})
export class AuthorityDetailComponent {
  authority = input<IAuthority | null>(null);

  previousState(): void {
    window.history.back();
  }
}
