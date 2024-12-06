package com.app.parkinglocator.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    // Many-to-Many relationship with Role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",  // Name of the join table
        joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),  // The foreign key in the user_roles table pointing to the user table
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")  // The foreign key in the user_roles table pointing to the role table
    )
    @JsonManagedReference
    private Set<Role> roles;

    // Returns the authorities (roles) for Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                    .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName())) // Role to authority
                    .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can add logic to check if the account has expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // You can add logic to check if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // You can add logic to check if the credentials have expired
    }

    @Override
    public boolean isEnabled() {
        return true;  // You can add logic to check if the user is enabled
    }

    // Getters and setters

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Other getters and setters
}
