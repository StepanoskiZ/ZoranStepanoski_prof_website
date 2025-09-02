import { Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';

export const routes: Routes = [
  { path: '', component: LandingComponent, title: 'ZORAN STEPANOSKI - Home' }, // default landing page
  { path: 'about', loadChildren: () => import('./about/about.module').then(m => m.AboutModule) },
  { path: 'services', loadChildren: () => import('./services/services.module').then(m => m.ServicesModule) },
  { path: 'projects', loadChildren: () => import('./projects/projects.module').then(m => m.ProjectsModule) },
  { path: 'gallery', loadChildren: () => import('./gallery/gallery.module').then(m => m.GalleryModule) },
  { path: 'blog', loadChildren: () => import('./blog/blog.module').then(m => m.BlogModule) },
  { path: 'contact', loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule) },
  //   {
  //     path: 'admin',
  //     loadChildren: () => import('./admin/admin.routes'),
  //   },
  //   {
  //     path: 'account',
  //     loadChildren: () => import('./account/account.routes'),
  //   },
  // Fallback route
  { path: '**', redirectTo: '' },
];
