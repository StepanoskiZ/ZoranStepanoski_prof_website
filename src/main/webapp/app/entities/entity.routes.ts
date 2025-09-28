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
    path: 'visitor-log',
    data: { pageTitle: 'zsWebsiteApp.visitorLog.home.title' },
    loadChildren: () => import('./visitor-log/visitor-log.routes'),
  },
  {
    path: 'about-me',
    data: { pageTitle: 'zsWebsiteApp.aboutMe.home.title' },
    loadChildren: () => import('./about-me/about-me.routes'),
  },
  {
    path: 'about-me-media',
    data: { pageTitle: 'zsWebsiteApp.aboutMeMedia.home.title' },
    loadChildren: () => import('./about-me-media/about-me-media.routes'),
  },
  {
    path: 'curriculum-vitae',
    data: { pageTitle: 'zsWebsiteApp.curriculumVitae.home.title' },
    loadChildren: () => import('./curriculum-vitae/curriculum-vitae.routes'),
  },
  {
    path: 'curriculum-vitae-media',
    data: { pageTitle: 'zsWebsiteApp.curriculumVitaeMedia.home.title' },
    loadChildren: () => import('./curriculum-vitae-media/curriculum-vitae-media.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
