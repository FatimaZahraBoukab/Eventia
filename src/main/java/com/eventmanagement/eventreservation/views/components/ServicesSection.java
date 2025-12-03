package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;


/**
 * Section Services - Affiche les 3 principaux événements
 */
public class ServicesSection extends Div {
    
    public ServicesSection() {
        addClassName("services-section");
        setId("services");
        setWidth("100%");
        getStyle()
            .set("background-color", "#ffffff")
            .set("padding", "80px 40px");
        
        // Container principal
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1400px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(false);
        container.setAlignItems(Alignment.CENTER);
        
        // En-tête de section
        Div header = createSectionHeader();
        
        // Grille de services avec images
        HorizontalLayout servicesGrid = createServicesGrid();
        
        // Bouton "Découvrir tous nos événements"
        Button viewAllButton = createViewAllButton();
        
        container.add(header, servicesGrid, viewAllButton);
        add(container);
        
        addStyles();
    }
    
    private Div createSectionHeader() {
        Div headerDiv = new Div();
        headerDiv.getStyle()
            .set("text-align", "center")
            .set("margin-bottom", "40px");
        
        Span services = new Span("SERVICES");
        services.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#c9a961")
            .set("letter-spacing", "3px")
            .set("font-weight", "500")
            .set("display", "block")
            .set("margin-bottom", "10px");
        
        H2 title = new H2();
        title.getElement().setProperty("innerHTML", "Offrez-vous le <span style='color: #c9a961;'>Meilleur</span>");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "2.8rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "10px")
            .set("font-weight", "400")
            .set("line-height", "1.2");
        
        Paragraph subtitle = new Paragraph("Des Services Sur Mesure pour Vos Mariages et Événements");
        subtitle.getStyle()
            .set("font-size", "1rem")
            .set("color", "#666")
            .set("margin", "0");
        
        headerDiv.add(services, title, subtitle);
        return headerDiv;
    }
    
    private HorizontalLayout createServicesGrid() {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setWidthFull();
        grid.setSpacing(true);
        grid.getStyle()
            .set("gap", "30px")
            .set("justify-content", "center")
            .set("margin-bottom", "25px")
            .set("max-width", "1200px")
            .set("margin-left", "auto")
            .set("margin-right", "auto")
            .set("padding-right", "60px");
        
        // Service 1 - Mariages
        grid.add(createServiceCard(
            "images/mariage.jpg",
            "Mariages",
            "mariage-details"
        ));
        
        // Service 2 - Conférence & Séminaire
        grid.add(createServiceCard(
            "images/confi.jpg",
            "Conférence & Séminaire",
            "conference-details"
        ));
        
        // Service 3 - Anniversaire
        grid.add(createServiceCard(
            "images/anniv.jpg",
            "Anniversaire",
            "anniversaire-details"
        ));
        
        return grid;
    }
    
    private Div createServiceCard(String imagePath, String title, String route) {
        Div card = new Div();
        card.addClassName("service-card");
        card.getStyle()
            .set("flex", "0 1 auto")
            .set("width", "340px")
            .set("background-color", "#ffffff")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)")
            .set("transition", "all 0.3s ease")
            .set("cursor", "pointer")
            .set("position", "relative");
        
        // Container image
        Div imageContainer = new Div();
        imageContainer.getStyle()
            .set("width", "100%")
            .set("height", "350px")
            .set("overflow", "hidden")
            .set("position", "relative");
        
        // Image
        Image serviceImage = new Image(imagePath, title);
        serviceImage.getStyle()
            .set("width", "100%")
            .set("height", "100%")
            .set("object-fit", "cover")
            .set("transition", "transform 0.3s ease");
        
        imageContainer.add(serviceImage);
        
        // Overlay blanc en bas
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("bottom", "0")
            .set("left", "0")
            .set("right", "0")
            .set("background-color", "rgba(255, 255, 255, 0.95)")
            .set("padding", "25px")
            .set("text-align", "center");
        
        // Titre
        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "1.6rem")
            .set("color", "#c9a961")
            .set("margin", "0")
            .set("font-weight", "400");
        
        overlay.add(cardTitle);
        imageContainer.add(overlay);
        card.add(imageContainer);
        
        // Effets hover
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-10px)")
                .set("box-shadow", "0 10px 40px rgba(0,0,0,0.15)");
            serviceImage.getStyle().set("transform", "scale(1.1)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)");
            serviceImage.getStyle().set("transform", "scale(1)");
        });
        
        // Navigation au clic
        card.addClickListener(e -> {
            UI.getCurrent().navigate(route);
        });
        
        return card;
    }
    
    private Button createViewAllButton() {
        Button button = new Button("Découvrir tous nos événements");
        button.getStyle()
            .set("background-color", "#c9a961")
            .set("color", "#ffffff")
            .set("border", "2px solid #c9a961")
            .set("padding", "14px 40px")
            .set("font-size", "1rem")
            .set("font-weight", "600")
            .set("border-radius", "4px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("margin-top", "10px")
            .set("letter-spacing", "0.5px");
        
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                .set("background-color", "#b8964f")
                .set("color", "#ffffff")
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 5px 15px rgba(201, 169, 97, 0.3)");
        });
        
        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                .set("background-color", "transparent")
                .set("color", "#2c2c2c")
                .set("transform", "translateY(0)")
                .set("box-shadow", "none");
        });
        
        button.addClickListener(e -> {
            UI.getCurrent().navigate("tous-evenements");
        });
        
        return button;
    }
    
    private void addStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "@media (max-width: 1200px) {" +
            "  .service-card {" +
            "    width: 300px !important;" +
            "  }" +
            "}" +
            "@media (max-width: 968px) {" +
            "  .services-section {" +
            "    padding: 60px 20px !important;" +
            "  }" +
            "  .service-card {" +
            "    width: 100% !important;" +
            "    max-width: 400px !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}