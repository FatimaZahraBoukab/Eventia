package com.eventmanagement.eventreservation.repository;

import com.eventmanagement.eventreservation.entity.Reservation;
import com.eventmanagement.eventreservation.entity.ReservationStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    /**
     * Trouver toutes les réservations d'un utilisateur
     */
    List<Reservation> findByUtilisateur(User utilisateur);
    
    /**
     * Trouver les réservations d'un utilisateur par statut
     */
    List<Reservation> findByUtilisateurAndStatut(User utilisateur, ReservationStatus statut);
    
    /**
     * Trouver les réservations d'un utilisateur triées par date décroissante
     */
    List<Reservation> findByUtilisateurOrderByDateReservationDesc(User utilisateur);
    
    /**
     * Trouver une réservation par code
     */
    Optional<Reservation> findByCodeReservation(String codeReservation);
    
    /**
     * Trouver toutes les réservations d'un événement
     */
    List<Reservation> findByEvenement(Event evenement);
    
    /**
     * Trouver les réservations d'un événement avec un statut donné
     */
    List<Reservation> findByEvenementAndStatut(Event evenement, ReservationStatus statut);
    
    /**
     * Calculer le nombre total de places réservées pour un événement (statut confirmé ou en attente)
     */
    @Query("SELECT COALESCE(SUM(r.nombrePlaces), 0) FROM Reservation r " +
           "WHERE r.evenement = :evenement AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE')")
    Integer countTotalPlacesReservees(@Param("evenement") Event evenement);
    
    /**
     * Calculer le nombre de places confirmées pour un événement
     */
    @Query("SELECT COALESCE(SUM(r.nombrePlaces), 0) FROM Reservation r " +
           "WHERE r.evenement = :evenement AND r.statut = 'CONFIRMEE'")
    Integer countPlacesConfirmees(@Param("evenement") Event evenement);
    
    /**
     * Trouver les réservations entre deux dates
     */
    @Query("SELECT r FROM Reservation r WHERE r.dateReservation BETWEEN :dateDebut AND :dateFin")
    List<Reservation> findByDateReservationBetween(
        @Param("dateDebut") LocalDateTime dateDebut, 
        @Param("dateFin") LocalDateTime dateFin
    );
    
    /**
     * Trouver les réservations confirmées d'un utilisateur
     */
    List<Reservation> findByUtilisateurAndStatutOrderByDateReservationDesc(
        User utilisateur, 
        ReservationStatus statut
    );
    
    /**
     * Calculer le montant total des réservations d'un utilisateur
     */
    @Query("SELECT COALESCE(SUM(r.montantTotal), 0.0) FROM Reservation r " +
           "WHERE r.utilisateur = :utilisateur AND r.statut = 'CONFIRMEE'")
    Double calculateTotalMontantByUser(@Param("utilisateur") User utilisateur);
    
    /**
     * Compter les réservations par utilisateur
     */
    Long countByUtilisateur(User utilisateur);
    
    /**
     * Compter les réservations par utilisateur et statut
     */
    Long countByUtilisateurAndStatut(User utilisateur, ReservationStatus statut);
    
    /**
     * Vérifier si un code de réservation existe
     */
    boolean existsByCodeReservation(String codeReservation);
    
    /**
     * Trouver les réservations à venir (événements futurs)
     */
    @Query("SELECT r FROM Reservation r " +
           "WHERE r.utilisateur = :utilisateur " +
           "AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE') " +
           "AND r.evenement.dateDebut > :now " +
           "ORDER BY r.evenement.dateDebut ASC")
    List<Reservation> findUpcomingReservations(
        @Param("utilisateur") User utilisateur,
        @Param("now") LocalDateTime now
    );
}