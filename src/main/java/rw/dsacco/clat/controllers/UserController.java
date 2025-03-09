package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.UserDTO;
import rw.dsacco.clat.dto.LoginDTO;
import rw.dsacco.clat.models.Role;
import rw.dsacco.clat.models.User;
import rw.dsacco.clat.services.UserService;
import rw.dsacco.clat.security.JwtUtil; // ✅ JWT Utility

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // ✅ For @RestController, @RequestMapping, @GetMapping, etc.
import org.springframework.http.HttpMethod; // ✅ Needed for security config (if applicable)

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List; // ✅ Required for List<User> response
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil; // ✅ Ensure JwtUtil is injected

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody UserDTO userDTO) {
        if (userService.existsByEmailOrPhone(userDTO.getEmail(), userDTO.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Email or phone number already exists"));
        }

        User user = new User();
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));
        user.setPassword(userDTO.getPassword()); // 🔐 Raw password (will be encrypted in service)
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now().toString());

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", savedUser));
    }


    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found")));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginUser(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userService.findByEmail(loginDTO.getEmail());

        if (userOptional.isEmpty() || !userService.verifyPassword(loginDTO.getPassword(), userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        }

        User user = userOptional.get();

        // ✅ Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // ✅ Prepare response data
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("phone", user.getPhone());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRole());
        response.put("password", user.getPassword());
        response.put("status", user.getStatus());
        response.put("createdAt", user.getCreatedAt());
        response.put("token", token); // ✅ Add JWT token to response

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }


    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found"));
        }

        User user = userOptional.get();
        user.setPhone(userDTO.getPhone());
        user.setName(userDTO.getName());
        user.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));

        // ✅ Ensure status is never null
        if (userDTO.getStatus() == null || userDTO.getStatus().isEmpty()) {
            user.setStatus("ACTIVE"); // Default to ACTIVE if missing
        } else {
            user.setStatus(userDTO.getStatus());
        }

        System.out.println("🔄 Updating User: " + user);

        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (Exception e) {
            System.out.println("❌ Error Updating User: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found"));
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }


}
