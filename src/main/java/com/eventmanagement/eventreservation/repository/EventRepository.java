package com.eventmanagement.eventreservation.repository;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventStatus;
import com.eventmanagement.eventreservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des événements
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    /**
     * Trouver tous les événements d'un organisateur
     */
    List<Event> findByOrganisateur(User organisateur);
    
    /**
     * Trouver les événements d'un organisateur par statut
     */
    List<Event> findByOrganisateurAndStatut(User organisateur, EventStatus statut);
    
    /**
     * Trouver tous les événements publiés
     */
    List<Event> findByStatut(EventStatus statut);
    
    /**
     * Compter les événements d'un organisateur
     */
    long countByOrganisateur(User organisateur);
    
    /**
     * Compter les événements d'un organisateur par statut
     */
    long countByOrganisateurAndStatut(User organisateur, EventStatus statut);
}