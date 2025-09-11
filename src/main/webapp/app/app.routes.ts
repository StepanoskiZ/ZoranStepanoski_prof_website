import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import MainComponent from './layouts/main/main.component';
import { LandingComponent } from './landing/landing.component';
import BLOG_ROUTES from './public-blog/blog.routes';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children: [
      // This is the child route. It's the content that goes INSIDE the frame.
      {
        path: '', // The root path is the landing page
        component: LandingComponent,
        title: 'global.mainTitle',
      },
      // We spread the public blog routes here so they are children of MainComponent
      ...BLOG_ROUTES,
    ],
  },

  // These are the standard JHipster routes for admin, login, etc.
  {
    path: 'admin',
    data: { authorities: [Authority.ADMIN] },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    canActivate: [UserRouteAccessService],
    loadChildren: () => import(`./entities/entity.routes`),
  },

  ...errorRoute,
];

export default routes;
