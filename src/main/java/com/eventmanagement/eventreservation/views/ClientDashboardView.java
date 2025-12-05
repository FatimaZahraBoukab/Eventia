package com.eventmanagement.eventreservation.views;

import com.eventmanagement.eventreservation.entity.User;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("client/dashboard")
public class ClientDashboardView extends Div {
    
    public ClientDashboardView() {
        setSizeFull();
        getStyle()
            .set("background-color", "#f5f5f5")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        // Récupérer l'utilisateur de la session
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        
        // Container
        VerticalLayout container = new VerticalLayout();
        container.getStyle()
            .set("background-color", "#ffffff")
            .set("padding", "60px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 10px 40px rgba(0,0,0,0.1)")
            .set("text-align", "center");
        
        // Titre
        H1 title = new H1("🎉 Hello Client !");
        title.getStyle()
            .set("color", "#c9a961")
            .set("font-family", "'Playfair Display', Georgia, serif")
            .set("font-size", "3rem")
            .set("margin-bottom", "20px");
        
        // Message de bienvenue
        Paragraph welcome = new Paragraph();
        if (user != null) {
            welcome.setText("Bienvenue " + user.getFullName() + " !");
        } else {
            welcome.setText("Bienvenue sur votre espace client !");
        }
        welcome.getStyle()
            .set("font-size", "1.3rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "10px");
        
        // Info rôle
        Paragraph role = new Paragraph("Rôle : CLIENT");
        role.getStyle()
            .set("font-size", "1rem")
            .set("color", "#666")
            .set("margin-top", "0");
        
        container.add(title, welcome, role);
        add(container);
    }
}