import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BusinessServiceMediaComponent } from './list/business-service-media.component';
import { BusinessServiceMediaDetailComponent } from './detail/business-service-media-detail.component';
import { BusinessServiceMediaUpdateComponent } from './update/business-service-media-update.component';
import BusinessServiceMediaResolve from './route/business-service-media-routing-resolve.service';

const businessServiceMediaRoute: Routes = [
  {
    path: '',
    component: BusinessServiceMediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessServiceMediaDetailComponent,
    resolve: {
      businessServiceMedia: BusinessServiceMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessServiceMediaUpdateComponent,
    resolve: {
      businessServiceMedia: BusinessServiceMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessServiceMediaUpdateComponent,
    resolve: {
      businessServiceMedia: BusinessServiceMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessServiceMediaRoute;
