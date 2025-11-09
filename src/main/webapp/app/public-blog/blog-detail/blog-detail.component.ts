import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { BlogPost } from '../blog-list/blog-list.component'; // Reuse the same interface
import { tap } from 'rxjs/operators';
import { SafeHtmlPipe } from 'app/shared/pipes/safe-html.pipe';
import { RemoveSoftHyphensPipe } from 'app/shared/pipes/remove-soft-hyphens.pipe';

@Component({
  selector: 'app-blog-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, SafeHtmlPipe, RemoveSoftHyphensPipe],
  templateUrl: './blog-detail.component.html',
  styleUrls: ['./blog-detail.component.scss'],
})
export class BlogDetailComponent implements OnInit {
  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  post$!: Observable<BlogPost>;

  ngOnInit(): void {
    this.post$ = this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        return this.http.get<BlogPost>(`/api/blog-posts/${id}`);
      }),
      tap(post => {
        console.log('Blog post data received from API:', post);
      }),
    );
  }
}
