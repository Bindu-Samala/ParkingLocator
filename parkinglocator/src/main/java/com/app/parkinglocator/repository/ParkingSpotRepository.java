package com.app.parkinglocator.repository;

import com.app.parkinglocator.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByIsBookedFalse();  // Get available spots
}
