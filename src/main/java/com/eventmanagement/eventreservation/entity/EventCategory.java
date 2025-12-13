package com.eventmanagement.eventreservation.entity;

/**
 * Énumération des catégories d'événements
 */
public enum EventCategory {
    CONCERT("Concert"),
    THEATRE("Théâtre"),
    CONFERENCE("Conférence"),
    SPORT("Sport"),
    AUTRE("Autre");
    
    private final String displayName;
    
    EventCategory(String displayName) {
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