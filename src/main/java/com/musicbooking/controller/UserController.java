package com.musicbooking.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musicbooking.dto.UserDTO;
import com.musicbooking.model.ERole;
import com.musicbooking.model.Role;
import com.musicbooking.model.User;
import com.musicbooking.repository.RoleRepository;
import com.musicbooking.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody User user) {
        // Set admin role
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        user.setRoles(Collections.singleton(adminRole));
        // Do NOT encode password here, let the service handle it
        UserDTO createdAdmin = userService.createUser(user);
        return ResponseEntity.ok(createdAdmin);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}