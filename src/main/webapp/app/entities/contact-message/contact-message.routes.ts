import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContactMessageComponent } from './list/contact-message.component';
import { ContactMessageDetailComponent } from './detail/contact-message-detail.component';
import { ContactMessageUpdateComponent } from './update/contact-message-update.component';
import ContactMessageResolve from './route/contact-message-routing-resolve.service';

const contactMessageRoute: Routes = [
  {
    path: '',
    component: ContactMessageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactMessageDetailComponent,
    resolve: {
      contactMessage: ContactMessageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactMessageUpdateComponent,
    resolve: {
      contactMessage: ContactMessageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactMessageUpdateComponent,
    resolve: {
      contactMessage: ContactMessageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contactMessageRoute;
