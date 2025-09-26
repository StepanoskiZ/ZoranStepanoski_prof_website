import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBlogPost } from '../blog-post.model';
import { BlogPostService } from '../service/blog-post.service';

@Component({
  standalone: true,
  templateUrl: './blog-post-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BlogPostDeleteDialogComponent {
  blogPost?: IBlogPost;

  constructor(
    protected blogPostService: BlogPostService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blogPostService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
