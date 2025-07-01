package com.AutolavaggioDomicilio.demo.Service;

import com.AutolavaggioDomicilio.demo.Entity.User;
import com.AutolavaggioDomicilio.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.Jwt;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public Jwt getCurrentUser() {
        try {
            return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (ClassCastException e) {
            return null;
        }
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void promoteWasher(User user) {
        user.setRole("WASHER");
        userRepository.save(user);
    }

    public void promoteAdmin(User user) {
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public User revokeRole(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }
}