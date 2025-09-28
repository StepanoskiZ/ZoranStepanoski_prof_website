import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CurriculumVitaeComponent } from './list/curriculum-vitae.component';
import { CurriculumVitaeDetailComponent } from './detail/curriculum-vitae-detail.component';
import { CurriculumVitaeUpdateComponent } from './update/curriculum-vitae-update.component';
import CurriculumVitaeResolve from './route/curriculum-vitae-routing-resolve.service';

const curriculumVitaeRoute: Routes = [
  {
    path: '',
    component: CurriculumVitaeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurriculumVitaeDetailComponent,
    resolve: {
      curriculumVitae: CurriculumVitaeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurriculumVitaeUpdateComponent,
    resolve: {
      curriculumVitae: CurriculumVitaeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurriculumVitaeUpdateComponent,
    resolve: {
      curriculumVitae: CurriculumVitaeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default curriculumVitaeRoute;
