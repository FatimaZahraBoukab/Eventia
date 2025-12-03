package com.eventmanagement.eventreservation.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.eventmanagement.eventreservation.views.components.PublicHeader;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.eventmanagement.eventreservation.views.components.Footer;

@Route("tous-evenements")
public class TousEvenementsView extends Div {
    
    public TousEvenementsView() {
        setSizeFull();
        getStyle()
            .set("margin", "0")
            .set("padding", "0")
            .set("overflow-x", "hidden")
            .set("overflow-y", "auto");
        
        PublicHeader header = new PublicHeader();
        
        VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setPadding(false);
        contentContainer.setMargin(false);
        contentContainer.setSpacing(false);
        contentContainer.setWidthFull();
        contentContainer.getStyle().set("margin-top", "60px");
        
        Div headerSection = createHeaderSection();
        Div eventsGrid = createEventsGrid();
        Footer footer = new Footer();
        
        contentContainer.add(headerSection, eventsGrid, footer);
        add(header, contentContainer);
    }
    
    private Div createHeaderSection() {
        Div header = new Div();
        header.getStyle()
            .set("padding", "80px 40px 60px 40px")
            .set("background-color", "#f5f5f5")
            .set("text-align", "center");
        
        Span services = new Span("NOS ÉVÉNEMENTS");
        services.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#c9a961")
            .set("letter-spacing", "3px")
            .set("font-weight", "500")
            .set("display", "block")
            .set("margin-bottom", "15px");
        
        H1 title = new H1("Tous nos services événementiels");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "3rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "20px")
            .set("font-weight", "400");
        
        Paragraph subtitle = new Paragraph("Découvrez notre gamme complète de services pour tous vos événements");
        subtitle.getStyle()
            .set("font-size", "1.1rem")
            .set("color", "#666")
            .set("margin", "0");
        
        header.add(services, title, subtitle);
        return header;
    }
    
    private Div createEventsGrid() {
        Div gridContainer = new Div();
        gridContainer.getStyle()
            .set("padding", "80px 40px")
            .set("background-color", "#ffffff");
        
        FlexLayout grid = new FlexLayout();
        grid.getStyle()
            .set("max-width", "1400px")
            .set("margin", "0 auto")
            .set("display", "flex")
            .set("flex-wrap", "wrap")
            .set("gap", "30px")
            .set("justify-content", "center");
        
        // 9 événements
        grid.add(createEventCard("images/mariage.jpg", "Mariages", "Célébrations inoubliables", "mariage-details"));
        grid.add(createEventCard("images/conference.jpg", "Conférence & Séminaire", "Événements professionnels", "conference-details"));
        grid.add(createEventCard("images/anniversaire.jpg", "Anniversaire", "Fêtes personnalisées", "anniversaire-details"));
        grid.add(createEventCard("images/bapteme.jpg", "Baptême", "Moments sacrés et précieux", "bapteme-details"));
        grid.add(createEventCard("images/gala.jpg", "Soirée de Gala", "Élégance et prestige", "gala-details"));
        grid.add(createEventCard("images/teambuilding.jpg", "Team Building", "Renforcement d'équipe", "teambuilding-details"));
        grid.add(createEventCard("images/lancement.jpg", "Lancement de Produit", "Événements corporate", "lancement-details"));
        grid.add(createEventCard("images/festival.jpg", "Festival & Concert", "Événements culturels", "festival-details"));
        grid.add(createEventCard("images/exposition.jpg", "Exposition", "Événements artistiques", "exposition-details"));
        
        gridContainer.add(grid);
        return gridContainer;
    }
    
    private Div createEventCard(String imagePath, String title, String description, String route) {
        Div card = new Div();
        card.getStyle()
            .set("width", "380px")
            .set("background-color", "#ffffff")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)")
            .set("transition", "all 0.3s ease")
            .set("cursor", "pointer");
        
        // Image container
        Div imageContainer = new Div();
        imageContainer.getStyle()
            .set("width", "100%")
            .set("height", "300px")
            .set("overflow", "hidden")
            .set("position", "relative");
        
        Image eventImage = new Image(imagePath, title);
        eventImage.getStyle()
            .set("width", "100%")
            .set("height", "100%")
            .set("object-fit", "cover")
            .set("transition", "transform 0.3s ease");
        
        imageContainer.add(eventImage);
        
        // Content
        Div content = new Div();
        content.getStyle()
            .set("padding", "25px")
            .set("text-align", "center")
            .set("background-color", "#ffffff");
        
        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "1.5rem")
            .set("color", "#c9a961")
            .set("margin", "0 0 10px 0")
            .set("font-weight", "400");
        
        Paragraph cardDesc = new Paragraph(description);
        cardDesc.getStyle()
            .set("font-size", "0.95rem")
            .set("color", "#666")
            .set("margin", "0")
            .set("line-height", "1.5");
        
        content.add(cardTitle, cardDesc);
        card.add(imageContainer, content);
        
        // Hover effects
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-10px)")
                .set("box-shadow", "0 10px 40px rgba(0,0,0,0.15)");
            eventImage.getStyle().set("transform", "scale(1.1)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)");
            eventImage.getStyle().set("transform", "scale(1)");
        });
        
        // Navigation
        card.addClickListener(e -> {
            UI.getCurrent().navigate(route);
        });
        
        return card;
    }
}