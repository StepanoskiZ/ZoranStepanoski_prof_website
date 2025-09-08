import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import MainComponent from './layouts/main/main.component';
import { LandingComponent } from './landing/landing.component';

const routes: Routes = [
  // This is the parent route. It loads the "frame" for your public site.
  //   {
  //     path: '', // The default path loads your LandingComponent
  //     component: LandingComponent,
  //     title: 'global.mainTitle',
  //   },
  {
    path: '',
    component: MainComponent, // It loads the MainComponent, which contains your navbar, footer, and initializes all services.
    children: [
      // This is the child route. It's the content that goes INSIDE the frame.
      {
        path: '', // When the URL is the base URL, this child is activated.
        component: LandingComponent,
        title: 'global.mainTitle', // Now this will be translated correctly!
      },
      {
        path: '', // This empty path acts as a prefix for the routes inside entity.routes
        loadChildren: () => import(`./entities/entity.routes`),
      },
    ],
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
  ...errorRoute,
];

export default routes;
