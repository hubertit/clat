package rw.dsacco.clat.services;

import rw.dsacco.clat.models.User;
import rw.dsacco.clat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 🔐 Password encoder

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User saveUser(User user) {
        System.out.println("🔍 Before Saving: " + user);

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure createdAt is not null
        user.setCreatedAt(LocalDateTime.now().toString());

        try {
            User savedUser = userRepository.save(user);
            System.out.println("✅ User Successfully Saved: " + savedUser);
            return savedUser;
        } catch (Exception e) {
            System.out.println("❌ Error Saving User: " + e.getMessage());
            return null;
        }
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean existsByEmailOrPhone(String email, String phone) {
        return userRepository.existsByEmail(email) || userRepository.existsByPhone(phone);
    }

    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        try {
            System.out.println("🔄 Updating User in Database: " + user);
            User updatedUser = userRepository.save(user);
            System.out.println("✅ User Successfully Updated: " + updatedUser);
            return updatedUser;
        } catch (Exception e) {
            System.out.println("❌ Error Updating User: " + e.getMessage());
            e.printStackTrace(); // ✅ Print full error stack trace
            return null;
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
