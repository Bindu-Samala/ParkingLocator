package com.app.parkinglocator.service;

import com.app.parkinglocator.entity.Role;
import com.app.parkinglocator.entity.User;
import com.app.parkinglocator.repository.RoleRepository;
import com.app.parkinglocator.repository.UserRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method for registering a new user
    public User registerNewUser(User user) {
        // Check if the username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the "USER" role by default
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        userRole.ifPresent(roles::add);
        user.setRoles(roles);

        // Save the user
        return userRepository.save(user);
    }
}
