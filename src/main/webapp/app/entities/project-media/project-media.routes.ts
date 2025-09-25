import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProjectMediaResolve from './route/project-media-routing-resolve.service';

const projectMediaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/project-media.component').then(m => m.ProjectMediaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/project-media-detail.component').then(m => m.ProjectMediaDetailComponent),
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/project-media-update.component').then(m => m.ProjectMediaUpdateComponent),
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/project-media-update.component').then(m => m.ProjectMediaUpdateComponent),
    resolve: {
      projectMedia: ProjectMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default projectMediaRoute;
