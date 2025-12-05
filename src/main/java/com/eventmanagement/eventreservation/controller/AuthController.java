package com.eventmanagement.eventreservation.controller;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Créer un compte ADMIN via Postman
     * POST /api/auth/create-admin
     */
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody CreateUserRequest request) {
        try {
            User admin = userService.createAdmin(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Administrateur créé avec succès");
            response.put("user", Map.of(
                "id", admin.getId(),
                "fullName", admin.getFullName(),
                "email", admin.getEmail(),
                "role", admin.getRole()
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    /**
     * Créer un compte ORGANISATEUR via Postman
     * POST /api/auth/create-organizer
     */
    @PostMapping("/create-organizer")
    public ResponseEntity<?> createOrganizer(@RequestBody CreateUserRequest request) {
        try {
            User organizer = userService.createOrganizer(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Organisateur créé avec succès");
            response.put("user", Map.of(
                "id", organizer.getId(),
                "fullName", organizer.getFullName(),
                "email", organizer.getEmail(),
                "role", organizer.getRole()
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    /**
     * Tester la connexion via API
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var user = userService.authenticate(request.getEmail(), request.getPassword());
        
        if (user.isPresent()) {
            User u = user.get();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Connexion réussie",
                "user", Map.of(
                    "id", u.getId(),
                    "fullName", u.getFullName(),
                    "email", u.getEmail(),
                    "role", u.getRole()
                )
            ));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("success", false, "message", "Email ou mot de passe incorrect"));
    }
    
    // Classes internes pour les requêtes
    public static class CreateUserRequest {
        private String fullName;
        private String email;
        private String password;
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}