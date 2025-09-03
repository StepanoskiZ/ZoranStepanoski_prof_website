import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import MainComponent from './layouts/main/main.component';
import { LandingComponent } from './landing/landing.component';

const routes: Routes = [
  // This is the parent route. It loads the "frame" for your public site.
  {
    path: '', // The default path loads your LandingComponent
    component: LandingComponent,
    title: 'Zoran Stepanoski - Professional Portfolio',
  },

  // These are the standard JHipster routes for admin, login, etc.
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'), // CORRECTED: It's account.route.ts (singular)
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
