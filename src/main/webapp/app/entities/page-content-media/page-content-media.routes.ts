import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PageContentMediaResolve from './route/page-content-media-routing-resolve.service';

const pageContentMediaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/page-content-media.component').then(m => m.PageContentMediaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/page-content-media-detail.component').then(m => m.PageContentMediaDetailComponent),
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/page-content-media-update.component').then(m => m.PageContentMediaUpdateComponent),
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/page-content-media-update.component').then(m => m.PageContentMediaUpdateComponent),
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pageContentMediaRoute;
