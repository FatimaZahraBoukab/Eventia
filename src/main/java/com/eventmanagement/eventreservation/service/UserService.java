package com.eventmanagement.eventreservation.service;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.entity.Role;
import com.eventmanagement.eventreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * IMPORTANT: Vérifie maintenant si le compte est actif
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        
        User user = userOpt.get();
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Optional.empty();
        }
        
        // NOUVEAU: Vérifier si le compte est actif
        if (!user.getEnabled()) {
            throw new RuntimeException("Votre compte a été désactivé. Veuillez contacter l'administrateur.");
        }
        
        return Optional.of(user);
    }
    
    /**
     * Mettre à jour les informations d'un utilisateur
     * NOUVEAU: Permet maintenant de modifier le rôle
     */
    @Transactional
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new RuntimeException("Impossible de mettre à jour un utilisateur sans ID");
        }
        
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!existingUser.getEmail().equals(user.getEmail())) {
            Optional<User> userWithEmail = userRepository.findByEmail(user.getEmail());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
                throw new RuntimeException("Cet email est déjà utilisé par un autre compte");
            }
        }
        
        // Mettre à jour les champs
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.getEnabled());
        existingUser.setRole(user.getRole()); // NOUVEAU: Permet le changement de rôle
        
        // Ne pas modifier le mot de passe ici
        // Ne pas modifier la date de création
        
        return userRepository.save(existingUser);
    }
    
    /**
     * NOUVEAU: Changer le rôle d'un utilisateur
     */
    @Transactional
    public User changeUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setRole(newRole);
        return userRepository.save(user);
    }
    
    /**
     * Changer le mot de passe d'un utilisateur
     */
    @Transactional
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si le mot de passe actuel est correct
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        
        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        return true;
    }
    
    /**
     * Trouver un utilisateur par email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Trouver un utilisateur par ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
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
    
    /**
     * Supprimer un utilisateur
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        userRepository.delete(user);
    }
    
    /**
     * Activer/Désactiver un utilisateur
     */
    @Transactional
    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }


    /**
 * Désactiver un utilisateur
 */
public void deactivateUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
    user.setEnabled(false);
    userRepository.save(user);
}

/**
 * Réactiver un utilisateur
 */
public void activateUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
    user.setEnabled(true);
    userRepository.save(user);
}
}