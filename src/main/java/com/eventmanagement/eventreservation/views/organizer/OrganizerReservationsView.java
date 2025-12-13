package com.eventmanagement.eventreservation.views.organizer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("organizer/reservations")
@RolesAllowed("ORGANIZER")
public class OrganizerReservationsView extends Div {
    
    public OrganizerReservationsView() {
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        OrganizerSidebar sidebar = new OrganizerSidebar("organizer/reservations");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        H2 title = new H2("Réservations");
        title.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 30px 0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        Div emptyState = createEmptyState();
        
        mainContent.add(title, emptyState);
        add(sidebar, mainContent);
    }
    
    private Div createEmptyState() {
        Div container = new Div();
        container.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "60px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("text-align", "center");
        
        Span icon = new Span("🎫");
        icon.getStyle()
            .set("font-size", "64px")
            .set("display", "block")
            .set("margin-bottom", "20px");
        
        Span message = new Span("Aucune réservation pour le moment");
        message.getStyle()
            .set("color", "#666666")
            .set("font-size", "18px")
            .set("display", "block")
            .set("margin-bottom", "10px");
        
        Span subtitle = new Span("Les réservations pour vos événements apparaîtront ici");
        subtitle.getStyle()
            .set("color", "#999999")
            .set("font-size", "14px")
            .set("display", "block");
        
        container.add(icon, message, subtitle);
        return container;
    }
}