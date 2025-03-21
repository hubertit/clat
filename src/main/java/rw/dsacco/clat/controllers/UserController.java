package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.UserDTO;
import rw.dsacco.clat.dto.LoginDTO;
import rw.dsacco.clat.models.Role;
import rw.dsacco.clat.models.User;
import rw.dsacco.clat.services.UserService;
import rw.dsacco.clat.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // ✅ Register User
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
        user.setPassword(userDTO.getPassword());
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now().toString());

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", savedUser));
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginUser(@RequestBody LoginDTO loginDTO) {
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());

        if (user.isEmpty() || !userService.verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        }

        // ✅ Generate JWT token
        String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getId());

        // ✅ Prepare response data
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.get().getId());
        response.put("phone", user.get().getPhone());
        response.put("email", user.get().getEmail());
        response.put("name", user.get().getName());
        response.put("role", user.get().getRole());
        response.put("status", user.get().getStatus());
        response.put("createdAt", user.get().getCreatedAt());
        response.put("token", token);

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    // ✅ Get All Users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "No users found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Fetched all users", users));
    }

    // ✅ Update User
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {

        Optional<User> existingUserOpt = userService.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found"));
        }

        User existingUser = existingUserOpt.get();
        existingUser.setName(userDTO.getName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));
        existingUser.setStatus(userDTO.getStatus());

        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    // ✅ Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        if (!userService.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
