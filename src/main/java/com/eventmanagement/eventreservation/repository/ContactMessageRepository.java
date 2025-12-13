package com.eventmanagement.eventreservation.repository;

import com.eventmanagement.eventreservation.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    
    // Trouver tous les messages non lus
    List<ContactMessage> findByIsReadFalseOrderBySentAtDesc();
    
    // Trouver tous les messages lus
    List<ContactMessage> findByIsReadTrueOrderBySentAtDesc();
    
    // Trouver tous les messages importants
    List<ContactMessage> findByIsImportantTrueOrderBySentAtDesc();
    
    // Trouver tous les messages triés par date
    List<ContactMessage> findAllByOrderBySentAtDesc();
    
    // Compter les messages non lus
    long countByIsReadFalse();
    
    // Rechercher dans les messages
    List<ContactMessage> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSubjectContainingIgnoreCaseOrderBySentAtDesc(
        String name, String email, String subject);
}