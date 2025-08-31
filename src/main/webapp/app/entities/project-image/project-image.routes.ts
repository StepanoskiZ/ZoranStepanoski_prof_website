import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProjectImageResolve from './route/project-image-routing-resolve.service';

const projectImageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/project-image.component').then(m => m.ProjectImageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/project-image-detail.component').then(m => m.ProjectImageDetailComponent),
    resolve: {
      projectImage: ProjectImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/project-image-update.component').then(m => m.ProjectImageUpdateComponent),
    resolve: {
      projectImage: ProjectImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/project-image-update.component').then(m => m.ProjectImageUpdateComponent),
    resolve: {
      projectImage: ProjectImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default projectImageRoute;
