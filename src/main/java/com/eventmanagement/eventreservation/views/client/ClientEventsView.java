package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.service.EventService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("client/events")
@RolesAllowed("CLIENT")
public class ClientEventsView extends Div {
    
    private final EventService eventService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public ClientEventsView(EventService eventService) {
        this.eventService = eventService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        ClientSidebar sidebar = new ClientSidebar("client/events");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
    
        
        Div contentContainer = new Div();
        contentContainer.getStyle()
            .set("padding", "40px")
            .set("max-width", "1400px")
            .set("margin", "0 auto");
        
        H2 title = new H2("Événements disponibles");
        title.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0 0 40px 0")
            .set("font-size", "32px")
            .set("font-weight", "700");
        
        List<Event> events = eventService.findPublishedEvents();
        
        if (events.isEmpty()) {
            Div emptyState = createEmptyState();
            contentContainer.add(title, emptyState);
        } else {
            FlexLayout eventsGrid = createEventsGrid(events);
            contentContainer.add(title, eventsGrid);
        }
        
        mainContent.add( contentContainer);
        add(sidebar, mainContent);
    }
    
    
    private FlexLayout createEventsGrid(List<Event> events) {
        FlexLayout grid = new FlexLayout();
        grid.getStyle()
            .set("display", "flex")
            .set("flex-wrap", "wrap")
            .set("gap", "30px")
            .set("justify-content", "flex-start");
        
        for (Event event : events) {
            Div card = createEventCard(event);
            grid.add(card);
        }
        
        return grid;
    }
    
    private Div createEventCard(Event event) {
        Div card = new Div();
        card.getStyle()
            .set("width", "380px")
            .set("background", "#ffffff")
            .set("border-radius", "16px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("transition", "all 0.3s ease")
            .set("cursor", "pointer")
            .set("border", "1px solid #e8e8e8");
        
        Div imageContainer = new Div();
        imageContainer.getStyle()
            .set("width", "100%")
            .set("height", "240px")
            .set("overflow", "hidden")
            .set("position", "relative")
            .set("background", "linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 100%)");
        
        if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
            Image eventImage = new Image(event.getImagePath(), event.getTitre());
            eventImage.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("object-fit", "cover")
                .set("transition", "transform 0.3s ease");
            imageContainer.add(eventImage);
            
            card.getElement().addEventListener("mouseenter", e -> {
                eventImage.getStyle().set("transform", "scale(1.05)");
            });
            
            card.getElement().addEventListener("mouseleave", e -> {
                eventImage.getStyle().set("transform", "scale(1)");
            });
        } else {
            Div placeholder = new Div();
            placeholder.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "64px")
                .set("opacity", "0.3");
            placeholder.add(new Span("🎉"));
            imageContainer.add(placeholder);
        }
        
        Div content = new Div();
        content.getStyle()
            .set("padding", "25px");
        
        H3 eventTitle = new H3(event.getTitre());
        eventTitle.getStyle()
            .set("margin", "0 0 15px 0")
            .set("color", "#2C3E50")
            .set("font-size", "20px")
            .set("font-weight", "700")
            .set("line-height", "1.3");
        
