import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VisitorLogComponent } from './list/visitor-log.component';
import { VisitorLogDetailComponent } from './detail/visitor-log-detail.component';
import { VisitorLogUpdateComponent } from './update/visitor-log-update.component';
import VisitorLogResolve from './route/visitor-log-routing-resolve.service';
import { VisitorLogMapComponent } from './map/visitor-log-map/visitor-log-map.component';

const visitorLogRoute: Routes = [
  {
    path: '',
    component: VisitorLogComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'map',
    component: VisitorLogMapComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VisitorLogDetailComponent,
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VisitorLogUpdateComponent,
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VisitorLogUpdateComponent,
    resolve: {
      visitorLog: VisitorLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default visitorLogRoute;
