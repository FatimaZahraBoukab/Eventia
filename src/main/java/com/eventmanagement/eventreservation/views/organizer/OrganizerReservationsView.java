package com.eventmanagement.eventreservation.views.organizer;

import com.eventmanagement.eventreservation.entity.*;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.ReservationService;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("organizer/reservations")
@RolesAllowed("ORGANIZER")
public class OrganizerReservationsView extends Div {
    
    private final ReservationService reservationService;
    private final EventService eventService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    
    private Grid<Reservation> reservationsGrid;
    private ComboBox<Event> eventFilter;
    private ComboBox<ReservationStatus> statutFilter;
  
    
    private List<Event> organizerEvents;
    
    public OrganizerReservationsView(ReservationService reservationService, EventService eventService) {
        this.reservationService = reservationService;
        this.eventService = eventService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        organizerEvents = eventService.findByOrganisateur(currentUser);
        
        OrganizerSidebar sidebar = new OrganizerSidebar("organizer/reservations");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "40px")
            .set("overflow-y", "auto");
        
        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setWidthFull();
        titleRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        titleRow.setAlignItems(FlexComponent.Alignment.CENTER);
        titleRow.getStyle().set("margin-bottom", "30px");
        
        H2 title = new H2("Gestion des Réservations");
        title.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0")
            .set("font-size", "32px")
            .set("font-weight", "700");
        
        titleRow.add(title);
        
        // Barre de filtres
        Div filtersBar = createFiltersBar();
        
        // Créer le tableau Grid
        createReservationsGrid();
        
        loadAllReservations();
        
