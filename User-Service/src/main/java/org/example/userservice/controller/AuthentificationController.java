package org.example.userservice.controller;

import org.example.userservice.entity.UserEntity;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final UserService userService;

    public AuthentificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.registerUser(username, password));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserEntity> registerAdmin(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.registerAdmin(username, password));
    }
}
