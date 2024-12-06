package com.app.parkinglocator.service;

import com.app.parkinglocator.entity.Role;
import com.app.parkinglocator.entity.User;
import com.app.parkinglocator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load the user from the database using the username
    	System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert the roles into GrantedAuthorities
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        System.out.println(user.getUsername());
        for (Role role : user.getRoles()) {
        	System.out.println("My role is"+role.getName());
            authorities.add(new SimpleGrantedAuthority(role.getName()));  // ROLE_ADMIN, ROLE_USER, etc.
        }

        // Return a User object that Spring Security can use, including username, password, and roles
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),  // Username
                user.getPassword(),  // Encoded password
                authorities          // Granted authorities (roles)
        );
    }
}
