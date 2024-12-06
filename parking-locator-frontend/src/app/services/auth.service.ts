import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { User } from '../models/user.model'; // Import User model

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080'; // Replace with your backend API URL
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(private http: HttpClient, private router: Router) {
    // Check if window is available (this ensures it runs only in the browser)
    const currentUserData = typeof window !== 'undefined' && window.localStorage
      ? JSON.parse(localStorage.getItem('currentUser') || 'null')
      : null;

    this.currentUserSubject = new BehaviorSubject<User | null>(currentUserData);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  // Get the current authenticated user (after successful login)
  getCurrentUser(): Observable<User> {
    console.log("Hi");
    return this.http.get<User>(`${this.apiUrl}/user/me`, { withCredentials: true });
  }
  setCurrentUser(user: User): void {
    this.currentUserSubject.next(user);
  }


  // Logout method to invalidate the session
  logout(): void {
    // Send a POST request to the backend logout endpoint
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe(() => {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.removeItem('currentUser');  // Remove user from localStorage
      }
      this.currentUserSubject.next(null);  // Update the currentUserBehaviorSubject to null
      this.router.navigate(['/login']);  // Navigate to the login page
    });
  }

  // Getter for currentUserValue
  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }


  // Register method to send new user data to backend
  register(user: User): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/auth/register`, user, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'  // Send data as JSON
      }),
      withCredentials: true,  // Include cookies with the request
      responseType: 'text' as 'json'  // Expect plain text response, not JSON
    });
  }
  
  
}
