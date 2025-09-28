import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AboutMeMediaComponent } from './list/about-me-media.component';
import { AboutMeMediaDetailComponent } from './detail/about-me-media-detail.component';
import { AboutMeMediaUpdateComponent } from './update/about-me-media-update.component';
import AboutMeMediaResolve from './route/about-me-media-routing-resolve.service';

const aboutMeMediaRoute: Routes = [
  {
    path: '',
    component: AboutMeMediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AboutMeMediaDetailComponent,
    resolve: {
      aboutMeMedia: AboutMeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AboutMeMediaUpdateComponent,
    resolve: {
      aboutMeMedia: AboutMeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AboutMeMediaUpdateComponent,
    resolve: {
      aboutMeMedia: AboutMeMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aboutMeMediaRoute;
