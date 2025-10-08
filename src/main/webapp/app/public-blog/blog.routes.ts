// src/main/webapp/app/public-blog/blog.routes.ts
import { Routes } from '@angular/router';
import { BlogListComponent } from './blog-list/blog-list.component';
import { BlogDetailComponent } from './blog-detail/blog-detail.component';

const BLOG_ROUTES: Routes = [
  {
    path: 'blog',
    component: BlogListComponent,
    title: 'global.mainTitle',
  },
  {
    path: 'blog/:id', // This route uses the ':slug' parameter
    component: BlogDetailComponent,
    title: 'global.mainTitle',
  },
];

export default BLOG_ROUTES;
