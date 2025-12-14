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
import java.time.LocalDateTime;
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
        
        // Vérifier automatiquement si l'événement est terminé
        checkAndUpdateEventStatus(event);
        
        return eventRepository.save(event);
    }
    
    /**
     * Vérifier et mettre à jour automatiquement le statut d'un événement
     */
    private void checkAndUpdateEventStatus(Event event) {
        LocalDateTime now = LocalDateTime.now();
        
        // Si l'événement est publié et que la date de fin est passée
        if (event.getStatut() == EventStatus.PUBLIE) {
            LocalDateTime eventEndDate = event.getDateFin() != null ? event.getDateFin() : event.getDateDebut();
            
            if (eventEndDate.isBefore(now)) {
                event.setStatut(EventStatus.TERMINE);
            }
        }
    }
    
    /**
     * Mettre à jour automatiquement tous les événements terminés
     * Cette méthode peut être appelée périodiquement ou lors de l'affichage
     */
    @Transactional
    public void updateExpiredEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> publishedEvents = eventRepository.findByStatut(EventStatus.PUBLIE);
        
        for (Event event : publishedEvents) {
            LocalDateTime eventEndDate = event.getDateFin() != null ? event.getDateFin() : event.getDateDebut();
            
            if (eventEndDate.isBefore(now)) {
                event.setStatut(EventStatus.TERMINE);
                eventRepository.save(event);
            }
        }
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
     * ✅ CORRECTION: Gestion des URLs externes vs fichiers locaux
     */
    public void deleteEventImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                // Vérifier si c'est une URL externe (commence par http:// ou https://)
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // C'est une URL externe, on ne peut pas la supprimer du serveur
                    // (elle est hébergée ailleurs)
                    System.out.println("Image externe (URL), pas de suppression nécessaire: " + imagePath);
                    return;
                }
                
                // C'est un fichier local, on peut le supprimer
                Path filePath = Paths.get(UPLOAD_DIR, imagePath);
                boolean deleted = Files.deleteIfExists(filePath);
                
                if (deleted) {
                    System.out.println("Image locale supprimée avec succès: " + imagePath);
                } else {
                    System.out.println("Image locale introuvable (peut-être déjà supprimée): " + imagePath);
                }
                
            } catch (IOException e) {
                // Log l'erreur mais ne pas faire échouer l'opération de suppression de l'événement
                System.err.println("Erreur lors de la suppression de l'image: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                // Capture toute autre exception (comme InvalidPathException)
                System.err.println("Erreur inattendue lors de la suppression de l'image: " + e.getMessage());
                e.printStackTrace();
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
        // Mettre à jour les événements expirés avant de les afficher
        updateExpiredEvents();
        return eventRepository.findByOrganisateur(organisateur);
    }
    
    /**
     * Trouver les événements d'un organisateur par statut
     */
    public List<Event> findByOrganisateurAndStatut(User organisateur, EventStatus statut) {
        // Mettre à jour les événements expirés avant de les afficher
        updateExpiredEvents();
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
        
        // Supprimer l'image si elle existe (gère maintenant les URLs externes)
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