        mainContent.add(titleRow, filtersBar, reservationsGrid);
        add(sidebar, mainContent);
    }
    
    private Div createFiltersBar() {
        Div filtersBar = new Div();
        filtersBar.getStyle()
            .set("background", "#ffffff")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("margin-bottom", "30px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.05)")
            .set("display", "flex")
            .set("gap", "15px")
            .set("align-items", "center")
            .set("flex-wrap", "wrap");
        
        // Filtre par événement
        eventFilter = new ComboBox<>("Événement");
        eventFilter.setItems(organizerEvents);
        eventFilter.setItemLabelGenerator(Event::getTitre);
        eventFilter.setPlaceholder("Tous les événements");
        eventFilter.setClearButtonVisible(true);
        eventFilter.getStyle().set("min-width", "250px");
        eventFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre par statut
        statutFilter = new ComboBox<>("Statut");
        statutFilter.setItems(ReservationStatus.values());
        statutFilter.setItemLabelGenerator(ReservationStatus::getDisplayName);
        statutFilter.setPlaceholder("Tous les statuts");
        statutFilter.setClearButtonVisible(true);
        statutFilter.getStyle().set("min-width", "200px");
        statutFilter.addValueChangeListener(e -> applyFilters());
        
        
        
      
        
        filtersBar.add(eventFilter, statutFilter);
        return filtersBar;
    }
    
    private void createReservationsGrid() {
        reservationsGrid = new Grid<>(Reservation.class, false);
        reservationsGrid.setHeight("calc(100vh - 300px)");
        reservationsGrid.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.05)");
        
        // Colonne ID
        reservationsGrid.addColumn(reservation -> reservation.getId())
            .setHeader("ID")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Colonne Nom Utilisateur
        reservationsGrid.addColumn(reservation -> reservation.getUtilisateur().getFullName())
            .setHeader("Nom Utilisateur")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Email Utilisateur
        reservationsGrid.addColumn(reservation -> reservation.getUtilisateur().getEmail())
            .setHeader("Email")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Code Réservation
        reservationsGrid.addColumn(reservation -> reservation.getCodeReservation())
            .setHeader("Code Réservation")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Nom Événement
        reservationsGrid.addColumn(reservation -> reservation.getEvenement().getTitre())
            .setHeader("Nom Événement")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Places
        reservationsGrid.addColumn(reservation -> reservation.getNombrePlaces())
            .setHeader("Places")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Colonne Montant
        reservationsGrid.addColumn(reservation -> String.format("%.2f DH", reservation.getMontantTotal()))
            .setHeader("Montant")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Colonne Statut avec badge coloré
        reservationsGrid.addComponentColumn(reservation -> {
            Span badge = createStatutBadgeSimple(reservation.getStatut());
            return badge;
        })
        .setHeader("Statut")
        .setAutoWidth(true)
        .setFlexGrow(0);
        
        // Colonne Actions
        reservationsGrid.addComponentColumn(reservation -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);
            actions.getStyle().set("gap", "5px");
            
            // Bouton Détails
            Button detailsButton = new Button(new Icon(VaadinIcon.EYE));
            detailsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            detailsButton.getElement().setAttribute("title", "Détails");
            detailsButton.addClickListener(e -> showReservationDetails(reservation));
            actions.add(detailsButton);
            
            // Bouton Confirmer (si EN_ATTENTE)
            if (reservation.getStatut() == ReservationStatus.EN_ATTENTE) {
                Button confirmerButton = new Button(new Icon(VaadinIcon.CHECK));
                confirmerButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
                confirmerButton.getElement().setAttribute("title", "Confirmer");
                confirmerButton.addClickListener(e -> confirmReservation(reservation));
                actions.add(confirmerButton);
            }
            
            // Bouton Annuler (si EN_ATTENTE ou CONFIRMEE)
            if (reservation.getStatut() == ReservationStatus.EN_ATTENTE 
                || reservation.getStatut() == ReservationStatus.CONFIRMEE) {
                Button annulerButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                annulerButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                annulerButton.getElement().setAttribute("title", "Annuler");
                annulerButton.addClickListener(e -> cancelReservation(reservation));
                actions.add(annulerButton);
            }
            
            // Bouton Supprimer (toujours disponible)
            Button supprimerButton = new Button(new Icon(VaadinIcon.TRASH));
            supprimerButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            supprimerButton.getElement().setAttribute("title", "Supprimer");
            supprimerButton.addClickListener(e -> deleteReservation(reservation));
            actions.add(supprimerButton);
            
            return actions;
        })
        .setHeader("Actions")
        .setAutoWidth(true)
        .setFlexGrow(0);
    }
    
    private Span createStatutBadgeSimple(ReservationStatus statut) {
        Span badge = new Span();
        badge.getStyle()
            .set("padding", "4px 10px")
            .set("border-radius", "12px")
            .set("font-size", "11px")
            .set("font-weight", "600")
            .set("text-transform", "uppercase")
            .set("white-space", "nowrap");
        
        switch (statut) {
            case EN_ATTENTE:
                badge.getStyle()
                    .set("background", "#FFF4E5")
                    .set("color", "#f39c12");
                badge.setText("En attente");
                break;
            case CONFIRMEE:
                badge.getStyle()
                    .set("background", "#E8F5E9")
                    .set("color", "#27ae60");
                badge.setText("Confirmée");
                break;
            case ANNULEE:
                badge.getStyle()
                    .set("background", "#FFEBEE")
                    .set("color", "#e74c3c");
                badge.setText("Annulée");
                break;
        }
        
        return badge;
    }
    
    private void applyFilters() {
        Event selectedEvent = eventFilter.getValue();
        ReservationStatus selectedStatut = statutFilter.getValue();
    
        
        List<Reservation> filteredReservations;
        
        if (selectedEvent != null) {
            if (selectedStatut != null) {
                filteredReservations = reservationService.findByEvenementAndStatut(selectedEvent, selectedStatut);
            } else {
                filteredReservations = reservationService.findByEvenement(selectedEvent);
            }
        } else {
            filteredReservations = organizerEvents.stream()
                .flatMap(event -> {
                    if (selectedStatut != null) {
                        return reservationService.findByEvenementAndStatut(event, selectedStatut).stream();
                    } else {
                        return reservationService.findByEvenement(event).stream();
                    }
                })
                .collect(Collectors.toList());
        }
        
        
        reservationsGrid.setItems(filteredReservations);
    }
    
    private void loadAllReservations() {
        List<Reservation> allReservations = organizerEvents.stream()
            .flatMap(event -> reservationService.findByEvenement(event).stream())
            .sorted((r1, r2) -> r2.getDateReservation().compareTo(r1.getDateReservation()))
            .collect(Collectors.toList());
        
        reservationsGrid.setItems(allReservations);
    }
    
    private void confirmReservation(Reservation reservation) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Confirmer la réservation");
        confirmDialog.setText("Voulez-vous confirmer la réservation " + 
            reservation.getCodeReservation() + " de " + 
            reservation.getUtilisateur().getFullName() + " ?");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Annuler");
        
        confirmDialog.setConfirmText("Confirmer");
        confirmDialog.setConfirmButtonTheme("success primary");
        
        confirmDialog.addConfirmListener(e -> {
            try {
                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.confirmerReservation(reservation.getId(), currentUser);
                
                Notification.show("Réservation confirmée avec succès", 
                    3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                applyFilters();
                
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 
                    5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmDialog.open();
    }
    
    private void cancelReservation(Reservation reservation) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Annuler la réservation");
        confirmDialog.setText("Voulez-vous annuler la réservation " + 
            reservation.getCodeReservation() + " ?");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Non");
        
        confirmDialog.setConfirmText("Oui, annuler");
        confirmDialog.setConfirmButtonTheme("error primary");
        
        confirmDialog.addConfirmListener(e -> {
            try {
                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.annulerReservation(reservation.getId(), currentUser);
                
                Notification.show("Réservation annulée", 
                    3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                applyFilters();
                
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 
                    5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmDialog.open();
    }
    
    private void deleteReservation(Reservation reservation) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Supprimer définitivement ?");
        confirmDialog.setText("Voulez-vous vraiment supprimer définitivement la réservation " + 
            reservation.getCodeReservation() + " ? Cette action est irréversible.");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Non");
        
        confirmDialog.setConfirmText("Oui, supprimer");
        confirmDialog.setConfirmButtonTheme("error primary");
        
        confirmDialog.addConfirmListener(e -> {
            try {
                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.supprimerReservation(reservation.getId(), currentUser);
                
                Notification.show("Réservation supprimée définitivement", 
                    3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                applyFilters();
                
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 
                    5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmDialog.open();
    }
    
    private void showReservationDetails(Reservation reservation) {
        Dialog dialog = new Dialog();
        dialog.setWidth("750px");
        dialog.setMaxHeight("90vh");

        Div content = new Div();
        content.getStyle().set("padding", "35px");

        // ================= HEADER =================
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

        // ================= CODE + STATUT =================
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
        codeSection.add(codeBox, createStatutBadgeSimple(reservation.getStatut()));

        // ================= CLIENT =================
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

        // ================= EVENT =================
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

        // ================= RESERVATION =================
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

        // ================= ACTIONS =================
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setWidthFull();
        actionButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        actionButtons.getStyle().set("gap", "15px");

        if (reservation.getStatut() == ReservationStatus.EN_ATTENTE) {
            Button confirmBtn = new Button("Confirmer", new Icon(VaadinIcon.CHECK));
            confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            confirmBtn.addClickListener(e -> {
                dialog.close();
                confirmReservation(reservation);
            });
            actionButtons.add(confirmBtn);
        }

        if (reservation.getStatut() == ReservationStatus.EN_ATTENTE
                || reservation.getStatut() == ReservationStatus.CONFIRMEE) {
            Button cancelBtn = new Button("Annuler", new Icon(VaadinIcon.CLOSE_CIRCLE));
            cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            cancelBtn.addClickListener(e -> {
                dialog.close();
                cancelReservation(reservation);
            });
            actionButtons.add(cancelBtn);
        }

        Button deleteBtn = new Button("Supprimer", new Icon(VaadinIcon.TRASH));
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        deleteBtn.addClickListener(e -> {
            dialog.close();
            deleteReservation(reservation);
        });
        actionButtons.add(deleteBtn);

        // ================= FINAL =================
        content.add(
                header,
                codeSection,
                clientTitle, clientBox,
                eventTitle, eventBox,
                resTitle, resBox,
                actionButtons
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
}