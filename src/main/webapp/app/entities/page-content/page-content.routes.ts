import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PageContentComponent } from './list/page-content.component';
import { PageContentDetailComponent } from './detail/page-content-detail.component';
import { PageContentUpdateComponent } from './update/page-content-update.component';
import PageContentResolve from './route/page-content-routing-resolve.service';

const pageContentRoute: Routes = [
  {
    path: '',
    component: PageContentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageContentDetailComponent,
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageContentUpdateComponent,
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageContentUpdateComponent,
    resolve: {
      pageContent: PageContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pageContentRoute;
