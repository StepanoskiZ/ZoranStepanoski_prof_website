import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BlogPostComponent } from './list/blog-post.component';
import { BlogPostDetailComponent } from './detail/blog-post-detail.component';
import { BlogPostUpdateComponent } from './update/blog-post-update.component';
import BlogPostResolve from './route/blog-post-routing-resolve.service';

const blogPostRoute: Routes = [
  {
    path: '',
    component: BlogPostComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlogPostDetailComponent,
    resolve: {
      blogPost: BlogPostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlogPostUpdateComponent,
    resolve: {
      blogPost: BlogPostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlogPostUpdateComponent,
    resolve: {
      blogPost: BlogPostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default blogPostRoute;
