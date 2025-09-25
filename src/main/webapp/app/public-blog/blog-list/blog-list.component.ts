import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

// Define an interface for the BlogPost data we expect from the API
export interface BlogPost {
  id: number;
  title: string;
  content: string;
  slug: string;
  imageUrl?: string;
  publishedDate: string;
}

@Component({
  selector: 'app-blog-list',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './blog-list.component.html',
  styleUrls: ['./blog-list.component.scss'],
})
export class BlogListComponent implements OnInit {
  private http = inject(HttpClient);
  posts: BlogPost[] = [];
  isLoading = true;

  ngOnInit(): void {
    // Fetch the list of blog posts from your public API endpoint
    this.http.get<BlogPost[]>('/api/blog-posts').subscribe({
      next: data => {
        this.posts = data;
        this.isLoading = false;
      },
      error: () => {
        // Handle potential errors, e.g., show an error message
        this.isLoading = false;
      },
    });
  }
}
