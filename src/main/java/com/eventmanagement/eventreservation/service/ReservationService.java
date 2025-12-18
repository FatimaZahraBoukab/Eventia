package com.eventmanagement.eventreservation.service;

import com.eventmanagement.eventreservation.entity.*;
import com.eventmanagement.eventreservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private EventService eventService;
    
    /**
     * Créer une nouvelle réservation avec toutes les vérifications
     */
    @Transactional
    public Reservation createReservation(User utilisateur, Event evenement, 
                                        Integer nombrePlaces, String commentaire) {
        // Vérification 1: L'événement existe
        if (evenement == null) {
            throw new RuntimeException("L'événement n'existe pas");
        }
        
        // Vérification 2: L'événement est publié
        if (evenement.getStatut() != EventStatus.PUBLIE) {
            throw new RuntimeException("Cet événement n'est pas disponible pour la réservation");
        }
        
        // Vérification 3: L'événement n'est pas terminé
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventEndDate = evenement.getDateFin() != null ? 
            evenement.getDateFin() : evenement.getDateDebut();
        
        if (eventEndDate.isBefore(now)) {
            throw new RuntimeException("Cet événement est déjà terminé");
        }
        
        // Vérification 4: Nombre de places valide
        if (nombrePlaces == null || nombrePlaces <= 0) {
            throw new RuntimeException("Le nombre de places doit être supérieur à 0");
        }
        
        if (nombrePlaces > 10) {
            throw new RuntimeException("Vous ne pouvez pas réserver plus de 10 places à la fois");
        }
        
        // Vérification 5: Disponibilité des places
        Integer placesDejaReservees = reservationRepository.countTotalPlacesReservees(evenement);
        Integer placesDisponibles = evenement.getCapaciteMax() - placesDejaReservees;
        
        if (nombrePlaces > placesDisponibles) {
            throw new RuntimeException(
                String.format("Seulement %d place(s) disponible(s) pour cet événement", 
                placesDisponibles)
            );
        }
        
        // Calcul du montant total
        Double montantTotal = evenement.getPrixUnitaire() * nombrePlaces;
        
        // Génération du code de réservation unique
        String codeReservation = generateUniqueReservationCode();
        
        // Création de la réservation
        Reservation reservation = new Reservation(
            utilisateur, 
            evenement, 
            nombrePlaces, 
            montantTotal, 
            codeReservation
        );
        reservation.setCommentaire(commentaire);
        reservation.setStatut(ReservationStatus.EN_ATTENTE);
        
        return reservationRepository.save(reservation);
    }
    
    /**
     * Générer un code de réservation unique (format: EVT-XXXXX)
     */
    private String generateUniqueReservationCode() {
        String code;
        Random random = new Random();
        
        do {
            // Génère un nombre aléatoire entre 10000 et 99999
            int number = 10000 + random.nextInt(90000);
            code = "EVT-" + number;
        } while (reservationRepository.existsByCodeReservation(code));
        
        return code;
    }
    
    /**
     * Confirmer une réservation (par l'organisateur ou l'admin)
     */
    @Transactional
    public Reservation confirmerReservation(Long reservationId, User confirmateur) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Vérifier que le confirmateur est l'organisateur de l'événement ou un admin
        Event evenement = reservation.getEvenement();
        boolean estAutorise = evenement.getOrganisateur().getId().equals(confirmateur.getId()) 
            || confirmateur.getRole() == Role.ADMIN;
        
        if (!estAutorise) {
            throw new RuntimeException("Vous n'êtes pas autorisé à confirmer cette réservation");
        }
        
        // Vérifier que la réservation est en attente
        if (reservation.getStatut() != ReservationStatus.EN_ATTENTE) {
            throw new RuntimeException("Seules les réservations en attente peuvent être confirmées");
        }
        
        reservation.setStatut(ReservationStatus.CONFIRMEE);
        return reservationRepository.save(reservation);
    }
    
    /**
     * Refuser une réservation (par l'organisateur ou l'admin)
     */
    @Transactional
    public Reservation refuserReservation(Long reservationId, User refuseur) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Vérifier que le refuseur est l'organisateur de l'événement ou un admin
        Event evenement = reservation.getEvenement();
        boolean estAutorise = evenement.getOrganisateur().getId().equals(refuseur.getId()) 
            || refuseur.getRole() == Role.ADMIN;
        
        if (!estAutorise) {
            throw new RuntimeException("Vous n'êtes pas autorisé à refuser cette réservation");
        }
        
        // Vérifier que la réservation est en attente
        if (reservation.getStatut() != ReservationStatus.EN_ATTENTE) {
            throw new RuntimeException("Seules les réservations en attente peuvent être refusées");
        }
        
    
        return reservationRepository.save(reservation);
    }
    
    /**
     * Annuler une réservation (par l'organisateur ou l'admin uniquement)
     * Les clients ne peuvent plus annuler - seulement demander via organisateur
     */
    @Transactional
    public Reservation annulerReservation(Long reservationId, User demandeur) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // NOUVELLE RÈGLE: Seuls l'organisateur et l'admin peuvent annuler
        boolean estAutorise = reservation.getEvenement().getOrganisateur().getId().equals(demandeur.getId())
            || demandeur.getRole() == Role.ADMIN;
        
        if (!estAutorise) {
            throw new RuntimeException("Seul l'organisateur ou l'administrateur peut annuler cette réservation");
        }
        
        // Vérifier que la réservation n'est pas déjà annulée ou refusée
        if (reservation.getStatut() == ReservationStatus.ANNULEE) {
            throw new RuntimeException("Cette réservation est déjà annulée");
        }
        
    
        
        // Pas de vérification de date - l'organisateur/admin peut annuler même après l'événement
        
        reservation.setStatut(ReservationStatus.ANNULEE);
        return reservationRepository.save(reservation);
    }
    
    /**
     * Supprimer définitivement une réservation
     * Organisateur et Admin peuvent supprimer n'importe quelle réservation
     */
    @Transactional
    public void supprimerReservation(Long reservationId, User demandeur) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Vérifier que c'est l'organisateur ou l'admin
        boolean estAutorise = reservation.getEvenement().getOrganisateur().getId().equals(demandeur.getId())
            || demandeur.getRole() == Role.ADMIN;
        
        if (!estAutorise) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer cette réservation");
        }
        
        // Organisateur et Admin peuvent supprimer n'importe quel statut
        reservationRepository.delete(reservation);
    }
    
    /**
     * Récupérer toutes les réservations d'un utilisateur
     */
    public List<Reservation> findByUtilisateur(User utilisateur) {
        return reservationRepository.findByUtilisateurOrderByDateReservationDesc(utilisateur);
    }
    
    /**
     * Récupérer les réservations d'un utilisateur par statut
     */
    public List<Reservation> findByUtilisateurAndStatut(User utilisateur, ReservationStatus statut) {
        return reservationRepository.findByUtilisateurAndStatutOrderByDateReservationDesc(
            utilisateur, statut
        );
    }
    
    /**
     * Trouver une réservation par son code
     */
    public Optional<Reservation> findByCodeReservation(String codeReservation) {
        return reservationRepository.findByCodeReservation(codeReservation);
    }
    
    /**
     * Trouver une réservation par ID
     */
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    
    /**
     * Récupérer les réservations à venir d'un utilisateur
     */
    public List<Reservation> findUpcomingReservations(User utilisateur) {
        return reservationRepository.findUpcomingReservations(utilisateur, LocalDateTime.now());
    }
    
    /**
     * Récupérer toutes les réservations d'un événement
     */
    public List<Reservation> findByEvenement(Event evenement) {
        return reservationRepository.findByEvenement(evenement);
    }
    
    /**
     * Récupérer les réservations d'un événement par statut
     */
    public List<Reservation> findByEvenementAndStatut(Event evenement, ReservationStatus statut) {
        return reservationRepository.findByEvenementAndStatut(evenement, statut);
    }
    
    /**
     * Vérifier la disponibilité pour un événement
     */
    public Integer getPlacesDisponibles(Event evenement) {
        Integer placesReservees = reservationRepository.countTotalPlacesReservees(evenement);
        return evenement.getCapaciteMax() - placesReservees;
    }
    
    /**
     * Compter les réservations d'un utilisateur
     */
    public long countByUtilisateur(User utilisateur) {
        return reservationRepository.countByUtilisateur(utilisateur);
    }
    
    /**
     * Compter les réservations par statut pour un utilisateur
     */
    public long countByUtilisateurAndStatut(User utilisateur, ReservationStatus statut) {
        return reservationRepository.countByUtilisateurAndStatut(utilisateur, statut);
    }
    
    /**
     * Calculer le montant total dépensé par un utilisateur
     */
    public Double calculateTotalMontantByUser(User utilisateur) {
        return reservationRepository.calculateTotalMontantByUser(utilisateur);
    }
    
    /**
     * Générer un récapitulatif de réservation
     */
    public String generateRecapitulatif(Reservation reservation) {
        StringBuilder recap = new StringBuilder();
        recap.append("=== RÉCAPITULATIF DE RÉSERVATION ===\n\n");
        recap.append("Code de réservation: ").append(reservation.getCodeReservation()).append("\n");
        recap.append("Événement: ").append(reservation.getEvenement().getTitre()).append("\n");
        recap.append("Date de l'événement: ")
             .append(reservation.getEvenement().getDateDebut().toLocalDate()).append("\n");
        recap.append("Lieu: ").append(reservation.getEvenement().getLieu())
             .append(", ").append(reservation.getEvenement().getVille()).append("\n");
        recap.append("Nombre de places: ").append(reservation.getNombrePlaces()).append("\n");
        recap.append("Montant total: ").append(String.format("%.2f DH", reservation.getMontantTotal()))
             .append("\n");
        recap.append("Statut: ").append(reservation.getStatut().getDisplayName()).append("\n");
        
        if (reservation.getCommentaire() != null && !reservation.getCommentaire().isEmpty()) {
            recap.append("Commentaire: ").append(reservation.getCommentaire()).append("\n");
        }
        
        recap.append("\n================================");
        
        return recap.toString();
    }
    
    /**
     * Obtenir toutes les réservations
     */
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}