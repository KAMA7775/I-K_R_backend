package org.example.userservice.controller;

import org.example.userservice.dto.AuthentificationRequest;
import org.example.userservice.entity.UserEntity;
import org.example.userservice.service.AuthentificationService;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final UserService userService;
    private final AuthentificationService authentificationService;

    public AuthentificationController(UserService userService, AuthentificationService authentificationService) {
        this.userService = userService;
        this.authentificationService = authentificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody AuthentificationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserEntity> registerAdmin(@RequestBody AuthentificationRequest request) {
        return ResponseEntity.ok(userService.registerAdmin(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthentificationRequest request) {
        return ResponseEntity.ok(authentificationService.authenticate(request));
    }}