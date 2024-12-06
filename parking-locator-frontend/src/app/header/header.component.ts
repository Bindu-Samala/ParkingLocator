import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';  // Import AuthService to get user data and logout
import { Router } from '@angular/router';
import { User } from '../models/user.model';  // Import User model to type-check the user data

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currentUser: User | null = null;  // To hold the current user object
  userRoles: string[] = [];  // Store user roles, can be used for role-based navigation
  menuVisible: boolean = false;  // Toggle for the dropdown menu
  isHeaderDisabled: boolean = false;  // Flag to disable the header

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Subscribe to the currentUser observable to get user data
    this.authService.currentUser.subscribe({
      next: (user: User | null) => {
        if (user) {
          this.currentUser = user;
          this.userRoles = user.roles?.map(role => role.name) || [];  // Extract roles if available
        } else {
          this.currentUser = null;
          this.userRoles = [];
        }
      },
      error: (err) => {
        console.error("Error fetching user data: ", err);
      }
    });
  }

  // Toggle the dropdown menu visibility
  toggleMenu(): void {
    this.menuVisible = !this.menuVisible;
  }

  // Logout method
  logout(): void {
    this.authService.logout();  // Call logout method in AuthService
    this.isHeaderDisabled = true; // Disable the header when logging out
    setTimeout(() => {
      this.router.navigate(['/login']);  // Redirect to login page
    }, 1000);  // Add a small delay for a smooth logout experience
  }

  // Helper method to check if the user has a specific role
  hasRole(role: string): boolean {
    return this.userRoles.includes(role);
  }
}
