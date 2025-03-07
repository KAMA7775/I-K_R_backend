package org.example.userservice.service;

import org.example.userservice.entity.Role;
import org.example.userservice.entity.UserEntity;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        UserEntity user = new UserEntity(username, passwordEncoder.encode(password), Set.of(Role.USER));
        return userRepository.save(user);
    }

    public UserEntity registerAdmin(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Admin already exists");
        }
        UserEntity admin = new UserEntity(username, passwordEncoder.encode(password), Set.of(Role.ADMIN));
        return userRepository.save(admin);
    }
}
