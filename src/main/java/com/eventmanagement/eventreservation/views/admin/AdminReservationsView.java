package com.eventmanagement.eventreservation.views.admin;

import com.eventmanagement.eventreservation.entity.*;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.ReservationService;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("admin/reservations")
@RolesAllowed("ADMIN")
public class AdminReservationsView extends Div {
    
    private final ReservationService reservationService;
    private final EventService eventService;
    private final UserService userService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    
    private Grid<Reservation> reservationsGrid;
    private ComboBox<Event> eventFilter;
    private ComboBox<ReservationStatus> statutFilter;
    private TextField searchField;
    
    public AdminReservationsView(ReservationService reservationService,
                                EventService eventService,
                                UserService userService) {
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.userService = userService;
        
        try {
            buildUI();
        } catch (Exception e) {
            removeAll();
            Div errorDiv = new Div();
            errorDiv.getStyle()
                .set("padding", "40px")
                .set("color", "red");
            errorDiv.add(new H3("Erreur lors du chargement"));
            errorDiv.add(new Paragraph("Message: " + e.getMessage()));
            add(errorDiv);
            e.printStackTrace();
        }
    }
    
    private void buildUI() {
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
            .set("padding", "40px")
            .set("overflow-y", "auto");
        
        H2 title = new H2("Gestion Globale des Réservations");
        title.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0 0 30px 0")
            .set("font-size", "32px")
            .set("font-weight", "700");
        
        // Filtres
        Div filtersBar = createAdvancedFiltersBar();
        
        // Grid
        createReservationsGrid();
        
        try {
            loadAllReservations();
        } catch (Exception e) {
            Notification.show("Erreur chargement réservations: " + e.getMessage(), 
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        
        mainContent.add(title, filtersBar, reservationsGrid);
        add(sidebar, mainContent);
    }
    
    private Div createGlobalStats() {
        List<Reservation> allReservations = new ArrayList<>();
        try {
            allReservations = reservationService.findAll();
        } catch (Exception e) {
            System.err.println("Erreur findAll: " + e.getMessage());
            allReservations = new ArrayList<>();
        }
        
        long totalReservations = allReservations.size();
        long enAttente = allReservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.EN_ATTENTE).count();
        long confirmees = allReservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.CONFIRMEE).count();
        long annulees = allReservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.ANNULEE).count();
        
        Div statsGrid = new Div();
        statsGrid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(4, 1fr)")
            .set("gap", "20px")
            .set("margin-bottom", "30px");
        
        statsGrid.add(
            createStatCard("Total", String.valueOf(totalReservations), "#3498db"),
            createStatCard("En Attente", String.valueOf(enAttente), "#f39c12"),
            createStatCard("Confirmées", String.valueOf(confirmees), "#27ae60"),
            createStatCard("Annulées", String.valueOf(annulees), "#e74c3c")
        );
        
