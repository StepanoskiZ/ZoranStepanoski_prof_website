import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AboutMeComponent } from './list/about-me.component';
import { AboutMeDetailComponent } from './detail/about-me-detail.component';
import { AboutMeUpdateComponent } from './update/about-me-update.component';
import AboutMeResolve from './route/about-me-routing-resolve.service';

const aboutMeRoute: Routes = [
  {
    path: '',
    component: AboutMeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AboutMeDetailComponent,
    resolve: {
      aboutMe: AboutMeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AboutMeUpdateComponent,
    resolve: {
      aboutMe: AboutMeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AboutMeUpdateComponent,
    resolve: {
      aboutMe: AboutMeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aboutMeRoute;
