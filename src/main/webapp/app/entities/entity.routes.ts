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
    path: 'project-media',
    data: { pageTitle: 'zsWebsiteApp.projectImage.home.title' },
    loadChildren: () => import('./project-media/project-media.routes'),
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
  {
    path: 'visitor-logs',
    data: { pageTitle: 'zsWebsiteApp.visitorLog.home.title' },
    loadChildren: () => import('./visitor-log/visitor-log.routes'),
  },
  {
    path: 'page-content',
    data: { pageTitle: 'zsWebsiteApp.pageContent.home.title' },
    loadChildren: () => import('./page-content/page-content.routes'),
  },
  {
    path: 'visitor-log',
    data: { pageTitle: 'zsWebsiteApp.visitorLog.home.title' },
    loadChildren: () => import('./visitor-log/visitor-log.routes'),
  },
  {
    path: 'page-content-media',
    data: { pageTitle: 'zsWebsiteApp.pageContentMedia.home.title' },
    loadChildren: () => import('./page-content-media/page-content-media.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
