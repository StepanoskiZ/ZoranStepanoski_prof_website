import { Component, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { VisitorLogService } from '../../service/visitor-log.service';
import { IVisitorLog } from '../../visitor-log.model';

@Component({
  selector: 'jhi-visitor-log-map',
  templateUrl: './visitor-log-map.component.html',
  styleUrls: ['./visitor-log-map.component.scss'],
  standalone: true, // If your project uses standalone components
  imports: [], // Add necessary imports if not standalone
})
export class VisitorLogMapComponent implements AfterViewInit {
  private map: L.Map | undefined;

  constructor(private visitorLogService: VisitorLogService) {}

  ngAfterViewInit(): void {
    this.initMap();
    this.loadMarkers();
  }

  private initMap(): void {
    // Set map center and zoom level
    this.map = L.map('map').setView([20, 0], 2); // Centered globally

    // Add the tile layer (the map image)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(this.map);
  }

  private loadMarkers(): void {
    this.visitorLogService.queryGeoPoints().subscribe(res => {
      const points = res.body ?? [];
      if (!this.map) return; // Guard against uninitialized map

      for (const point of points) {
        if (point.latitude && point.longitude) {
          const marker = L.marker([point.latitude, point.longitude]).addTo(this.map);
          // Add a popup with info
          marker.bindPopup(
            `<b>IP:</b> ${point.ipAddress}<br><b>Location:</b> ${point.city ?? ''}, ${point.country ?? ''}<br><b>Page:</b> ${point.pageVisited}`,
          );
        }
      }
    });
  }
}
