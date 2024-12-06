package com.app.parkinglocator.controller;

import com.app.parkinglocator.entity.Booking;
import com.app.parkinglocator.entity.ParkingSpot;
import com.app.parkinglocator.service.ParkingSpotService;
import com.app.parkinglocator.entity.User;
import com.app.parkinglocator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ParkingSpotService parkingSpotService;
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        // Get the currently authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Fetch the username from the authentication object
            String username = authentication.getName();
            
            // Fetch the full user entity from the repository using the username
            User user = userRepository.findByUsername(username)
                    .orElse(null); // Return null if not found

            if (user == null) {
                // Return 404 if the user does not exist
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // Return the full User object (this contains roles, email, etc.)
            return ResponseEntity.ok(user);
        }

        // Return 401 if the user is not authenticated
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(null);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/availableSpots")
    public ResponseEntity<List<ParkingSpot>> getAvailableParkingSpots(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        List<ParkingSpot> spots = parkingSpotService.getAvailableParkingSpots(latitude, longitude, radius);
        
        // Return the available spots with a 200 OK status
        return ResponseEntity.ok(spots);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/bookSpot")
    public ResponseEntity<String> bookParkingSpot(@RequestBody Booking bookingRequest) {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        ParkingSpot spot = parkingSpotService.findById(bookingRequest.getParkingSpot().getId());

        if (spot == null || spot.isBooked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Spot is not available");
        }

        // Proceed with booking
        bookingRequest.setUser(user); // Set the authenticated user
        bookingRequest.setBookingDate(new Date()); // Set the current date as the booking date
        parkingSpotService.assignBookingToUser(bookingRequest, user);
        return ResponseEntity.ok("Booking Successful!");
    }

}
