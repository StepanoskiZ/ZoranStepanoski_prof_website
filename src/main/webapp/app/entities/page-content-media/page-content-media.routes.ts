import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PageContentMediaComponent } from './list/page-content-media.component';
import { PageContentMediaDetailComponent } from './detail/page-content-media-detail.component';
import { PageContentMediaUpdateComponent } from './update/page-content-media-update.component';
import PageContentMediaResolve from './route/page-content-media-routing-resolve.service';

const pageContentMediaRoute: Routes = [
  {
    path: '',
    component: PageContentMediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageContentMediaDetailComponent,
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageContentMediaUpdateComponent,
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageContentMediaUpdateComponent,
    resolve: {
      pageContentMedia: PageContentMediaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pageContentMediaRoute;
