import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import SharedModule from '../../shared/shared.module';

export interface VisitorStats {
  totalVisits: number;
  uniqueVisitors: number;
}

@Component({
  selector: 'jhi-visitor-stats',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './visitor-stats.component.html',
})
export class VisitorStatsComponent implements OnInit {
  private http = inject(HttpClient);
  stats: VisitorStats | null = null;
  isLoading = true;

  ngOnInit(): void {
    this.isLoading = true;
    this.http.get<VisitorStats>('/api/visitor-log/stats').subscribe({
      next: data => {
        this.stats = data;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }
}
