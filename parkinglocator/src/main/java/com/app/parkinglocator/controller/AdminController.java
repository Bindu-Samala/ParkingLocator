package com.app.parkinglocator.controller;

import com.app.parkinglocator.entity.Booking;
import com.app.parkinglocator.entity.ParkingSpot;
import com.app.parkinglocator.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    // Endpoint to get all bookings (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/allBookings")
    public List<Booking> getAllBookings() {
        return parkingSpotService.getAllBookings();
    }

    // Endpoint to get all parking spots (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/allSpots")
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    // Endpoint to get available parking spots within a radius (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/availableSpots")
    public List<ParkingSpot> getAvailableParkingSpots(
            @RequestParam double latitude, 
            @RequestParam double longitude, 
            @RequestParam double radius) {
        return parkingSpotService.getAvailableParkingSpots(latitude, longitude, radius);
    }

    // Endpoint to create a new parking spot (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createSpot")
    public ParkingSpot createParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        return parkingSpotService.createParkingSpot(parkingSpot);
    }

    // Endpoint to update an existing parking spot (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateSpot/{spotId}")
    public ParkingSpot updateParkingSpot(@PathVariable Long spotId, @RequestBody ParkingSpot updatedSpot) {
        return parkingSpotService.updateParkingSpot(spotId, updatedSpot);
    }

    // Endpoint to delete a parking spot (only for admin)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteSpot/{spotId}")
    public void deleteParkingSpot(@PathVariable Long spotId) {
        parkingSpotService.deleteParkingSpot(spotId);
    }

    // Endpoint to assign a booking to a user for a parking spot (only for admin)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
   // @PostMapping("/assignBooking/{spotId}/{userId}")
    /*public Booking assignBookingToUser(@PathVariable Long spotId, @PathVariable Long userId,
                                       @RequestParam String startTime, @RequestParam String endTime) {
        return parkingSpotService.assignBookingToUser(spotId, userId, startTime, endTime);
    }*/
}
