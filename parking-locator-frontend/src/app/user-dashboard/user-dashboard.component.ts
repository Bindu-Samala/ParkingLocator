import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { ParkingSpot } from '../models/parking-spot.model';
import { Booking } from '../models/booking.model';
import { User } from '../models/user.model'; // Import the User model

declare var google: any; // Declare google for Google Maps integration

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent implements OnInit {
  parkingSpots: ParkingSpot[] = [];
  selectedSpot: ParkingSpot | null = null;
  bookingDetails: Booking = new Booking(0, new Date(), new Date(), new Date(), new ParkingSpot(0, '', '', false, 0, 0, null), {} as User);
  
  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadAvailableParkingSpots();
  }

  // Load available parking spots
  loadAvailableParkingSpots(): void {
    const latitude = 37.7749; // Example latitude (San Francisco)
    const longitude = -122.4194; // Example longitude (San Francisco)
    const radius = 5; // Search within 5 km radius

    this.userService.getAvailableParkingSpots(latitude, longitude, radius).subscribe(spots => {
      this.parkingSpots = spots;
      this.initializeMap(spots);  // Initialize map once spots are loaded
    });
  }

  // Initialize Google Map and display parking spots
  initializeMap(spots: ParkingSpot[]): void {
    const mapOptions = {
      center: { lat: 37.7749, lng: -122.4194 }, // Default to San Francisco
      zoom: 12
    };

    const map = new google.maps.Map(document.getElementById("map"), mapOptions);

    spots.forEach(spot => {
      const marker = new google.maps.Marker({
        position: { lat: spot.latitude, lng: spot.longitude },
        map: map,
        title: spot.location
      });

      // Add click event to select a parking spot
      marker.addListener('click', () => {
        this.selectParkingSpot(spot);
      });
    });
  }

  // Select a parking spot
  selectParkingSpot(spot: ParkingSpot): void {
    this.selectedSpot = spot;
    this.bookingDetails.parkingSpot = spot;
  }

  // Book the selected parking spot
  bookParkingSpot(): void {
    if (this.selectedSpot && this.bookingDetails.bookingStartTime && this.bookingDetails.bookingEndTime) {
      this.userService.bookParkingSpot(this.bookingDetails).subscribe(response => {
        alert(response); // Show success message
        this.loadAvailableParkingSpots(); // Reload available parking spots after booking
      });
    } else {
      alert('Please select a parking spot and provide booking details.');
    }
  }
}
