package com.eventmanagement.eventreservation.service;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.entity.Role;
import com.eventmanagement.eventreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Enregistrer un nouveau client
     */
    public User registerClient(String fullName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.CLIENT);
        user.setEnabled(true);
        
        return userRepository.save(user);
    }
    
    /**
     * Créer un organisateur (via admin ou API)
     */
    public User createOrganizer(String fullName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ORGANIZER);
        user.setEnabled(true);
        
        return userRepository.save(user);
    }
    
    /**
     * Créer un admin (via API)
     */
    public User createAdmin(String fullName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        
        return userRepository.save(user);
    }
    
    /**
     * Authentifier un utilisateur
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        
        return Optional.empty();
    }
    
    /**
     * Trouver un utilisateur par email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Trouver tous les utilisateurs par rôle
     */
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Compter les utilisateurs par rôle
     */
    public long countByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    /**
     * Obtenir tous les utilisateurs
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }
}