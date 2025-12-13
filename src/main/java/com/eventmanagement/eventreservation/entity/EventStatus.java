package com.eventmanagement.eventreservation.entity;

/**
 * Énumération des statuts d'événements
 */
public enum EventStatus {
    BROUILLON("Brouillon"),
    PUBLIE("Publié"),
    ANNULE("Annulé"),
    TERMINE("Terminé");
    
    private final String displayName;
    
    EventStatus(String displayName) {
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