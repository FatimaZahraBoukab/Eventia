package com.eventmanagement.eventreservation.entity;

/**
 * Énumération des statuts de réservation
 */
public enum ReservationStatus {
    /**
     * En attente de confirmation par l'organisateur
     */
    EN_ATTENTE("En attente"),
    
    /**
     * Réservation confirmée par l'organisateur
     */
    CONFIRMEE("Confirmée"),
    
    /**
     * Réservation annulée
     */
    ANNULEE("Annulée");
    
    
    private final String displayName;
    
    ReservationStatus(String displayName) {
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