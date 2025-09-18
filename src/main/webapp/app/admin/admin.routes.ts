import { Routes } from '@angular/router';
import { VisitorStatsComponent } from './visitor-stats/visitor-stats.component';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

const routes: Routes = [
  {
    path: 'user-management',
    loadChildren: () => import('./user-management/user-management.route'),
    title: 'userManagement.home.title',
  },
  {
    path: 'docs',
    loadComponent: () => import('./docs/docs.component'),
    title: 'global.menu.admin.apidocs',
  },
  {
    path: 'configuration',
    loadComponent: () => import('./configuration/configuration.component'),
    title: 'configuration.title',
  },
  {
    path: 'health',
    loadComponent: () => import('./health/health.component'),
    title: 'health.title',
  },
  {
    path: 'logs',
    loadComponent: () => import('./logs/logs.component'),
    title: 'logs.title',
  },
  {
    path: 'metrics',
    loadComponent: () => import('./metrics/metrics.component'),
    title: 'metrics.title',
  },
  {
    path: 'visitor-stats', // <-- ADD THIS ROUTE OBJECT
    component: VisitorStatsComponent,
    title: 'global.menu.admin.visitorStats',
  },
  /* jhipster-needle-add-admin-route - JHipster will add admin routes here */
];

export default routes;
