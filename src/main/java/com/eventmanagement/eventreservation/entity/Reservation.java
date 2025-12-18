package com.eventmanagement.eventreservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;
    
    @ManyToOne
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;
    
    @Column(nullable = false)
    private Integer nombrePlaces;
    
    @Column(nullable = false)
    private Double montantTotal;
    
    @Column(nullable = false)
    private LocalDateTime dateReservation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus statut;
    
    @Column(unique = true, nullable = false)
    private String codeReservation;
    
    @Column(length = 500)
    private String commentaire;
    
    @PrePersist
    protected void onCreate() {
        dateReservation = LocalDateTime.now();
        if (statut == null) {
            statut = ReservationStatus.EN_ATTENTE;
        }
    }
    
    // Constructeurs
    public Reservation() {}
    
    public Reservation(User utilisateur, Event evenement, Integer nombrePlaces, 
                      Double montantTotal, String codeReservation) {
        this.utilisateur = utilisateur;
        this.evenement = evenement;
        this.nombrePlaces = nombrePlaces;
        this.montantTotal = montantTotal;
        this.codeReservation = codeReservation;
        this.statut = ReservationStatus.EN_ATTENTE;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public Event getEvenement() {
        return evenement;
    }
    
    public void setEvenement(Event evenement) {
        this.evenement = evenement;
    }
    
    public Integer getNombrePlaces() {
        return nombrePlaces;
    }
    
    public void setNombrePlaces(Integer nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public LocalDateTime getDateReservation() {
        return dateReservation;
    }
    
    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
    
    public ReservationStatus getStatut() {
        return statut;
    }
    
    public void setStatut(ReservationStatus statut) {
        this.statut = statut;
    }
    
    public String getCodeReservation() {
        return codeReservation;
    }
    
    public void setCodeReservation(String codeReservation) {
        this.codeReservation = codeReservation;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}