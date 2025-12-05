package com.eventmanagement.eventreservation.entity;

/**
 * Énumération des rôles utilisateur dans l'application Eventia
 */
public enum Role {
    /**
     * Client - Utilisateur normal qui peut réserver des événements
     */
    CLIENT("Client"),
    
    /**
     * Organisateur - Peut créer et gérer des événements
     */
    ORGANIZER("Organisateur"),
    
    /**
     * Administrateur - Accès complet à toutes les fonctionnalités
     */
    ADMIN("Administrateur");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}