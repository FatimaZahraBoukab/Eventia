package com.eventmanagement.eventreservation.service;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    private static final String UPLOAD_DIR = "uploads/events";
    
    public EventService() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le répertoire d'upload", e);
        }
    }
    
    /**
     * Créer un nouvel événement
     */
    @Transactional
    public Event createEvent(Event event) {
        if (event.getOrganisateur() == null) {
            throw new RuntimeException("L'organisateur est obligatoire");
        }
        
        // Validation des dates
        if (event.getDateFin() != null && event.getDateFin().isBefore(event.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }
        
        return eventRepository.save(event);
    }
    
    /**
     * Mettre à jour un événement
     */
    @Transactional
    public Event updateEvent(Event event) {
        if (event.getId() == null) {
            throw new RuntimeException("Impossible de mettre à jour un événement sans ID");
        }
        
        Event existingEvent = eventRepository.findById(event.getId())
            .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        // Vérifier que l'organisateur ne change pas
        if (!existingEvent.getOrganisateur().getId().equals(event.getOrganisateur().getId())) {
            throw new RuntimeException("Vous ne pouvez pas modifier l'organisateur d'un événement");
        }
        
        // Validation des dates
        if (event.getDateFin() != null && event.getDateFin().isBefore(event.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }
        
        return eventRepository.save(event);
    }
    
    /**
     * Sauvegarder une image d'événement
     */
    public String saveEventImage(String fileName, InputStream inputStream) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);
        
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return uniqueFileName;
    }
    
    /**
     * Supprimer une image d'événement
     */
    public void deleteEventImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR, imagePath);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log l'erreur mais ne pas faire échouer l'opération
                System.err.println("Erreur lors de la suppression de l'image: " + e.getMessage());
            }
        }
    }
    
    /**
     * Trouver un événement par ID
     */
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }
    
    /**
     * Trouver tous les événements d'un organisateur
     */
    public List<Event> findByOrganisateur(User organisateur) {
        return eventRepository.findByOrganisateur(organisateur);
    }
    
    /**
     * Trouver les événements d'un organisateur par statut
     */
    public List<Event> findByOrganisateurAndStatut(User organisateur, EventStatus statut) {
        return eventRepository.findByOrganisateurAndStatut(organisateur, statut);
    }
    
    /**
     * Trouver tous les événements publiés
     */
    public List<Event> findPublishedEvents() {
        return eventRepository.findByStatut(EventStatus.PUBLIE);
    }
    
    /**
     * Publier un événement
     */
    @Transactional
    public Event publierEvent(Long eventId, User organisateur) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        // Vérifier que c'est bien l'organisateur de l'événement
        if (!event.getOrganisateur().getId().equals(organisateur.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à publier cet événement");
        }
        
        event.setStatut(EventStatus.PUBLIE);
        return eventRepository.save(event);
    }
    
    /**
     * Annuler un événement
     */
    @Transactional
    public Event annulerEvent(Long eventId, User organisateur) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        if (!event.getOrganisateur().getId().equals(organisateur.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à annuler cet événement");
        }
        
        event.setStatut(EventStatus.ANNULE);
        return eventRepository.save(event);
    }
    
    /**
     * Supprimer un événement
     */
    @Transactional
    public void deleteEvent(Long eventId, User organisateur) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        if (!event.getOrganisateur().getId().equals(organisateur.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer cet événement");
        }
        
        // Supprimer l'image si elle existe
        deleteEventImage(event.getImagePath());
        
        eventRepository.delete(event);
    }
    
    /**
     * Compter les événements d'un organisateur
     */
    public long countByOrganisateur(User organisateur) {
        return eventRepository.countByOrganisateur(organisateur);
    }
    
    /**
     * Obtenir tous les événements
     */
    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}