import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PageContentResolve from './route/page-content-routing-resolve.service';

const pageContentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/page-content.component').then(m => m.PageContentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/page-content-detail.component').then(m => m.PageContentDetailComponent),
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/page-content-update.component').then(m => m.PageContentUpdateComponent),
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/page-content-update.component').then(m => m.PageContentUpdateComponent),
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pageContentRoute;
