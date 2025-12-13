package com.eventmanagement.eventreservation.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("admin/events")
public class AdminEventsView extends Div {
    
    public AdminEventsView() {
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        AdminSidebar sidebar = new AdminSidebar("admin/events");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        mainContent.add(createHeader(), createEmptyState());
        add(sidebar, mainContent);
    }
    
    private Div createHeader() {
        Div header = new Div();
        header.getStyle().set("margin-bottom", "30px");
        
        H2 title = new H2("Gestion des Événements");
        title.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 20px 0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(HorizontalLayout.Alignment.CENTER);
        
        HorizontalLayout leftSection = new HorizontalLayout();
        leftSection.setSpacing(true);
        
        TextField searchField = new TextField();
        searchField.setPlaceholder("Rechercher un événement...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("300px");
        searchField.getStyle().set("--lumo-border-radius", "8px");
        
        Select<String> categoryFilter = new Select<>();
        categoryFilter.setPlaceholder("Catégorie");
        categoryFilter.setItems("Toutes", "Conférence", "Concert", "Festival", "Spectacle", "Sport");
        categoryFilter.setWidth("150px");
        
        Select<String> statusFilter = new Select<>();
        statusFilter.setPlaceholder("Statut");
        statusFilter.setItems("Tous", "À venir", "En cours", "Terminé", "Annulé");
        statusFilter.setWidth("140px");
        
        leftSection.add(searchField, categoryFilter, statusFilter);
        
        Button addButton = new Button("Nouvel événement", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.getStyle()
            .set("background", "#C8A050")
            .set("border-radius", "8px");
        
        toolbar.add(leftSection, addButton);
        header.add(title, toolbar);
        return header;
    }
    
    private Div createEmptyState() {
        Div container = new Div();
        container.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "80px 60px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("text-align", "center");
        
        Span icon = new Span("📅");
        icon.getStyle()
            .set("font-size", "72px")
            .set("display", "block")
            .set("margin-bottom", "25px");
        
        H2 title = new H2("Aucun événement");
        title.getStyle()
            .set("color", "#333333")
            .set("font-size", "24px")
            .set("margin", "0 0 10px 0");
        
        Span message = new Span("Commencez par créer votre premier événement");
        message.getStyle()
            .set("color", "#666666")
            .set("font-size", "16px")
            .set("display", "block")
            .set("margin-bottom", "30px");
        
        Button createButton = new Button("Créer un événement", VaadinIcon.PLUS.create());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        createButton.getStyle()
            .set("background", "#C8A050")
            .set("border-radius", "8px");
        
        container.add(icon, title, message, createButton);
        return container;
    }
}