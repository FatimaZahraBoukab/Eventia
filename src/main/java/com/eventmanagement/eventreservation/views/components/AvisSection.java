package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Section Avis - Témoignages clients avec nouveau design
 */
public class AvisSection extends Div {
    
    public AvisSection() {
        setId("avis");
        addClassName("avis-section");
        setId("avis");
        setWidth("100%");
        getStyle()
            .set("background-color", "#f5f5f5")
            .set("padding", "60px 40px 80px 25px");
        
        // Container principal
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1400px");
        container.getStyle()
            .set("margin", "0 auto")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("align-items", "center");
        container.setPadding(false);
        container.setSpacing(false);
        
        // En-tête
        Div header = createSectionHeader();
        
        // Grille de témoignages
        HorizontalLayout testimonials = createTestimonialsGrid();
        
        container.add(header, testimonials);
        add(container);
        
        addStyles();
    }
    
    private Div createSectionHeader() {
        Div headerDiv = new Div();
        headerDiv.getStyle()
            .set("text-align", "center")
            .set("margin-bottom", "30px")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("align-items", "center");
        
        H2 title = new H2();
        title.getElement().setProperty("innerHTML", "Ce que nos clients <span style='color: #c9a961;'>disent</span>");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "2.8rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "15px")
            .set("font-weight", "400")
            .set("line-height", "1.2")
            .set("text-align", "center");
        
        Paragraph subtitle = new Paragraph("Découvrez l'avis de ceux qui vivent leurs meilleurs événements grâce à Eventia");
        subtitle.getStyle()
            .set("font-size", "1rem")
            .set("color", "#666")
            .set("margin", "0")
            .set("max-width", "700px")
            .set("text-align", "center");
        
        headerDiv.add(title, subtitle);
        return headerDiv;
    }
    
    private HorizontalLayout createTestimonialsGrid() {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setWidthFull();
        grid.setSpacing(true);
        grid.getStyle()
            .set("gap", "25px")
            .set("flex-wrap", "nowrap")
            .set("justify-content", "center")
            .set("align-items", "stretch")
            .set("max-width", "1200px")
            .set("margin", "0 auto")
            .set("padding", "0 60px 0 10px");
        
        // Témoignage 1
        grid.add(createTestimonialCard(
            "Sophia Alaoui",
            "Participante événementielle",
            "Eventia nous permet de réserver rapidement des conférences et spectacles déjà organisés. La plateforme est simple à utiliser et offre un large choix d’événements.",
            5,
            "images/avis1.jpg"
        ));
        
        // Témoignage 2
        grid.add(createTestimonialCard(
            "Hassan Benjelloun",
            "Directeur Marketing",
            "Grâce à Eventia, nous réservons facilement des séminaires et événements professionnels. Les informations sont claires, les réservations instantanées et le suivi très pratique. Un vrai gain de temps .",
            4.5,
            "images/avis2.jpg"
        ));
        
        // Témoignage 3
        grid.add(createTestimonialCard(
            "Karima Aatar",
            "Chef d'entreprise",
            "Eventia est devenue notre solution principale pour réserver des galas et festivals. Le catalogue d’événements est riche et bien organisé . Je recommande vivement !",
            5,
            "images/avis3.jpg"
        ));
        
        return grid;
    }
    
    private Div createTestimonialCard(String name, String position, String testimonial, double stars, String imagePath) {
        Div card = new Div();
        card.addClassName("testimonial-card");
        card.getStyle()
            .set("flex", "1 1 0")
            .set("min-width", "0")
            .set("max-width", "360px")
            .set("background-color", "#ffffff")
            .set("padding", "35px 30px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("transition", "all 0.3s ease");
        
        // Citation (texte du témoignage en italique)
        Paragraph testimonialText = new Paragraph(testimonial);
        testimonialText.getStyle()
            .set("font-size", "1rem")
            .set("color", "#444")
            .set("line-height", "1.7")
            .set("margin-bottom", "30px")
            .set("font-style", "italic");
        
        // Container pour la photo et les infos
        HorizontalLayout clientInfo = new HorizontalLayout();
        clientInfo.setSpacing(true);
        clientInfo.getStyle()
            .set("gap", "15px")
            .set("align-items", "center");
        
        // Photo du client (cercle)
        Image clientPhoto = new Image(imagePath, name);
        clientPhoto.getStyle()
            .set("width", "60px")
            .set("height", "60px")
            .set("border-radius", "50%")
            .set("object-fit", "cover");
        
        // Informations client (nom, position, étoiles)
        Div infoDiv = new Div();
        infoDiv.getStyle().set("flex", "1");
        
        H4 clientName = new H4(name);
        clientName.getStyle()
            .set("font-size", "1.1rem")
            .set("color", "#5b3a8a")
            .set("margin", "0 0 3px 0")
            .set("font-weight", "600");
        
        Span clientPosition = new Span(position);
        clientPosition.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#999")
            .set("display", "block")
            .set("margin-bottom", "8px");
        
        // Étoiles
        Div starsDiv = createStars(stars);
        
        infoDiv.add(clientName, clientPosition, starsDiv);
        clientInfo.add(clientPhoto, infoDiv);
        
        card.add(testimonialText, clientInfo);
        
        // Effet hover
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-5px)")
                .set("box-shadow", "0 8px 30px rgba(0,0,0,0.12)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)");
        });
        
        return card;
    }
    
    private Div createStars(double rating) {
        Div starsDiv = new Div();
        starsDiv.getStyle()
            .set("color", "#fbbf24")
            .set("font-size", "1.1rem")
            .set("display", "flex")
            .set("gap", "2px");
        
        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.5;
        
        // Étoiles pleines
        for (int i = 0; i < fullStars; i++) {
            Span star = new Span("★");
            star.getStyle().set("color", "#fbbf24");
            starsDiv.add(star);
        }
        
        // Demi-étoile
        if (halfStar) {
            Span star = new Span("★");
            star.getStyle()
                .set("color", "#fbbf24")
                .set("opacity", "0.5");
            starsDiv.add(star);
        }
        
        // Étoiles vides
        int totalStars = halfStar ? fullStars + 1 : fullStars;
        for (int i = totalStars; i < 5; i++) {
            Span star = new Span("★");
            star.getStyle()
                .set("color", "#d1d5db");
            starsDiv.add(star);
        }
        
        return starsDiv;
    }
    
    private void addStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "@media (max-width: 1200px) {" +
            "  .avis-section .testimonial-card {" +
            "    flex-wrap: wrap !important;" +
            "  }" +
            "}" +
            "@media (max-width: 968px) {" +
            "  .avis-section {" +
            "    padding: 60px 20px !important;" +
            "  }" +
            "  .avis-section > div > div:last-child {" +
            "    flex-wrap: wrap !important;" +
            "    padding: 0 20px !important;" +
            "  }" +
            "  .testimonial-card {" +
            "    width: 100% !important;" +
            "    max-width: 400px !important;" +
            "    flex: 1 1 100% !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}