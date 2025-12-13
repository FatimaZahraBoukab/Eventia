package com.eventmanagement.eventreservation.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("admin/reservations")
public class AdminReservationsView extends Div {
    
    public AdminReservationsView() {
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        AdminSidebar sidebar = new AdminSidebar("admin/reservations");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        mainContent.add(createHeader(), createStatsCards(), createEmptyState());
        add(sidebar, mainContent);
    }
    
    private Div createHeader() {
        Div header = new Div();
        header.getStyle().set("margin-bottom", "30px");
        
        H2 title = new H2("Gestion des Réservations");
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
        searchField.setPlaceholder("Rechercher une réservation...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("300px");
        searchField.getStyle().set("--lumo-border-radius", "8px");
        
        Select<String> statusFilter = new Select<>();
        statusFilter.setPlaceholder("Statut");
        statusFilter.setItems("Toutes", "En attente", "Confirmée", "Annulée", "Remboursée");
        statusFilter.setWidth("150px");
        
        Select<String> dateFilter = new Select<>();
        dateFilter.setPlaceholder("Période");
        dateFilter.setItems("Toutes", "Aujourd'hui", "Cette semaine", "Ce mois", "Ce trimestre");
        dateFilter.setWidth("150px");
        
        leftSection.add(searchField, statusFilter, dateFilter);
        
        HorizontalLayout rightSection = new HorizontalLayout();
        rightSection.setSpacing(true);
        
        Button exportButton = new Button("Exporter", VaadinIcon.DOWNLOAD.create());
        exportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        exportButton.getStyle()
            .set("color", "#C8A050")
            .set("border", "1px solid #C8A050")
            .set("border-radius", "8px");
        
        Button refreshButton = new Button(VaadinIcon.REFRESH.create());
        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshButton.getStyle()
            .set("border-radius", "8px");
        
        rightSection.add(exportButton, refreshButton);
        
        toolbar.add(leftSection, rightSection);
        header.add(title, toolbar);
        return header;
    }
    
    private HorizontalLayout createStatsCards() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.getStyle()
            .set("gap", "20px")
            .set("margin-bottom", "30px");
        
        statsLayout.add(
            createStatCard("Total réservations", "0", VaadinIcon.TICKET, "#3498db", "+0%"),
            createStatCard("En attente", "0", VaadinIcon.CLOCK, "#f39c12", "0"),
            createStatCard("Confirmées", "0", VaadinIcon.CHECK_CIRCLE, "#2ecc71", "+0%"),
            createStatCard("Annulées", "0", VaadinIcon.CLOSE_CIRCLE, "#e74c3c", "0")
        );
        
        return statsLayout;
    }
    
    private Div createStatCard(String title, String value, VaadinIcon iconType, String color, String trend) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#ffffff")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("flex", "1")
            .set("min-width", "200px");
        
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setAlignItems(HorizontalLayout.Alignment.START);
        
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setPadding(false);
        leftContent.setSpacing(false);
        leftContent.getStyle().set("gap", "8px");
        
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
            .set("color", "#666666")
            .set("font-size", "13px")
            .set("display", "block");
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("font-size", "28px")
            .set("font-weight", "700")
            .set("color", "#333333")
            .set("display", "block");
        
        Span trendSpan = new Span(trend);
        trendSpan.getStyle()
            .set("font-size", "12px")
            .set("color", trend.startsWith("+") ? "#2ecc71" : "#666666")
            .set("display", "block");
        
        leftContent.add(titleSpan, valueSpan, trendSpan);
        
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
            .set("width", "45px")
            .set("height", "45px")
            .set("border-radius", "10px")
            .set("background", color + "20")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("color", color)
            .set("width", "22px")
            .set("height", "22px");
        
        iconWrapper.add(icon);
        
        header.add(leftContent, iconWrapper);
        card.add(header);
        return card;
    }
    
    private Div createEmptyState() {
        Div container = new Div();
        container.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "80px 60px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("text-align", "center");
        
        Span icon = new Span("🎫");
        icon.getStyle()
            .set("font-size", "72px")
            .set("display", "block")
            .set("margin-bottom", "25px");
        
        H2 title = new H2("Aucune réservation");
        title.getStyle()
            .set("color", "#333333")
            .set("font-size", "24px")
            .set("margin", "0 0 10px 0");
        
        Span message = new Span("Les réservations apparaîtront ici une fois que les clients auront commencé à réserver vos événements");
        message.getStyle()
            .set("color", "#666666")
            .set("font-size", "16px")
            .set("display", "block")
            .set("max-width", "500px")
            .set("margin", "0 auto 30px auto")
            .set("line-height", "1.6");
        
        Button viewEventsButton = new Button("Voir les événements", VaadinIcon.CALENDAR.create());
        viewEventsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        viewEventsButton.getStyle()
            .set("background", "#C8A050")
            .set("border-radius", "8px");
        
        viewEventsButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("admin/events"));
        });
        
        container.add(icon, title, message, viewEventsButton);
        return container;
    }
}