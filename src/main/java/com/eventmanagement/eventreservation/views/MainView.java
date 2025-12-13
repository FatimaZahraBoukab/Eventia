package com.eventmanagement.eventreservation.views;

import com.eventmanagement.eventreservation.service.ContactMessageService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.eventmanagement.eventreservation.views.components.PublicHeader;
import com.eventmanagement.eventreservation.views.components.HeroSection;
import com.eventmanagement.eventreservation.views.components.AboutSection;
import com.eventmanagement.eventreservation.views.components.ServicesSection;
import com.eventmanagement.eventreservation.views.components.AvisSection;
import com.eventmanagement.eventreservation.views.components.ContactSection;
import com.eventmanagement.eventreservation.views.components.Footer;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class MainView extends Div {
    
    @Autowired
    private ContactMessageService contactMessageService;
    
    public MainView(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
        
        // Configuration de la page principale
        setSizeFull();
        getStyle()
            .set("margin", "0")
            .set("padding", "0")
            .set("overflow-x", "hidden");

        // Header fixe
        PublicHeader header = new PublicHeader();

        // Container pour tout le contenu
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("padding-top", "70px")  // Espace pour le header fixe
            .set("width", "100%")
            .set("margin", "0")
            .set("padding-left", "0")
            .set("padding-right", "0");

        // Sections
        HeroSection hero = new HeroSection();
        AboutSection about = new AboutSection();
        ServicesSection services = new ServicesSection();
        AvisSection avis = new AvisSection();
        ContactSection contact = new ContactSection(contactMessageService); // Injection du service
        Footer footer = new Footer();

        mainContent.add(hero, about, services, avis, contact, footer);
        
        add(header, mainContent);
    }
}