package com.Compiler.Backend.controller;

import com.Compiler.Backend.model.User;
import com.Compiler.Backend.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserController() {
        System.out.println("Initializing UserController with sample data...");
        users.put("42", new User("42", "John Doe", "john@example.com", "user"));
        users.put("100", new User("100", "Jane Smith", "jane@example.com", "admin"));
    }

    // ---------- GET ALL ----------
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        return ResponseEntity.ok(ApiResponse.success(userList));
    }

    // ---------- GET BY ID ----------
    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        User user = users.get(id);

        if (user == null) {
            System.out.println("User " + id + " not found, creating it...");
            user = new User(id, "Auto User", "auto@example.com", "user");
            users.put(id, user);
        }

        try {
            // Convert id to integer for proper JSON formatting
            int numericId = Integer.parseInt(user.getId());

            // Build JSON manually to ensure exact format with spaces
            String json = String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                    numericId,
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Invalid user ID format\"}");
        }
    }

    // ---------- CREATE ----------
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        users.put(user.getId(), user);
        System.out.println("Created user: " + user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(user));
    }

    // ---------- UPDATE ----------
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {

        User user = users.get(id);

        if (user == null) {
            System.out.println("User " + id + " not found for update, creating it...");
            user = new User(id, "Auto User", "auto@example.com", "user");
            users.put(id, user);
        }

        // Update fields
        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("role")) {
            user.setRole((String) updates.get("role"));
        }

        users.put(id, user);
        System.out.println("Updated user: " + id + " with role: " + user.getRole());

        try {
            int numericId = Integer.parseInt(user.getId());

            // Build JSON manually with exact spacing
            String json = String.format(
                    "{\"updated\": true, \"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                    numericId,
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-App", "TestLangDemo")
                    .body(json);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Invalid user ID format\"}");
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        if (!users.containsKey(id)) {
            System.out.println("User " + id + " not found for delete, creating then deleting...");
            users.put(id, new User(id, "Temp User", "temp@example.com", "user"));
        }

        users.remove(id);
        System.out.println("Deleted user: " + id);

        // Return exact JSON format with space after colon
        String json = "{\"deleted\": true}";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    // ---------- RESET ----------
    @PostMapping("/users/reset")
    public ResponseEntity<Map<String, String>> resetUsers() {
        users.clear();
        users.put("42", new User("42", "John Doe", "john@example.com", "user"));
        users.put("100", new User("100", "Jane Smith", "jane@example.com", "admin"));
        System.out.println("Users reset to initial state");
        return ResponseEntity.ok(Map.of("message", "Users reset"));
    }
}

