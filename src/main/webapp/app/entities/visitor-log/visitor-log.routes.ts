import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VisitorLogResolve from './route/visitor-log-routing-resolve.service';

const visitorLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/visitor-log.component').then(m => m.VisitorLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/visitor-log-detail.component').then(m => m.VisitorLogDetailComponent),
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/visitor-log-update.component').then(m => m.VisitorLogUpdateComponent),
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/visitor-log-update.component').then(m => m.VisitorLogUpdateComponent),
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default visitorLogRoute;
