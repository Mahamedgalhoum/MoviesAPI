package com.movieapp.movie_backend.controller;
import com.movieapp.movie_backend.model.User;
import com.movieapp.movie_backend.security.JwtUtil;
import com.movieapp.movie_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.get("email"), request.get("password")
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.get("email"));
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
