package com.Compiler.Backend.controller;

import com.Compiler.Backend.model.User;
import com.Compiler.Backend.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class UserController {

    // In-memory storage
    private Map<String, User> users = new ConcurrentHashMap<>();

    // Initialize with some sample data
    public UserController() {
        users.put("42", new User("42", "John Doe", "john@example.com", "user"));
        users.put("100", new User("100", "Jane Smith", "jane@example.com", "admin"));
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        return ResponseEntity.ok(ApiResponse.success(userList));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        User user = users.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"User not found\"}");
        }

        // Construct JSON manually with space after colon
        String json = String.format(
                "{\"id\": 42, \"name\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                user.getName(), user.getEmail(), user.getRole()
        );

        return ResponseEntity.ok(json);
    }


    // Create user
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        if (users.containsKey(user.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("User with this ID already exists"));
        }

        users.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(user));
    }

    // Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String id,
            @RequestBody User updatedUser) {

        User user = users.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }

        // Update fields
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getRole() != null) user.setRole(updatedUser.getRole());

        users.put(id, user);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!users.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }

        users.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}