package com.eventmanagement.eventreservation.views.client;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;

public class ClientSidebar extends Div {
    
    public ClientSidebar(String currentRoute) {
        addClassName("client-sidebar");
        setId("clientSidebar");
        getStyle()
            .set("width", "260px")
            .set("min-height", "100vh")
            .set("background", "#FFFFFF")
            .set("padding", "0")
            .set("position", "fixed")
            .set("left", "0")
            .set("top", "0")
            .set("box-shadow", "2px 0 15px rgba(0,0,0,0.05)")
            .set("z-index", "1001")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("border-right", "1px solid #E8E6E3");
        
        // En-tête avec logo
        Div header = new Div();
        header.getStyle()
            .set("padding", "30px 20px 25px 20px")
            .set("border-bottom", "1px solid #F0EDE8");
        
        H3 logo = new H3("Eventia");
        logo.getStyle()
            .set("color", "#C8A050")
            .set("margin", "0")
            .set("font-size", "28px")
            .set("font-weight", "700")
            .set("letter-spacing", "0.5px");
        
        header.add(logo);
        
        // Menu de navigation
        VerticalLayout menu = new VerticalLayout();
        menu.setPadding(false);
        menu.setSpacing(false);
        menu.getStyle()
            .set("padding", "20px 15px")
            .set("gap", "6px")
            .set("flex", "1");
        
        menu.add(
            createMenuItem(VaadinIcon.DASHBOARD, "Tableau de bord", "client/dashboard", currentRoute.equals("client/dashboard")),
            createMenuItem(VaadinIcon.CALENDAR, "Événements", "client/events", currentRoute.equals("client/events")),
            createMenuItem(VaadinIcon.TICKET, "Mes Réservations", "client/reservations", currentRoute.equals("client/reservations")),
            createMenuItem(VaadinIcon.USER, "Mon Profil", "client/profile", currentRoute.equals("client/profile")),
            createMenuItem(VaadinIcon.QUESTION_CIRCLE, "Support", "client/support", currentRoute.equals("client/support"))
        );
        
        // Bouton déconnexion
        Div logoutSection = new Div();
        logoutSection.getStyle()
            .set("padding", "15px 15px 20px 15px")
            .set("border-top", "1px solid #F0EDE8");
        
        Div logoutBtn = createMenuItem(VaadinIcon.SIGN_OUT, "Déconnexion", "logout", false);
        
        logoutSection.add(logoutBtn);
        
        add(header, menu, logoutSection);
    }
    
    private Div createMenuItem(VaadinIcon iconType, String text, String route, boolean active) {
        Div menuItem = new Div();
        
        // Couleur spéciale pour le bouton logout
        boolean isLogout = route.equals("logout");
        String itemColor = isLogout ? "#E74C3C" : (active ? "#C8A050" : "#6B6B6B");
        String itemBg = isLogout ? "rgba(231, 76, 60, 0.08)" : (active ? "rgba(200, 160, 80, 0.12)" : "transparent");
        
        menuItem.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("padding", "13px 16px")
            .set("margin", "2px 0")
            .set("border-radius", "10px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("color", itemColor)
            .set("background", itemBg);
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("margin-right", "12px")
            .set("color", itemColor)
            .set("width", "20px")
            .set("height", "20px");
        
        Span label = new Span(text);
        label.getStyle()
            .set("font-size", "14px")
            .set("font-weight", active ? "600" : "500");
        
        menuItem.add(icon, label);
        
        // Effet hover (sauf pour les éléments actifs et logout)
        if (!active && !isLogout) {
            menuItem.getElement().addEventListener("mouseenter", e -> {
                menuItem.getStyle()
                    .set("background", "rgba(200, 160, 80, 0.08)")
                    .set("color", "#C8A050");
                icon.getStyle().set("color", "#C8A050");
            });
            
            menuItem.getElement().addEventListener("mouseleave", e -> {
                menuItem.getStyle()
                    .set("background", "transparent")
                    .set("color", "#6B6B6B");
                icon.getStyle().set("color", "#6B6B6B");
            });
        } else if (isLogout) {
            // Effet hover spécial pour logout
            menuItem.getElement().addEventListener("mouseenter", e -> {
                menuItem.getStyle().set("background", "rgba(231, 76, 60, 0.15)");
            });
            
            menuItem.getElement().addEventListener("mouseleave", e -> {
                menuItem.getStyle().set("background", "rgba(231, 76, 60, 0.08)");
            });
        }
        
        // Navigation au clic
        menuItem.getElement().addEventListener("click", e -> {
            if (route.equals("logout")) {
                UI.getCurrent().getPage().setLocation("/logout");
            } else {
                UI.getCurrent().navigate(route);
            }
        });
        
        return menuItem;
    }
}