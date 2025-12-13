package com.eventmanagement.eventreservation.service;

import com.eventmanagement.eventreservation.entity.ContactMessage;
import com.eventmanagement.eventreservation.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactMessageService {
    
    @Autowired
    private ContactMessageRepository contactMessageRepository;
    
    // Enregistrer un nouveau message
    public ContactMessage saveMessage(ContactMessage message) {
        return contactMessageRepository.save(message);
    }
    
    // Récupérer tous les messages
    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderBySentAtDesc();
    }
    
    // Récupérer les messages non lus
    public List<ContactMessage> getUnreadMessages() {
        return contactMessageRepository.findByIsReadFalseOrderBySentAtDesc();
    }
    
    // Récupérer les messages lus
    public List<ContactMessage> getReadMessages() {
        return contactMessageRepository.findByIsReadTrueOrderBySentAtDesc();
    }
    
    // Récupérer les messages importants
    public List<ContactMessage> getImportantMessages() {
        return contactMessageRepository.findByIsImportantTrueOrderBySentAtDesc();
    }
    
    // Compter les messages non lus
    public long countUnreadMessages() {
        return contactMessageRepository.countByIsReadFalse();
    }
    
    // Rechercher des messages
    public List<ContactMessage> searchMessages(String searchTerm) {
        return contactMessageRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSubjectContainingIgnoreCaseOrderBySentAtDesc(
            searchTerm, searchTerm, searchTerm);
    }
    
    // Récupérer un message par ID
    public Optional<ContactMessage> getMessageById(Long id) {
        return contactMessageRepository.findById(id);
    }
    
    // Marquer un message comme lu
    public void markAsRead(Long id) {
        Optional<ContactMessage> messageOpt = contactMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            ContactMessage message = messageOpt.get();
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
            contactMessageRepository.save(message);
        }
    }
    
    // Marquer un message comme non lu
    public void markAsUnread(Long id) {
        Optional<ContactMessage> messageOpt = contactMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            ContactMessage message = messageOpt.get();
            message.setRead(false);
            message.setReadAt(null);
            contactMessageRepository.save(message);
        }
    }
    
    // Marquer/démarquer comme important
    public void toggleImportant(Long id) {
        Optional<ContactMessage> messageOpt = contactMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            ContactMessage message = messageOpt.get();
            message.setImportant(!message.isImportant());
            contactMessageRepository.save(message);
        }
    }
    
    // Supprimer un message
    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }
    
    // Supprimer plusieurs messages
    public void deleteMessages(List<Long> ids) {
        ids.forEach(id -> contactMessageRepository.deleteById(id));
    }
}