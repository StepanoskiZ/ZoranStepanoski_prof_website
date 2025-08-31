import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IBusinessService } from '../business-service.model';

@Component({
  selector: 'jhi-business-service-detail',
  templateUrl: './business-service-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class BusinessServiceDetailComponent {
  businessService = input<IBusinessService | null>(null);

  previousState(): void {
    window.history.back();
  }
}
