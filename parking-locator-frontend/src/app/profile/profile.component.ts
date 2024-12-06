import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';  // Import AuthService to handle user data
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;  // Store the current user data
  selectedOption: string = 'profile';  // Default selected option is 'Profile'
  bookingHistory = [
    // Sample data for Booking History
    { date: '2024-11-01', location: 'Downtown', duration: 2, cost: 20 },
    { date: '2024-11-05', location: 'Airport', duration: 3, cost: 30 }
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Get the current user data when the component loads
    this.currentUser = this.authService.getCurrentUser();
  }

  setSelectedOption(option: string): void {
    this.selectedOption = option;  // Change selected option based on user clicks
  }

  logout(): void {
    this.authService.logout();  // Call logout method from AuthService
    this.router.navigate(['/login']);  // Redirect to the login page
  }
}
