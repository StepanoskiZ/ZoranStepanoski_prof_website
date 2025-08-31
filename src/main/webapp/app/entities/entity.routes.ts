import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'zsWebsiteApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'zsWebsiteApp.project.home.title' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'project-image',
    data: { pageTitle: 'zsWebsiteApp.projectImage.home.title' },
    loadChildren: () => import('./project-image/project-image.routes'),
  },
  {
    path: 'skill',
    data: { pageTitle: 'zsWebsiteApp.skill.home.title' },
    loadChildren: () => import('./skill/skill.routes'),
  },
  {
    path: 'business-service',
    data: { pageTitle: 'zsWebsiteApp.businessService.home.title' },
    loadChildren: () => import('./business-service/business-service.routes'),
  },
  {
    path: 'blog-post',
    data: { pageTitle: 'zsWebsiteApp.blogPost.home.title' },
    loadChildren: () => import('./blog-post/blog-post.routes'),
  },
  {
    path: 'contact-message',
    data: { pageTitle: 'zsWebsiteApp.contactMessage.home.title' },
    loadChildren: () => import('./contact-message/contact-message.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
