import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { User } from '../models/user.model';  // Import the User model

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user: User = {
    username: '',
    email: '',
    password: ''
  };

  confirmPassword: string = '';  // Local variable for confirm password
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (this.user.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match!';
      return;
    }

    this.authService.register(this.user).subscribe({
      next: (response) => {
        console.log(response);  // Log the response for debugging
        this.successMessage = response;  // Display success message
        setTimeout(() => {
          this.router.navigate(['/login']);  // Redirect to login after successful registration
        }, 2000);
      },
      error: (error) => {
        console.log(error);
        this.errorMessage = error.error || 'An error occurred during registration';  // Display error message
      }
    });
  }
}
