import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BusinessServiceResolve from './route/business-service-routing-resolve.service';

const businessServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/business-service.component').then(m => m.BusinessServiceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/business-service-detail.component').then(m => m.BusinessServiceDetailComponent),
    resolve: {
      businessService: BusinessServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/business-service-update.component').then(m => m.BusinessServiceUpdateComponent),
    resolve: {
      businessService: BusinessServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/business-service-update.component').then(m => m.BusinessServiceUpdateComponent),
    resolve: {
      businessService: BusinessServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessServiceRoute;
