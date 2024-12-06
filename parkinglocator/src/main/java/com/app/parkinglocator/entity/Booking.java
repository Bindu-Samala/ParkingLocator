package com.app.parkinglocator.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with User (the person who made the booking)
    @ManyToOne
    private User user;

    // Many-to-one relationship with ParkingSpot (the spot that was booked)
    @ManyToOne
    private ParkingSpot parkingSpot;

    private Date bookingDate;  // The date when the booking was made
    private Date bookingStartTime;  // Start time of the booking
    private Date bookingEndTime;  // End time of the booking

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(Date bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public Date getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(Date bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }
}
