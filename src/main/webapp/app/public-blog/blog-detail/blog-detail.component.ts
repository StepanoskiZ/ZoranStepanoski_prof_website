import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { BlogPost } from '../blog-list/blog-list.component'; // Reuse the same interface

@Component({
  selector: 'app-blog-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './blog-detail.component.html',
  styleUrls: ['./blog-detail.component.scss'],
})
export class BlogDetailComponent implements OnInit {
  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  post$!: Observable<BlogPost>;

  ngOnInit(): void {
    // This creates an observable that gets the 'slug' from the URL,
    // then uses it to fetch the correct blog post from the API.
    this.post$ = this.route.params.pipe(
      switchMap(params => {
        const id = params['id'];
        return this.http.get<BlogPost>(`/api/blog-posts/${id}`);
      }),
    );
  }
}
