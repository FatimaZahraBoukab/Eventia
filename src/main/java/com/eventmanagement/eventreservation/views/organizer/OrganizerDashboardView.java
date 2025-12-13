package com.eventmanagement.eventreservation.views.organizer;

import com.eventmanagement.eventreservation.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

@Route("organizer/dashboard")
@RolesAllowed("ORGANIZER")
public class OrganizerDashboardView extends Div {
    
    public OrganizerDashboardView() {
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        // Sidebar
        OrganizerSidebar sidebar = new OrganizerSidebar("organizer/dashboard");
        
        // Contenu principal
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
        // Header avec profil
        Div header = createOrganizerHeader();
        
        // Container pour le contenu
        Div contentContainer = new Div();
        contentContainer.getStyle().set("padding", "30px");
        
    
        
        // Cartes de statistiques
        HorizontalLayout statsCards = createStatsCards();
        
        contentContainer.add(statsCards);
        mainContent.add(header, contentContainer);
        add(sidebar, mainContent);
    }
    
    private Div createOrganizerHeader() {
        Div header = new Div();
        header.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "20px 30px")
            .set("border-bottom", "1px solid #E8E6E3")
            .set("display", "flex")
            .set("justify-content", "flex-end")
            .set("align-items", "center")
            .set("box-shadow", "0 1px 3px rgba(0,0,0,0.04)");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Organisateur";
        String userInitial = userName.substring(0, 1).toUpperCase();
        
        HorizontalLayout profileSection = new HorizontalLayout();
        profileSection.setSpacing(true);
        profileSection.setAlignItems(HorizontalLayout.Alignment.CENTER);
        profileSection.getStyle()
            .set("cursor", "pointer")
            .set("padding", "8px 15px")
            .set("border-radius", "30px")
            .set("transition", "all 0.3s ease");
        
        // Nom de l'utilisateur
        Span nameSpan = new Span(userName);
        nameSpan.getStyle()
            .set("color", "#2C2C2C")
            .set("font-weight", "500")
            .set("font-size", "14px")
            .set("margin-right", "12px");
        
        // Avatar avec initiale
        Div avatar = new Div();
        avatar.getStyle()
            .set("width", "40px")
            .set("height", "40px")
            .set("border-radius", "50%")
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6E 100%)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("color", "#FFFFFF")
            .set("font-weight", "700")
            .set("font-size", "16px")
            .set("box-shadow", "0 2px 8px rgba(200, 160, 80, 0.25)");
        
        Span initial = new Span(userInitial);
        avatar.add(initial);
        
        profileSection.add(nameSpan, avatar);
        
        // Effet hover
        profileSection.getElement().addEventListener("mouseenter", e -> {
            profileSection.getStyle()
                .set("background", "rgba(200, 160, 80, 0.08)");
        });
        
        profileSection.getElement().addEventListener("mouseleave", e -> {
            profileSection.getStyle()
                .set("background", "transparent");
        });
        
        // Navigation vers le profil au clic
        profileSection.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate("organizer/profile");
        });
        
        header.add(profileSection);
        return header;
    }
    
    
    private HorizontalLayout createStatsCards() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.getStyle()
            .set("gap", "20px")
            .set("flex-wrap", "wrap");
        
        layout.add(
            createStatCard("Événements", "0", VaadinIcon.CALENDAR, "#5B9BD5"),
            createStatCard("Réservations", "0", VaadinIcon.TICKET, "#C8A050"),
            createStatCard("En attente", "0", VaadinIcon.CLOCK, "#ED7D31"),
            createStatCard("Confirmées", "0", VaadinIcon.CHECK_CIRCLE, "#70AD47")
        );
        
        return layout;
    }
    
    private Div createStatCard(String title, String value, VaadinIcon iconType, String color) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "25px")
            .set("border-radius", "14px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("flex", "1")
            .set("min-width", "220px")
            .set("border", "1px solid #F0EDE8")
            .set("transition", "transform 0.3s ease, box-shadow 0.3s ease");
        
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-4px)")
                .set("box-shadow", "0 6px 24px rgba(0,0,0,0.1)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)");
        });
        
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "15px");
        
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
            .set("width", "48px")
            .set("height", "48px")
            .set("border-radius", "12px")
            .set("background", color + "15")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("color", color)
            .set("width", "24px")
            .set("height", "24px");
        
        iconWrapper.add(icon);
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("font-size", "34px")
            .set("font-weight", "700")
            .set("color", "#2C2C2C");
        
        header.add(iconWrapper, valueSpan);
        
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
            .set("color", "#6B6B6B")
            .set("font-size", "14px")
            .set("font-weight", "500")
            .set("display", "block");
        
        card.add(header, titleSpan);
        return card;
    }
}