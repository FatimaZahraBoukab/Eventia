package com.eventmanagement.eventreservation.views.admin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AdminSidebar extends Div {
    
    private Span timeLabel;
    private Span dateLabel;
    
    public AdminSidebar(String currentRoute) {
        addClassName("admin-sidebar");
        getStyle()
            .set("width", "260px")
            .set("min-height", "100vh")
            .set("background", "#FFFFFF")
            .set("padding", "0")
            .set("position", "fixed")
            .set("left", "0")
            .set("top", "0")
            .set("box-shadow", "2px 0 15px rgba(0,0,0,0.05)")
            .set("z-index", "1000")
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
        
        // Panneau date et heure
        Div clockPanel = createClockPanel();
        
        // Menu de navigation
        VerticalLayout menu = new VerticalLayout();
        menu.setPadding(false);
        menu.setSpacing(false);
        menu.getStyle()
            .set("padding", "20px 15px")
            .set("gap", "6px")
            .set("flex", "1");
        
        menu.add(
            createMenuItem(VaadinIcon.DASHBOARD, "Tableau de bord", "admin/dashboard", currentRoute.equals("admin/dashboard")),
            createMenuItem(VaadinIcon.USERS, "Utilisateurs", "admin/users", currentRoute.equals("admin/users")),
            createMenuItem(VaadinIcon.CALENDAR, "Événements", "admin/events", currentRoute.equals("admin/events")),
            createMenuItem(VaadinIcon.TICKET, "Réservations", "admin/reservations", currentRoute.equals("admin/reservations")),
            createMenuItem(VaadinIcon.ENVELOPE, "Boîte de réception", "admin/inbox", currentRoute.equals("admin/inbox")),
            createMenuItem(VaadinIcon.USER, "Mon Profil", "admin/profile", currentRoute.equals("admin/profile"))
        );
        
        // Bouton déconnexion
        Div logoutSection = new Div();
        logoutSection.getStyle()
            .set("padding", "15px 15px 20px 15px")
            .set("border-top", "1px solid #F0EDE8");
        
        Div logoutBtn = createMenuItem(VaadinIcon.SIGN_OUT, "Déconnexion", "logout", false);
        logoutBtn.getStyle()
            .set("color", "#E74C3C")
            .set("background", "rgba(231, 76, 60, 0.08)");
        
        Icon logoutIcon = (Icon) logoutBtn.getChildren().findFirst().get();
        logoutIcon.getStyle().set("color", "#E74C3C");
        
        logoutSection.add(logoutBtn);
        
        add(header, clockPanel, menu, logoutSection);
        
        // Mise à jour de l'heure toutes les secondes
        startClock();
    }
    
    private Div createClockPanel() {
        Div panel = new Div();
        panel.getStyle()
            .set("background", "linear-gradient(135deg, rgba(200, 160, 80, 0.08) 0%, rgba(212, 175, 110, 0.08) 100%)")
            .set("margin", "15px")
            .set("padding", "18px 15px")
            .set("border-radius", "12px")
            .set("border", "1px solid rgba(200, 160, 80, 0.15)")
            .set("text-align", "center");
        
        timeLabel = new Span();
        timeLabel.addClassName("time-label");
        timeLabel.getStyle()
            .set("display", "block")
            .set("color", "#C8A050")
            .set("font-size", "26px")
            .set("font-weight", "700")
            .set("margin-bottom", "6px")
            .set("letter-spacing", "1px");
        
        dateLabel = new Span();
        dateLabel.addClassName("date-label");
        dateLabel.getStyle()
            .set("display", "block")
            .set("color", "#8B8B8B")
            .set("font-size", "13px")
            .set("font-weight", "500");
        
        updateClock();
        
        panel.add(timeLabel, dateLabel);
        return panel;
    }
    
    private void startClock() {
        getElement().executeJs(
            "setInterval(() => {" +
            "  const now = new Date();" +
            "  const time = now.toLocaleTimeString('fr-FR');" +
            "  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };" +
            "  const date = now.toLocaleDateString('fr-FR', options);" +
            "  const timeEl = document.querySelector('.time-label');" +
            "  const dateEl = document.querySelector('.date-label');" +
            "  if (timeEl) timeEl.textContent = time;" +
            "  if (dateEl) dateEl.textContent = date;" +
            "}, 1000);"
        );
    }
    
    public void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH);
        
        timeLabel.setText(now.format(timeFormatter));
        dateLabel.setText(now.format(dateFormatter));
    }
    
    private Div createMenuItem(VaadinIcon iconType, String text, String route, boolean active) {
        Div menuItem = new Div();
        menuItem.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("padding", "13px 16px")
            .set("margin", "2px 0")
            .set("border-radius", "10px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("color", active ? "#C8A050" : "#6B6B6B")
            .set("background", active ? "rgba(200, 160, 80, 0.12)" : "transparent");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("margin-right", "12px")
            .set("color", active ? "#C8A050" : "#6B6B6B")
            .set("width", "20px")
            .set("height", "20px");
        
        Span label = new Span(text);
        label.getStyle()
            .set("font-size", "14px")
            .set("font-weight", active ? "600" : "500");
        
        menuItem.add(icon, label);
        
        // Effet hover
        menuItem.getElement().addEventListener("mouseenter", e -> {
            if (!active) {
                menuItem.getStyle()
                    .set("background", "rgba(200, 160, 80, 0.08)")
                    .set("color", "#C8A050");
                icon.getStyle().set("color", "#C8A050");
            }
        });
        
        menuItem.getElement().addEventListener("mouseleave", e -> {
            if (!active) {
                menuItem.getStyle()
                    .set("background", "transparent")
                    .set("color", "#6B6B6B");
                icon.getStyle().set("color", "#6B6B6B");
            }
        });
        
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