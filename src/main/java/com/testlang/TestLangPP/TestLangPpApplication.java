package com.testlang.TestLangPP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@SpringBootApplication
public class TestLangPpApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestLangPpApplication.class, args);
	}

    // Simple health check endpoint
    @GetMapping("/")
    public String home() {
        return "TestLang++ Demo Backend is running!";
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        // Simple authentication logic
        String username = credentials.get("username");
        String password = credentials.get("password");

        if ("admin".equals(username) && "1234".equals(password)) {
            response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                    "eyJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0." +
                    "example-signature");
            response.put("username", username);
            response.put("success", true);
            response.put("message", "Login successful");
        } else {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .body(response);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable int id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");
        user.put("role", "USER");
        user.put("active", true);
        user.put("createdAt", "2024-01-15T10:30:00Z");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .body(user);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable int id,
            @RequestBody Map<String, String> updates) {
        Map<String, Object> response = new HashMap<>();
        response.put("updated", true);
        response.put("id", id);
        response.put("previousRole", "USER");
        response.put("newRole", updates.get("role"));
        response.put("email", updates.get("email"));
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "User updated successfully");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .body(response);
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        response.put("deleted", true);
        response.put("id", id);
        response.put("username", "user_" + id);
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "User deleted successfully");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .body(response);
    }

    // Additional endpoints for more comprehensive testing
    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "testlang-demo");
        response.put("timestamp", java.time.Instant.now().toString());

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @PostMapping("/api/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>(request);
        response.put("echo", true);
        response.put("receivedAt", System.currentTimeMillis());
        response.put("server", "TestLangDemo");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    // Endpoint to test header assertions
    @GetMapping("/api/headers")
    public ResponseEntity<Map<String, Object>> testHeaders(
            @RequestHeader Map<String, String> headers) {
        Map<String, Object> response = new HashMap<>();
        response.put("receivedHeaders", headers);
        response.put("serverTime", System.currentTimeMillis());
        response.put("message", "Headers received successfully");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-Custom-Header", "custom-value")
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .header("X-Test-Header", "test-value")
                .body(response);
    }

    // Endpoint to test various status codes
    @GetMapping("/api/status/{code}")
    public ResponseEntity<Map<String, Object>> testStatus(@PathVariable int code) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestedCode", code);
        response.put("message", "Returning requested status code");

        return ResponseEntity.status(code)
                .header("Content-Type", "application/json")
                .header("X-Requested-Status", String.valueOf(code))
                .body(response);
    }

}
