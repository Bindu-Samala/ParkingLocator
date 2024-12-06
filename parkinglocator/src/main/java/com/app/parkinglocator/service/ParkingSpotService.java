package com.app.parkinglocator.service;

import com.app.parkinglocator.entity.Booking;
import com.app.parkinglocator.entity.ParkingSpot;
import com.app.parkinglocator.entity.User;
import com.app.parkinglocator.repository.BookingRepository;
import com.app.parkinglocator.repository.ParkingSpotRepository;
import com.app.parkinglocator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // Get available parking spots within a certain radius from a user's location
    public List<ParkingSpot> getAvailableParkingSpots(double userLat, double userLon, double radius) {
        List<ParkingSpot> allParkingSpots = parkingSpotRepository.findByIsBookedFalse(); // Get all available spots

        return allParkingSpots.stream()
                .filter(spot -> calculateDistance(userLat, userLon, spot.getLatitude(), spot.getLongitude()) <= radius)
                .collect(Collectors.toList());
    }

    // Calculate distance between two points on Earth using the Haversine Formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    // Create a new parking spot
    public ParkingSpot createParkingSpot(ParkingSpot parkingSpot) {
        return parkingSpotRepository.save(parkingSpot);
    }

    // Update an existing parking spot
    public ParkingSpot updateParkingSpot(Long spotId, ParkingSpot updatedSpot) {
        Optional<ParkingSpot> existingSpotOpt = parkingSpotRepository.findById(spotId);
        if (existingSpotOpt.isPresent()) {
            ParkingSpot existingSpot = existingSpotOpt.get();
            existingSpot.setLocation(updatedSpot.getLocation());
            existingSpot.setSpotNumber(updatedSpot.getSpotNumber());
            existingSpot.setLatitude(updatedSpot.getLatitude());
            existingSpot.setLongitude(updatedSpot.getLongitude());
            return parkingSpotRepository.save(existingSpot);
        }
        return null; // Return null or throw an exception if the spot is not found
    }

    // Delete a parking spot
    public void deleteParkingSpot(Long spotId) {
        parkingSpotRepository.deleteById(spotId);
    }

    // Get all available parking spots
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Assign a booking to a user for a parking spot
    public Booking assignBookingToUser(Booking bookingRequest, User user) {
        Optional<ParkingSpot> spotOpt = parkingSpotRepository.findById(bookingRequest.getParkingSpot().getId());
        if (spotOpt.isPresent()) {
            ParkingSpot spot = spotOpt.get();
            if (!spot.isBooked()) {
                Booking booking = new Booking();
                booking.setParkingSpot(spot);
                booking.setUser(user);
                booking.setBookingStartTime(bookingRequest.getBookingStartTime());
                booking.setBookingEndTime(bookingRequest.getBookingEndTime());
                booking.setBookingDate(bookingRequest.getBookingDate());

                // Update parking spot
                spot.setBooked(true);
                spot.setBookedBy(user);
                parkingSpotRepository.save(spot);

                return bookingRepository.save(booking);
            }
        }
        return null; // Spot unavailable or not found
    

    }
    public ParkingSpot findById(Long id) {
        return parkingSpotRepository.findById(id).orElse(null);
    }
}
