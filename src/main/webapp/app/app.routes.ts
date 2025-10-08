import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';
import MainComponent from './layouts/main/main.component';
import { LandingComponent } from './landing/landing.component';
// import { PrivacyPolicyComponent } from './privacy-policy/privacy-policy.component';
// import { IntakeFormComponent } from './intake-form/intake-form.component';
// import BLOG_ROUTES from './public-blog/blog.routes';

const routes: Routes = [
  // --- Main Application Layout Route ---
  // This is the single entry point for all pages that show the navbar and footer.
  {
    path: '',
    component: MainComponent,
    children: [
      // Public-facing pages
      {
        path: '', // The root path is the landing page
        component: LandingComponent,
        title: 'global.mainTitle',
      },
      {
        path: 'blog',
        loadComponent: () => import('./public-blog/blog-detail/public-blog.component').then(m => m.PublicBlogComponent),
        title: 'blog.title',
      },
      {
        path: 'privacy-policy',
        loadComponent: () => import('./privacy-policy/privacy-policy.component').then(m => m.PrivacyPolicyComponent), // Use lazy loading
        title: 'privacyPolicy.title',
      },
      {
        path: 'intake-form',
        loadComponent: () => import('./intake-form/intake-form.component').then(m => m.IntakeFormComponent), // Use lazy loading
        title: 'intakeForm.title',
      },
      {
        path: 'ai-generator',
        canActivate: [UserRouteAccessService], // Protects the route, requires login
        loadComponent: () => import('./ai-generator/ai-generator.component').then(m => m.CvAiGeneratorComponent), // Lazy loads the component
        title: 'aiGenerator.title', // Use a translation key for the page title
      },
      // All blog routes are also children of the main layout
//       ...BLOG_ROUTES,

      // JHipster-generated entity routes for admins should ALSO be children
      // so they appear within the main layout.
      {
        path: '',
        canActivate: [UserRouteAccessService],
        loadChildren: () => import(`./entities/entity.routes`),
      },
    ],
  },

  // --- Full-Page Routes (No Main Layout) ---
  // These routes take over the entire screen (admin dashboard, login page, etc.)
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

  // Error routes are at the top level
  ...errorRoute,
];

export default routes;
