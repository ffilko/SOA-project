import { Component, OnInit, AfterViewInit } from '@angular/core';
import { PositionService } from '../../services/position.service';
import { AuthService } from '../../services/auth.service';
import * as L from 'leaflet';

const iconDefault = L.icon({
  iconRetinaUrl: 'assets/marker-icon-2x.png',
  iconUrl: 'assets/marker-icon.png',
  shadowUrl: 'assets/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-position-simulator',
  templateUrl: './position-simulator.component.html',
  styleUrls: ['./position-simulator.component.css']
})
export class PositionSimulatorComponent implements OnInit, AfterViewInit {
  private map!: L.Map;
  private marker?: L.Marker;
  touristId = this.authService.getUserId() || 'tourist-123';

  constructor(private positionService: PositionService, private authService: AuthService) {}

  ngOnInit(): void {
    this.positionService.getByTourist(this.touristId).subscribe(position => {
      if (position) {
        this.setMarker(position.latitude, position.longitude);
      }
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
    this.initMap();
  }, 0);
  }

  private initMap(): void {
    this.map = L.map('map').setView([44.8176, 20.4569], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      const { lat, lng } = e.latlng;
      this.setMarker(lat, lng);
      this.positionService.upsert({
        touristId: this.touristId,
        latitude: lat,
        longitude: lng
      }).subscribe();
    });
  }

  private setMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.setLatLng([lat, lng]);
    } else {
      this.marker = L.marker([lat, lng]).addTo(this.map);
    }
  }
}