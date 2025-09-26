import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProjectMediaComponent } from './list/project-media.component';
import { ProjectMediaDetailComponent } from './detail/project-media-detail.component';
import { ProjectMediaUpdateComponent } from './update/project-media-update.component';
import ProjectMediaResolve from './route/project-media-routing-resolve.service';

const projectMediaRoute: Routes = [
  {
    path: '',
    component: ProjectMediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjectMediaDetailComponent,
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjectMediaUpdateComponent,
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProjectMediaUpdateComponent,
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default projectMediaRoute;
