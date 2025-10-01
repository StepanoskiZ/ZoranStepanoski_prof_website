import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { BlogPostService } from '../service/blog-post.service';
import { IBlogPost } from '../blog-post.model';
import { BlogPostFormGroup, BlogPostFormService } from './blog-post-form.service';
import { QuillModule } from 'ngx-quill';

@Component({
  selector: 'jhi-blog-post-update',
  standalone: true,
  templateUrl: './blog-post-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, QuillModule],
})
export class BlogPostUpdateComponent implements OnInit {
  isSaving = false;
  blogPost: IBlogPost | null = null;

  editForm: BlogPostFormGroup = this.blogPostFormService.createBlogPostFormGroup();

  editorModules = {
    toolbar: [
      [{ header: [1, 2, 3, false] }],
      ['bold', 'italic', 'underline'],
      ['link', 'image', 'video', 'code-block'],
    ],
  };

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected blogPostService: BlogPostService,
    protected blogPostFormService: BlogPostFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogPost }) => {
      this.blogPost = blogPost;
      if (blogPost) {
        this.updateForm(blogPost);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('zsWebsiteApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blogPost = this.blogPostFormService.getBlogPost(this.editForm);
    if (blogPost.id !== null) {
      this.subscribeToSaveResponse(this.blogPostService.update(blogPost));
    } else {
      this.subscribeToSaveResponse(this.blogPostService.create(blogPost));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlogPost>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(blogPost: IBlogPost): void {
    this.blogPost = blogPost;
    this.blogPostFormService.resetForm(this.editForm, blogPost);
  }
}
