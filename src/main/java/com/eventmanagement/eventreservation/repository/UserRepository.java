package com.eventmanagement.eventreservation.repository;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository pour la gestion des utilisateurs
 * Fournit les méthodes CRUD de base + recherches personnalisées
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Trouver un utilisateur par son email
     * @param email Email de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifier si un email existe déjà
     * @param email Email à vérifier
     * @return true si l'email existe, false sinon
     */
    boolean existsByEmail(String email);
    
    /**
     * Trouver tous les utilisateurs ayant un rôle spécifique
     * @param role Rôle recherché
     * @return Liste des utilisateurs avec ce rôle
     */
    List<User> findByRole(Role role);
    
    /**
     * Compter le nombre d'utilisateurs par rôle
     * @param role Rôle à compter
     * @return Nombre d'utilisateurs avec ce rôle
     */
    long countByRole(Role role);
    
    /**
     * Trouver les utilisateurs actifs par rôle
     * @param role Rôle recherché
     * @param enabled Statut actif
     * @return Liste des utilisateurs actifs avec ce rôle
     */
    List<User> findByRoleAndEnabled(Role role, Boolean enabled);
}