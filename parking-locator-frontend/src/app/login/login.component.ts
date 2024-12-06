import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { AuthService } from '../services/auth.service';  // Ensure you have AuthService for getting current user info
import { User } from '../models/user.model';  // User model for type-checking

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  isSubmitting: boolean = false;

  constructor(private router: Router, private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  login(): void {
    this.isSubmitting = true;  // Disable the submit button during login

    // Prepare the form data
    const body = new HttpParams()
      .set('username', this.username)
      .set('password', this.password);

    // Send login request to Spring Security
    this.http.post<any>('http://localhost:8080/login', body, {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      withCredentials: true
    }).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.errorMessage = '';
        this.successMessage = 'Login successful!';

        // Fetch the current authenticated user with roles
        this.authService.getCurrentUser().subscribe({
          next: (user: User) => {
            // Set the user data in AuthService
            this.authService.setCurrentUser(user);  // Update current user in AuthService

            // Based on user roles, navigate to appropriate page
            const userRoles = user.roles ?? [];
            if (userRoles.some(role => role.name === 'ROLE_ADMIN')) {
              this.router.navigate(['/admin-dashboard']);
            } else if (userRoles.some(role => role.name === 'ROLE_USER')) {
              this.router.navigate(['/user-dashboard']);
            } else {
              this.router.navigate(['/']);
            }
          },
          error: (err) => {
            this.isSubmitting = false;
            this.errorMessage = 'Could not retrieve user details.';
            console.error(err);
          }
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = 'Login failed: Invalid credentials!';
        console.error(err);
      }
    });
  }
}
