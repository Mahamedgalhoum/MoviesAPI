package com.movieapp.movie_backend.service;

import com.movieapp.movie_backend.model.User;
import com.movieapp.movie_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public User saveUser(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }

        public Optional<User> findByEmail(String email) {
            return userRepository.findByEmail(email);
        }
    }

