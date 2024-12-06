package com.app.parkinglocator.repository;
import com.app.parkinglocator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // Find user by username

    Optional<User> findByEmail(String email); // Find user by email (added this method)

}