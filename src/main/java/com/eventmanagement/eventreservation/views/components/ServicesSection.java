package com.eventmanagement.eventreservation.views.components;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.service.EventService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicesSection extends Div {

    private final EventService eventService;
    

    public ServicesSection(EventService eventService) {
        this.eventService = eventService;
        
        addClassName("services-section");
        setId("services");
        setWidth("100%");
        getStyle()
            .set("background-color", "#ffffff")
            .set("padding", "80px 40px");

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1400px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(false);
        container.setAlignItems(Alignment.CENTER);

        Div header = createSectionHeader();
        HorizontalLayout servicesGrid = createServicesGrid();
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

        Span services = new Span("ÉVÉNEMENTS");
        services.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#c9a961")
            .set("letter-spacing", "3px")
            .set("font-weight", "500")
            .set("display", "block")
            .set("margin-bottom", "10px");

        H2 title = new H2();
        title.getElement().setProperty("innerHTML",
                "Offrez-vous le <span style='color: #c9a961;'>Meilleur</span>");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "2.8rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "10px")
            .set("font-weight", "400");

        Paragraph subtitle = new Paragraph("Des événements prêts à vivre, à portée de clic");
        subtitle.getStyle()
            .set("font-size", "1rem")
            .set("color", "#666");

        headerDiv.add(services, title, subtitle);
        return headerDiv;
    }

    private HorizontalLayout createServicesGrid() {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setWidthFull();
        grid.getStyle()
            .set("gap", "30px")
            .set("justify-content", "center")
            .set("margin-bottom", "25px")
            .set("max-width", "1200px")
            .set("margin", "0 auto")
            .set("padding-right", "60px");

        // Récupérer tous les événements publiés
        List<Event> publishedEvents = eventService.findPublishedEvents();
        
        if (publishedEvents.isEmpty()) {
            // Si aucun événement, afficher un message
            Div emptyMessage = new Div();
            emptyMessage.getStyle()
                .set("text-align", "center")
                .set("padding", "40px")
                .set("color", "#666");
            emptyMessage.add(new Span("Aucun événement disponible pour le moment"));
            grid.add(emptyMessage);
        } else {
            // Mélanger et prendre 3 événements aléatoires
            List<Event> shuffledEvents = new ArrayList<>(publishedEvents);
            Collections.shuffle(shuffledEvents);
            
            // Limiter à 3 événements ou moins si pas assez
            int count = Math.min(3, shuffledEvents.size());
            
            for (int i = 0; i < count; i++) {
                Event event = shuffledEvents.get(i);
                grid.add(createServiceCard(event));
            }
        }

        return grid;
    }

    private Div createServiceCard(Event event) {
        Div card = new Div();
        card.addClassName("service-card");
        card.getStyle()
            .set("width", "340px")
            .set("background-color", "#ffffff")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)")
            .set("cursor", "pointer")
            .set("position", "relative")
            .set("transition", "all 0.3s ease");

        Div imageContainer = new Div();
        imageContainer.getStyle()
            .set("height", "350px")
            .set("position", "relative")
            .set("overflow", "hidden")
            .set("background", "linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 100%)");

        // Image de l'événement
        if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
            Image serviceImage = new Image(event.getImagePath(), event.getTitre());
            serviceImage.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("object-fit", "cover")
                .set("transition", "transform 0.3s ease");
            imageContainer.add(serviceImage);
            
            card.getElement().addEventListener("mouseenter", e -> {
                serviceImage.getStyle().set("transform", "scale(1.05)");
            });
            
            card.getElement().addEventListener("mouseleave", e -> {
                serviceImage.getStyle().set("transform", "scale(1)");
            });
        } else {
            // Placeholder si pas d'image
            Div placeholder = new Div();
            placeholder.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "80px")
                .set("opacity", "0.3");
            placeholder.add(new Span("🎉"));
            imageContainer.add(placeholder);
        }

        // Badge Ville
        Span cityBadge = new Span(event.getVille());
        cityBadge.getStyle()
            .set("position", "absolute")
            .set("top", "16px")
            .set("left", "16px")
            .set("background", "rgba(255,255,255,0.9)")
            .set("padding", "6px 14px")
            .set("font-size", "0.75rem")
            .set("font-weight", "500")
            .set("border-radius", "20px")
            .set("color", "#2c2c2c")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)");

        // Overlay avec infos
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("bottom", "0")
            .set("left", "0")
            .set("right", "0")
            .set("padding", "25px")
            .set("background", "rgba(255,255,255,0.95)")
            .set("text-align", "center");

        H3 cardTitle = new H3(event.getTitre());
        cardTitle.getStyle()
            .set("font-family", "'Playfair Display', serif")
            .set("font-size", "1.6rem")
            .set("color", "#c9a961")
            .set("margin", "0 0 8px 0");
        
        

        overlay.add(cardTitle);
        imageContainer.add(cityBadge, overlay);
        card.add(imageContainer);

        // Navigation vers tous-evenements au clic
        card.addClickListener(e -> UI.getCurrent().navigate("tous-evenements"));
        
        // Effet hover sur la carte
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-5px)")
                .set("box-shadow", "0 8px 30px rgba(0,0,0,0.15)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)");
        });
        
        return card;
    }

    private Button createViewAllButton() {
        Button button = new Button("Découvrir tous nos événements");
        button.getStyle()
            .set("background-color", "#c9a961")
            .set("color", "#ffffff")
            .set("padding", "14px 40px")
            .set("font-weight", "600")
            .set("border-radius", "25px")
            .set("border", "none")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("box-shadow", "0 4px 15px rgba(201, 169, 97, 0.3)");
        
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 6px 20px rgba(201, 169, 97, 0.4)");
        });
        
        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 15px rgba(201, 169, 97, 0.3)");
        });
        
        button.addClickListener(e -> UI.getCurrent().navigate("tous-evenements"));
        return button;
    }

    private void addStyles() {
        getElement().executeJs(
            "const style=document.createElement('style');" +
            "style.textContent=`@media(max-width:968px){.service-card{width:100%!important;}}`;" +
            "document.head.appendChild(style);"
        );
    }
}