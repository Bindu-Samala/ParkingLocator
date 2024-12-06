package com.app.parkinglocator.config;

import com.app.parkinglocator.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder for hashing passwords
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API-based applications
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login", "/auth/register").permitAll() 
                    .requestMatchers("/user/me").authenticated()// Allow login and registration
                    .requestMatchers("/admin/**").hasRole("ADMIN")  // Allow access to /admin/** for ADMIN role
                    .requestMatchers("/user/**").hasRole("USER")    // Allow access to /user/** for USER role
                    .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .formLogin(formLogin -> 
            formLogin
                .loginProcessingUrl("/login")  // The backend login URL for processing login requests
                .permitAll()  // Allow everyone to access the login page
                .defaultSuccessUrl("/user/me", true)  // Redirect after successful login (to a default URL)
                .failureUrl("/login?error=true")  // Redirect to login with error if authentication fails
        )
        .logout(logout -> 
            logout
                .permitAll()  // Allow logout without authentication
                .logoutUrl("/logout")  // Specify the logout URL (optional, defaults to /logout)
                .logoutSuccessUrl("/login?logout=true")  // Redirect to login page on successful logout
        )
            .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);  // Add CorsFilter before UsernamePasswordAuthenticationFilter

        return http.build();  // Return the built security filter chain
    }

    // CORS configuration to handle requests from Angular frontend
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // Allow credentials (cookies) with requests
        config.addAllowedOrigin("http://localhost:4200");  // Allow Angular frontend (localhost:4200)
        config.addAllowedHeader("*");  // Allow all headers
        config.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Authentication provider for user authentication
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());  // Set the custom UserDetailsService
        provider.setPasswordEncoder(passwordEncoder());  // Set the password encoder
        return provider;
    }

    // Define the custom UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();  // Your custom UserDetailsService implementation
    }
}
