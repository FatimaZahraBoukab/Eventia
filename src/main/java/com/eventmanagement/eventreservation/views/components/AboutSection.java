package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Section À propos de l'application Eventia
 * Affiche la présentation de l'entreprise avec texte et image
 */
public class AboutSection extends Div {
    
    public AboutSection() {
        addClassName("about-section");
        setId("about");
        setWidth("100%");
        
        // Container principal
        HorizontalLayout container = new HorizontalLayout();
        container.setWidthFull();
        container.setSpacing(false);
        container.setPadding(false);
        container.addClassName("about-container");
        
        // Section texte (gauche)
        Div textSection = createTextSection();
        
        // Section image (droite)
        Div imageSection = createImageSection();
        
        container.add(textSection, imageSection);
        add(container);
        
        addStyles();
    }
    
    private Div createTextSection() {
        Div textDiv = new Div();
        textDiv.addClassName("text-section");
        
        // Titre principal
        H2 mainTitle = new H2("Votre partenaire événementiel");
        mainTitle.addClassName("main-title");
        
        // Sous-titre avec style doré
        H3 subtitle = new H3("Expérience fluide & Réservation facile ");
        subtitle.addClassName("subtitle");
        
        // Paragraphe descriptif
        Paragraph description = new Paragraph();
        description.setText(
            "Eventia est une plateforme intelligente dédiée à la réservation d’événements. Elle simplifie l’accès aux conférences, galas, spectacles et rencontres culturelles en quelques clics. Grâce à une expérience fluide et moderne, Eventia connecte le public aux meilleurs événements. Notre mission est de rendre chaque réservation rapide, fiable et agréable, afin de vivre des moments uniques et mémorables en toute simplicité."
        );
        description.addClassName("description");
        
        textDiv.add(mainTitle, subtitle, description);
        return textDiv;
    }
    
    private Div createImageSection() {
        Div imageDiv = new Div();
        imageDiv.addClassName("image-section");
        
        // Image de la table élégante
        Image eventImage = new Image("images/About3.jpg", "Événement élégant");
        eventImage.addClassName("event-image");
        eventImage.setWidth("100%");
        eventImage.setHeight("100%");
        
        imageDiv.add(eventImage);
        return imageDiv;
    }
    
    // ... existing code ...

    private void addStyles() {
        getElement().getStyle()
            .set("background-color", "#f5f5f5")
            .set("padding", "80px 0");
        
        // Style pour le container
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            ".about-container {" +
            "  max-width: 1400px;" +
            "  margin: 0 auto;" +
            "  display: flex;" +
            "  align-items: center;" +
            "  gap: 60px;" +
            "  padding: 0 40px;" +
            "}" +
            ".text-section {" +
            "  flex: 1;" +
            "  padding: 40px 20px;" +
            "  text-align: left;" +
            "}" +
            ".main-title {" +
            "  font-family: 'Playfair Display', 'Georgia', serif;" +
            "  font-size: 3rem;" +
            "  font-weight: 400;" +
            "  color: #2c2c2c;" +
            "  line-height: 1.2;" +
            "  margin: 0 0 30px 0;" +
            "}" +
            ".subtitle {" +
            "  font-family: 'Playfair Display', 'Georgia', serif;" +
            "  font-size: 1.8rem;" +
            "  font-weight: 400;" +
            "  color: #c9a961;" +
            "  margin: 0 0 30px 0;" +
            "  font-style: italic;" +
            "}" +
            ".description {" +
            "  font-family: 'Lato', 'Arial', sans-serif;" +
            "  font-size: 1.05rem;" +
            "  line-height: 1.8;" +
            "  color: #5a5a5a;" +
            "  text-align: justify;" +
            "  margin: 0;" +
            "}" +
            ".image-section {" +
            "  flex: 0.8;" +
            "  height: 380px;" +
            "  width: 450px;" +
            "  overflow: hidden;" +
            "  border-radius: 8px;" +
            "  box-shadow: 0 10px 40px rgba(0,0,0,0.15);" +
            "}" +
            ".event-image {" +
            "  object-fit: cover;" +
            "  display: block;" +
            "}" +
            "@media (max-width: 968px) {" +
            "  .about-container {" +
            "    flex-direction: column;" +
            "    gap: 40px;" +
            "  }" +
            "  .text-section {" +
            "    padding: 20px 30px;" +
            "  }" +
            "  .main-title {" +
            "    font-size: 2.2rem;" +
            "  }" +
            "  .subtitle {" +
            "    font-size: 1.4rem;" +
            "  }" +
            "  .image-section {" +
            "    height: 300px;" +
            "    width: 100%;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}