        return statsGrid;
    }
    
    private Div createStatCard(String label, String value, String color) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#ffffff")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("border-left", "4px solid " + color);
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("display", "block")
            .set("font-size", "28px")
            .set("font-weight", "800")
            .set("color", "#2C3E50")
            .set("margin-bottom", "5px");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("display", "block")
            .set("font-size", "13px")
            .set("color", "#999")
            .set("text-transform", "uppercase");
        
        card.add(valueSpan, labelSpan);
        return card;
    }
    
    private Div createAdvancedFiltersBar() {
        Div filtersBar = new Div();
        filtersBar.getStyle()
            .set("background", "#ffffff")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("margin-bottom", "30px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.05)")
            .set("display", "flex")
            .set("gap", "15px")
            .set("flex-wrap", "wrap");
        
        try {
            // Filtre par événement
            eventFilter = new ComboBox<>("Événement");
            List<Event> events = eventService.findAll();
            eventFilter.setItems(events);
            eventFilter.setItemLabelGenerator(Event::getTitre);
            eventFilter.setPlaceholder("Tous");
            eventFilter.setClearButtonVisible(true);
            eventFilter.setWidth("250px");
            eventFilter.addValueChangeListener(e -> applyFilters());
            
            // Filtre par statut
            statutFilter = new ComboBox<>("Statut");
            statutFilter.setItems(ReservationStatus.values());
            statutFilter.setItemLabelGenerator(ReservationStatus::getDisplayName);
            statutFilter.setPlaceholder("Tous");
            statutFilter.setClearButtonVisible(true);
            statutFilter.setWidth("150px");
            statutFilter.addValueChangeListener(e -> applyFilters());
            
            // Recherche
            searchField = new TextField();
            searchField.setPlaceholder("Rechercher...");
            searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            searchField.setClearButtonVisible(true);
            searchField.setWidth("300px");
            searchField.getStyle().set("padding-top", "37px");
            searchField.addValueChangeListener(e -> applyFilters());
            
            
            
            filtersBar.add(eventFilter, statutFilter, searchField);
            
        } catch (Exception e) {
            filtersBar.add(new Span("Erreur chargement filtres: " + e.getMessage()));
        }
        
        return filtersBar;
    }
    
    private void createReservationsGrid() {
        reservationsGrid = new Grid<>(Reservation.class, false);
        reservationsGrid.setAllRowsVisible(true);
        reservationsGrid.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.05)");
        
        reservationsGrid.addColumn(Reservation::getId)
            .setHeader("ID")
            .setAutoWidth(true)
            .setFlexGrow(0);
            
        reservationsGrid.addColumn(Reservation::getCodeReservation)
            .setHeader("Code")
            .setAutoWidth(true)
            .setFlexGrow(1);
            
        reservationsGrid.addColumn(r -> {
            try {
                return r.getUtilisateur() != null ? r.getUtilisateur().getFullName() : "N/A";
            } catch (Exception e) {
                return "Erreur";
            }
        })
        .setHeader("Client")
        .setAutoWidth(true)
        .setFlexGrow(1);
        
        reservationsGrid.addColumn(r -> {
            try {
                return r.getEvenement() != null ? r.getEvenement().getTitre() : "N/A";
            } catch (Exception e) {
                return "Erreur";
            }
        })
        .setHeader("Événement")
        .setAutoWidth(true)
        .setFlexGrow(1);
        
        reservationsGrid.addColumn(Reservation::getNombrePlaces)
            .setHeader("Places")
            .setAutoWidth(true)
            .setFlexGrow(0);
            
        reservationsGrid.addColumn(r -> String.format("%.2f DH", r.getMontantTotal()))
            .setHeader("Montant")
            .setAutoWidth(true)
            .setFlexGrow(0);
            
        reservationsGrid.addComponentColumn(r -> createStatutBadge(r.getStatut()))
            .setHeader("Statut")
            .setAutoWidth(true)
            .setFlexGrow(0);
            
        reservationsGrid.addComponentColumn(this::createActionsColumn)
            .setHeader("Actions")
            .setAutoWidth(true)
            .setFlexGrow(0);
    }
    
    private Span createStatutBadge(ReservationStatus statut) {
        Span badge = new Span(statut.getDisplayName());
        badge.getStyle()
            .set("padding", "4px 10px")
            .set("border-radius", "12px")
            .set("font-size", "11px")
            .set("font-weight", "600")
            .set("white-space", "nowrap");
        
        switch (statut) {
            case EN_ATTENTE:
                badge.getStyle().set("background", "#FFF4E5").set("color", "#f39c12");
                break;
            case CONFIRMEE:
                badge.getStyle().set("background", "#E8F5E9").set("color", "#27ae60");
                break;
            case ANNULEE:
                badge.getStyle().set("background", "#FFEBEE").set("color", "#e74c3c");
                break;
        }
        return badge;
    }
    
    private HorizontalLayout createActionsColumn(Reservation reservation) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.getStyle().set("gap", "5px");
        
        Button detailsBtn = new Button(new Icon(VaadinIcon.EYE));
        detailsBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        detailsBtn.getElement().setAttribute("title", "Détails");
        detailsBtn.addClickListener(e -> showReservationDetails(reservation));
        actions.add(detailsBtn);
        
        if (reservation.getStatut() == ReservationStatus.EN_ATTENTE) {
            Button confirmBtn = new Button(new Icon(VaadinIcon.CHECK));
            confirmBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            confirmBtn.getElement().setAttribute("title", "Confirmer");
            confirmBtn.addClickListener(e -> confirmReservation(reservation));
            actions.add(confirmBtn);
        }
        
        if (reservation.getStatut() == ReservationStatus.EN_ATTENTE || 
            reservation.getStatut() == ReservationStatus.CONFIRMEE) {
            Button cancelBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
            cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            cancelBtn.getElement().setAttribute("title", "Annuler");
            cancelBtn.addClickListener(e -> cancelReservation(reservation));
            actions.add(cancelBtn);
        }
        
        Button deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        deleteBtn.getElement().setAttribute("title", "Supprimer");
        deleteBtn.addClickListener(e -> deleteReservation(reservation));
        actions.add(deleteBtn);
        
        return actions;
    }
    
    private void showReservationDetails(Reservation reservation) {
        Dialog dialog = new Dialog();
        dialog.setWidth("750px");
        dialog.setMaxHeight("90vh");

        Div content = new Div();
        content.getStyle().set("padding", "35px");

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "30px");

        H2 title = new H2("Détails de la réservation");
        title.getStyle()
            .set("margin", "0")
            .set("color", "#2C3E50")
            .set("font-weight", "700");

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addClickListener(e -> dialog.close());

        header.add(title, closeButton);

        // Code + Statut
        Div codeSection = new Div();
        codeSection.getStyle()
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("align-items", "center")
            .set("padding", "25px")
            .set("background", "#FFF8E7")
            .set("border-radius", "12px")
            .set("border", "2px solid #C8A050")
            .set("margin-bottom", "30px");

        Div codeBox = new Div();
        Span codeLabel = new Span("Code de réservation");
        codeLabel.getStyle()
            .set("font-size", "12px")
            .set("color", "#999");

        Span codeValue = new Span(reservation.getCodeReservation());
        codeValue.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "800")
            .set("color", "#C8A050");

        codeBox.add(codeLabel, codeValue);
        codeSection.add(codeBox, createStatutBadge(reservation.getStatut()));

        // Client
        H3 clientTitle = new H3("Client");
        Div clientBox = new Div();
        clientBox.getStyle()
            .set("padding", "20px")
            .set("background", "#F8F9FA")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px");

        clientBox.add(
            createDetailRow("Nom", reservation.getUtilisateur().getFullName()),
            createDetailRow("Email", reservation.getUtilisateur().getEmail())
        );

        // Événement
        H3 eventTitle = new H3("Événement");
        Div eventBox = new Div();
        eventBox.getStyle()
            .set("padding", "20px")
            .set("background", "#F8F9FA")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px");

        Event event = reservation.getEvenement();
        eventBox.add(
            createDetailRow("Titre", event.getTitre()),
            createDetailRow("Date", event.getDateDebut().format(DATE_FORMATTER)),
            createDetailRow("Lieu", event.getLieu() + " - " + event.getVille())
        );

        // Réservation
        H3 resTitle = new H3("Détails de la réservation");
        Div resBox = new Div();
        resBox.getStyle()
            .set("padding", "20px")
            .set("background", "#F8F9FA")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px");

        resBox.add(
            createDetailRow("Places", String.valueOf(reservation.getNombrePlaces())),
            createDetailRow("Montant", String.format("%.2f DH", reservation.getMontantTotal())),
            createDetailRow("Date réservation",
                reservation.getDateReservation().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                ))
        );

        content.add(
            header,
            codeSection,
            clientTitle, clientBox,
            eventTitle, eventBox,
            resTitle, resBox
        );

        dialog.add(content);
        dialog.open();
    }
    
    private Div createDetailRow(String label, String value) {
        Div row = new Div();
        row.getStyle()
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("padding", "12px 0")
            .set("border-bottom", "1px solid #E8E8E8");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-weight", "600")
            .set("color", "#666");
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("font-weight", "700")
            .set("color", "#2C3E50")
            .set("text-align", "right");
        
        row.add(labelSpan, valueSpan);
        return row;
    }
    
    private void applyFilters() {
        try {
            List<Reservation> filtered = reservationService.findAll();
            
            if (eventFilter != null && eventFilter.getValue() != null) {
                filtered = filtered.stream()
                    .filter(r -> r.getEvenement().getId().equals(eventFilter.getValue().getId()))
                    .collect(Collectors.toList());
            }
            
            if (statutFilter != null && statutFilter.getValue() != null) {
                filtered = filtered.stream()
                    .filter(r -> r.getStatut() == statutFilter.getValue())
                    .collect(Collectors.toList());
            }
            
            if (searchField != null && searchField.getValue() != null && !searchField.getValue().trim().isEmpty()) {
                String search = searchField.getValue().toLowerCase();
                filtered = filtered.stream()
                    .filter(r -> r.getCodeReservation().toLowerCase().contains(search) ||
                                r.getUtilisateur().getFullName().toLowerCase().contains(search) ||
                                r.getEvenement().getTitre().toLowerCase().contains(search))
                    .collect(Collectors.toList());
            }
            
            reservationsGrid.setItems(filtered);
        } catch (Exception e) {
            Notification.show("Erreur lors du filtrage: " + e.getMessage(), 
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void loadAllReservations() {
        try {
            List<Reservation> allReservations = reservationService.findAll();
            reservationsGrid.setItems(allReservations);
        } catch (Exception e) {
            Notification.show("Erreur chargement: " + e.getMessage(), 
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void clearFilters() {
        if (eventFilter != null) eventFilter.clear();
        if (statutFilter != null) statutFilter.clear();
        if (searchField != null) searchField.clear();
        loadAllReservations();
    }
    
    private void confirmReservation(Reservation reservation) {
        ConfirmDialog dialog = new ConfirmDialog();
                    dialog.setHeader("Confirmer");
        dialog.setText("Confirmer la réservation " + reservation.getCodeReservation() + " ?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Confirmer");
        dialog.setConfirmButtonTheme("success primary");
        
        dialog.addConfirmListener(e -> {
            try {
                User user = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.confirmerReservation(reservation.getId(), user);
                Notification.show("Réservation confirmée", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                applyFilters();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        dialog.open();
    }
    
    private void cancelReservation(Reservation reservation) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Annuler");
        dialog.setText("Annuler la réservation " + reservation.getCodeReservation() + " ?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Annuler");
        dialog.setConfirmButtonTheme("error primary");
        
        dialog.addConfirmListener(e -> {
            try {
                User user = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.annulerReservation(reservation.getId(), user);
                Notification.show("Réservation annulée", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                applyFilters();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        dialog.open();
    }
    
    private void deleteReservation(Reservation reservation) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Supprimer définitivement");
        dialog.setText("Supprimer la réservation " + reservation.getCodeReservation() + " ? Irréversible.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Supprimer");
        dialog.setConfirmButtonTheme("error primary");
        
        dialog.addConfirmListener(e -> {
            try {
                User user = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.supprimerReservation(reservation.getId(), user);
                Notification.show("Réservation supprimée", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                applyFilters();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        dialog.open();
    }
}