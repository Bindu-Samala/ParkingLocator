package com.app.parkinglocator.repository;

import com.app.parkinglocator.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId); // Get bookings for a specific user
}