        Div quickInfo = new Div();
        quickInfo.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "12px")
            .set("margin-bottom", "20px");
        
        Div dateInfo = createInfoRow(VaadinIcon.CALENDAR, 
            event.getDateDebut().format(DATE_FORMATTER) + " à " + event.getDateDebut().format(TIME_FORMATTER));
        
        Div locationInfo = createInfoRow(VaadinIcon.MAP_MARKER, 
            event.getLieu() + ", " + event.getVille());
        
        Div priceInfo = createInfoRow(VaadinIcon.MONEY, 
            event.getPrixUnitaire() == 0 ? "Gratuit" : String.format("%.2f DH", event.getPrixUnitaire()));
        
        quickInfo.add(dateInfo, locationInfo, priceInfo);
        
        Button detailsButton = new Button("Voir les détails");
        detailsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        detailsButton.setWidthFull();
        detailsButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
            .set("border", "none")
            .set("border-radius", "10px")
            .set("padding", "12px")
            .set("font-weight", "600")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease");
        
        detailsButton.addClickListener(e -> showEventDetails(event));
        
        content.add(eventTitle, quickInfo, detailsButton);
        card.add(imageContainer, content);
        
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
    
    private Div createInfoRow(VaadinIcon iconType, String text) {
        Div row = new Div();
        row.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "10px");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("color", "#C8A050")
            .set("width", "18px")
            .set("height", "18px")
            .set("flex-shrink", "0");
        
        Span textSpan = new Span(text);
        textSpan.getStyle()
            .set("font-size", "14px")
            .set("color", "#555")
            .set("line-height", "1.4");
        
        row.add(icon, textSpan);
        return row;
    }
    
    private void showEventDetails(Event event) {
        Dialog dialog = new Dialog();
        dialog.setWidth("900px");
        dialog.setMaxHeight("95vh");
        dialog.getElement().getStyle()
            .set("border-radius", "20px")
            .set("overflow", "hidden");
        
        // Container principal avec scroll
        Div mainContainer = new Div();
        mainContainer.getStyle()
            .set("max-height", "90vh")
            .set("overflow-y", "auto")
            .set("background", "#ffffff");
        
        // Header moderne avec image de fond
        Div dialogHeader = new Div();
        dialogHeader.getStyle()
            .set("position", "relative")
            .set("height", "320px")
            .set("overflow", "hidden");
        
        // Image de fond avec overlay
        if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
            Div imageBackdrop = new Div();
            imageBackdrop.getStyle()
                .set("position", "absolute")
                .set("top", "0")
                .set("left", "0")
                .set("width", "100%")
                .set("height", "100%")
                .set("background-image", "url('" + event.getImagePath() + "')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("filter", "brightness(0.6) blur(2px)")
                .set("transform", "scale(1.1)");
            dialogHeader.add(imageBackdrop);
        }
        
        // Overlay gradient
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "100%")
            .set("height", "100%");
         
        dialogHeader.add(overlay);
        
        // Bouton fermer
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        closeButton.getStyle()
            .set("position", "absolute")
            .set("top", "20px")
            .set("right", "20px")
            .set("color", "#ffffff")
            .set("background", "rgba(0,0,0,0.3)")
            .set("border-radius", "50%")
            .set("width", "45px")
            .set("height", "45px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("z-index", "10");
        closeButton.addClickListener(e -> dialog.close());
        dialogHeader.add(closeButton);
        
        // Contenu du header
        Div headerContent = new Div();
        headerContent.getStyle()
            .set("position", "absolute")
            .set("bottom", "30px")
            .set("left", "40px")
            .set("right", "40px")
            .set("z-index", "5");
        
        // Badge catégorie
        Span categoryBadge = new Span(event.getCategorie().getDisplayName());
        categoryBadge.getStyle()
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("color", "#C8A050")
            .set("padding", "8px 20px")
            .set("border-radius", "25px")
            .set("font-size", "12px")
            .set("font-weight", "700")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "1px")
            .set("display", "inline-block")
            .set("margin-bottom", "15px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.1)");
        
        // Titre
        H1 dialogTitle = new H1(event.getTitre());
        dialogTitle.getStyle()
            .set("margin", "0")
            .set("color", "#ffffff")
            .set("font-size", "36px")
            .set("font-weight", "800")
            .set("line-height", "1.2")
            .set("text-shadow", "0 2px 20px rgba(0,0,0,0.3)");
        
        headerContent.add(categoryBadge, dialogTitle);
        dialogHeader.add(headerContent);
        
        // Contenu principal
        Div dialogContent = new Div();
        dialogContent.getStyle()
            .set("padding", "40px")
            .set("background", "#ffffff");
        
        // Section informations clés - Design en cards horizontales
        Div infoSection = new Div();
        infoSection.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(3, 1fr)")
            .set("gap", "20px")
            .set("margin-bottom", "35px");
        
        infoSection.add(
            createModernInfoCard(VaadinIcon.CALENDAR, "Date", event.getDateDebut().format(DATE_FORMATTER)),
            createModernInfoCard(VaadinIcon.CLOCK, "Heure", event.getDateDebut().format(TIME_FORMATTER)),
            createModernInfoCard(VaadinIcon.USERS, "Capacité", event.getCapaciteMax() + " places")
        );
        
        // Deuxième ligne
        Div infoSection2 = new Div();
        infoSection2.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(3, 1fr)")
            .set("gap", "20px")
            .set("margin-bottom", "35px");
        
        infoSection2.add(
            createModernInfoCard(VaadinIcon.MAP_MARKER, "Lieu", event.getLieu()),
            createModernInfoCard(VaadinIcon.LOCATION_ARROW, "Ville", event.getVille()),
            createModernInfoCard(VaadinIcon.MONEY, "Prix", 
                event.getPrixUnitaire() == 0 ? "Gratuit" : String.format("%.2f DH", event.getPrixUnitaire()))
        );
        
        dialogContent.add(infoSection, infoSection2);
        
        // Ligne séparatrice élégante
        Div separator = new Div();
        separator.getStyle()
            .set("height", "1px")
            .set("background", "linear-gradient(90deg, transparent 0%, #E0E0E0 50%, transparent 100%)")
            .set("margin", "35px 0");
        dialogContent.add(separator);
        
        // Description avec design moderne
        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
            Div descriptionSection = new Div();
            descriptionSection.getStyle()
                .set("margin-bottom", "35px");
            
            Div descHeader = new Div();
            descHeader.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "12px")
                .set("margin-bottom", "20px");
            
            Div iconCircle = new Div();
            iconCircle.getStyle()
                .set("width", "40px")
                .set("height", "40px")
                .set("border-radius", "50%")
                .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("box-shadow", "0 4px 15px rgba(200, 160, 80, 0.25)");
            
            Icon descIcon = VaadinIcon.CLIPBOARD_TEXT.create();
            descIcon.getStyle()
                .set("color", "#ffffff")
                .set("width", "20px")
                .set("height", "20px");
            iconCircle.add(descIcon);
            
            H3 descTitle = new H3("À propos de cet événement");
            descTitle.getStyle()
                .set("margin", "0")
                .set("color", "#2C3E50")
                .set("font-size", "22px")
                .set("font-weight", "700");
            
            descHeader.add(iconCircle, descTitle);
            
            Paragraph descText = new Paragraph(event.getDescription());
            descText.getStyle()
                .set("margin", "0")
                .set("color", "#555")
                .set("line-height", "1.8")
                .set("font-size", "15px")
                .set("padding", "25px")
                .set("background", "#F8F9FA")
                .set("border-radius", "12px")
                .set("border", "1px solid #E8E8E8");
            
            descriptionSection.add(descHeader, descText);
            dialogContent.add(descriptionSection);
        }
        
        // Call to action pour réserver
        Div ctaSection = new Div();
        ctaSection.getStyle()
            .set("background", "linear-gradient(135deg, #FFF8E7 0%, #FFF4D6 100%)")
            .set("padding", "35px")
            .set("border-radius", "16px")
            .set("border", "2px solid #C8A050")
            .set("text-align", "center")
            .set("box-shadow", "0 4px 20px rgba(200, 160, 80, 0.15)");
        
        Icon ticketIcon = VaadinIcon.TICKET.create();
        ticketIcon.getStyle()
            .set("color", "#C8A050")
            .set("width", "36px")
            .set("height", "36px")
            .set("margin-bottom", "15px");
        
        H4 ctaTitle = new H4("Réservez votre place maintenant");
        ctaTitle.getStyle()
            .set("margin", "0 0 10px 0")
            .set("color", "#2C3E50")
            .set("font-size", "22px")
            .set("font-weight", "700");
        
        Paragraph ctaText = new Paragraph("Ne manquez pas cette opportunité ! Les places sont limitées.");
        ctaText.getStyle()
            .set("margin", "0 0 25px 0")
            .set("color", "#666")
            .set("font-size", "15px")
            .set("line-height", "1.6");
        
        Button reserveButton = new Button("Réserver maintenant", new Icon(VaadinIcon.CHECK_CIRCLE));
        reserveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        reserveButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
            .set("border", "none")
            .set("border-radius", "12px")
            .set("padding", "16px 40px")
            .set("font-weight", "700")
            .set("font-size", "17px")
            .set("cursor", "pointer")
            .set("box-shadow", "0 4px 20px rgba(200, 160, 80, 0.3)")
            .set("transition", "all 0.3s ease")
            .set("min-width", "250px");
        
        reserveButton.addClickListener(e -> {
    dialog.close();
    UI.getCurrent().navigate("client/event/reserve/" + event.getId());
});
        
        ctaSection.add(ticketIcon, ctaTitle, ctaText, reserveButton);
        dialogContent.add(ctaSection);
        
        mainContainer.add(dialogHeader, dialogContent);
        dialog.add(mainContainer);
        dialog.open();
    }
    
    private Div createModernInfoCard(VaadinIcon icon, String label, String value) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#ffffff")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("border", "1px solid #E8E8E8")
            .set("transition", "all 0.3s ease")
            .set("position", "relative")
            .set("overflow", "hidden");
        
        // Accent bar
        Div accentBar = new Div();
        accentBar.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "4px")
            .set("height", "100%")
            .set("background", "linear-gradient(180deg, #C8A050 0%, #D4AF6A 100%)");
        card.add(accentBar);
        
        Div content = new Div();
        content.getStyle()
            .set("padding-left", "15px");
        
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "8px");
        
        Icon cardIcon = icon.create();
        cardIcon.getStyle()
            .set("color", "#C8A050")
            .set("width", "20px")
            .set("height", "20px")
            .set("margin-right", "8px");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-size", "11px")
            .set("color", "#999")
            .set("font-weight", "600")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "0.5px");
        
        header.add(cardIcon, labelSpan);
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("display", "block")
            .set("font-size", "16px")
            .set("color", "#2C3E50")
            .set("font-weight", "700")
            .set("line-height", "1.4");
        
        content.add(header, valueSpan);
        card.add(content);
        
        return card;
    }
    
    private Div createEmptyState() {
        Div container = new Div();
        container.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "16px")
            .set("padding", "80px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("text-align", "center");
        
        Span icon = new Span("📅");
        icon.getStyle()
            .set("font-size", "80px")
            .set("display", "block")
            .set("margin-bottom", "20px");
        
        Span message = new Span("Aucun événement disponible pour le moment");
        message.getStyle()
            .set("color", "#666666")
            .set("font-size", "20px")
            .set("display", "block")
            .set("margin-bottom", "10px")
            .set("font-weight", "600");
        
        Span subtitle = new Span("Les nouveaux événements apparaîtront ici dès leur publication");
        subtitle.getStyle()
            .set("color", "#999999")
            .set("font-size", "15px")
            .set("display", "block");
        
        container.add(icon, message, subtitle);
        return container;
    }
}