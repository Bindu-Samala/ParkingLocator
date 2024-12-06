import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking.model';
import { ParkingSpot } from '../models/parking-spot.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/user'; // Update with your backend API URL

  constructor(private http: HttpClient) {}

  // Get available parking spots based on location and radius
  getAvailableParkingSpots(latitude: number, longitude: number, radius: number): Observable<ParkingSpot[]> {
    const params = new HttpParams()
      .set('latitude', latitude.toString())
      .set('longitude', longitude.toString())
      .set('radius', radius.toString());

    return this.http.get<ParkingSpot[]>(`${this.baseUrl}/availableSpots`, { params });
  }

  // Book a parking spot
  bookParkingSpot(bookingDetails: Booking): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/bookSpot`, bookingDetails);
  }
}
