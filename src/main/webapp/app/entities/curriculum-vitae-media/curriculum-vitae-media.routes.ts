import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CurriculumVitaeMediaComponent } from './list/curriculum-vitae-media.component';
import { CurriculumVitaeMediaDetailComponent } from './detail/curriculum-vitae-media-detail.component';
import { CurriculumVitaeMediaUpdateComponent } from './update/curriculum-vitae-media-update.component';
import CurriculumVitaeMediaResolve from './route/curriculum-vitae-media-routing-resolve.service';

const curriculumVitaeMediaRoute: Routes = [
  {
    path: '',
    component: CurriculumVitaeMediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurriculumVitaeMediaDetailComponent,
    resolve: {
      curriculumVitaeMedia: CurriculumVitaeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurriculumVitaeMediaUpdateComponent,
    resolve: {
      curriculumVitaeMedia: CurriculumVitaeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurriculumVitaeMediaUpdateComponent,
    resolve: {
      curriculumVitaeMedia: CurriculumVitaeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default curriculumVitaeMediaRoute;
