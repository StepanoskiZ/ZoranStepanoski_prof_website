import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IBlogPost } from '../blog-post.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';

@Component({
  selector: 'jhi-blog-post-detail',
  templateUrl: './blog-post-detail.component.html',
  imports: [AlertComponent, AlertErrorComponent, SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BlogPostDetailComponent {
  blogPost = input<IBlogPost | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
