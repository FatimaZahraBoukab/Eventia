package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.Reservation;
import com.eventmanagement.eventreservation.entity.ReservationStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.ReservationService;
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
import java.util.List;
import java.util.stream.Collectors;

@Route("client/reservations")
@RolesAllowed("CLIENT")
public class ClientReservationsView extends Div {
    
    private final ReservationService reservationService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    private Grid<Reservation> reservationsGrid;
    private ComboBox<ReservationStatus> statutFilter;
    private TextField searchField;
    
    public ClientReservationsView(ReservationService reservationService) {
        this.reservationService = reservationService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        ClientSidebar sidebar = new ClientSidebar("client/reservations");
        
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
        
        H2 title = new H2("Mes Réservations");
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
        
        loadReservations(null);
        
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
        
        // Filtre par statut
        statutFilter = new ComboBox<>("Filtrer par statut");
        statutFilter.setItems(ReservationStatus.values());
        statutFilter.setItemLabelGenerator(ReservationStatus::getDisplayName);
        statutFilter.setPlaceholder("Tous les statuts");
        statutFilter.setClearButtonVisible(true);
        statutFilter.getStyle().set("min-width", "200px");
        statutFilter.addValueChangeListener(e -> loadReservations(e.getValue()));
        
        // Recherche par code
        searchField = new TextField();
        searchField.setPlaceholder("Rechercher par code ou événement...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setClearButtonVisible(true);
        searchField.getStyle().set("min-width", "300px");
        searchField.getStyle().set("padding-top", "37px");
        searchField.addValueChangeListener(e -> applyFilters());
        
        Button refreshButton = new Button("Actualiser", new Icon(VaadinIcon.REFRESH));
        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshButton.addClickListener(e -> {
            statutFilter.clear();
            searchField.clear();
            loadReservations(null);
            Notification.show("Liste actualisée", 2000, Notification.Position.BOTTOM_CENTER);
        });
        
        filtersBar.add(statutFilter, searchField, refreshButton);
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
        
        // Colonne Code Réservation
        reservationsGrid.addColumn(reservation -> reservation.getCodeReservation())
            .setHeader("Code Réservation")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Nom Événement
        reservationsGrid.addColumn(reservation -> reservation.getEvenement().getTitre())
            .setHeader("Événement")
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
        
        // Colonne Date Réservation
        reservationsGrid.addColumn(reservation -> 
            reservation.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setHeader("Date Réservation")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
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
            
            // Bouton Annuler (si EN_ATTENTE ou CONFIRMEE)
            if (reservation.getStatut() == ReservationStatus.EN_ATTENTE 
                || reservation.getStatut() == ReservationStatus.CONFIRMEE) {
                Button annulerButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
                annulerButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                annulerButton.getElement().setAttribute("title", "Annuler");
                annulerButton.addClickListener(e -> confirmAnnulation(reservation));
                actions.add(annulerButton);
            }
            
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
        ReservationStatus selectedStatut = statutFilter.getValue();
        String searchText = searchField.getValue();
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        List<Reservation> filteredReservations;
        
        if (selectedStatut != null) {
            filteredReservations = reservationService.findByUtilisateurAndStatut(currentUser, selectedStatut);
        } else {
            filteredReservations = reservationService.findByUtilisateur(currentUser);
        }
        
        // Appliquer la recherche textuelle
        if (searchText != null && !searchText.trim().isEmpty()) {
            String searchLower = searchText.toLowerCase().trim();
            filteredReservations = filteredReservations.stream()
                .filter(r -> r.getCodeReservation().toLowerCase().contains(searchLower) ||
                            r.getEvenement().getTitre().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
        }
        
        reservationsGrid.setItems(filteredReservations);
    }
    
    private void loadReservations(ReservationStatus statut) {
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        List<Reservation> reservations;
        
        if (statut != null) {
            reservations = reservationService.findByUtilisateurAndStatut(currentUser, statut);
        } else {
            reservations = reservationService.findByUtilisateur(currentUser);
        }
        
        // Trier par date de réservation (plus récent en premier)
        reservations = reservations.stream()
            .sorted((r1, r2) -> r2.getDateReservation().compareTo(r1.getDateReservation()))
            .collect(Collectors.toList());
        
        reservationsGrid.setItems(reservations);
    }
    
    private void showReservationDetails(Reservation reservation) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        dialog.setMaxHeight("90vh");
        
        Div content = new Div();
        content.getStyle().set("padding", "30px");
        
        // En-tête
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "25px");
        
        H2 title = new H2("Détails de la réservation");
        title.getStyle()
            .set("margin", "0")
            .set("color", "#2C3E50")
            .set("font-weight", "700");
        
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addClickListener(e -> dialog.close());
        
        header.add(title, closeButton);
        
        // Code et statut
        Div codeStatutSection = new Div();
        codeStatutSection.getStyle()
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
            .set("display", "block")
            .set("font-size", "12px")
            .set("color", "#999")
            .set("margin-bottom", "5px");
        
        Span codeValue = new Span(reservation.getCodeReservation());
        codeValue.getStyle()
            .set("display", "block")
            .set("font-size", "24px")
            .set("font-weight", "800")
            .set("color", "#C8A050")
            .set("letter-spacing", "2px");
        
        codeBox.add(codeLabel, codeValue);
        
        Span statutBadge = createStatutBadgeSimple(reservation.getStatut());
        statutBadge.getStyle()
            .set("padding", "8px 16px")
            .set("font-size", "12px");
        
        codeStatutSection.add(codeBox, statutBadge);
        
        // Section Événement
        H3 eventTitle = new H3("Événement");
        eventTitle.getStyle()
            .set("margin", "0 0 15px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px");
        
        Div eventBox = new Div();
        eventBox.getStyle()
            .set("padding", "20px")
            .set("background", "#F8F9FA")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px");
        
        eventBox.add(
            createDetailRow("Titre", reservation.getEvenement().getTitre()),
            createDetailRow("Date", 
                reservation.getEvenement().getDateDebut().format(DATE_FORMATTER) + 
                " à " + reservation.getEvenement().getDateDebut().format(TIME_FORMATTER)),
            createDetailRow("Lieu", 
                reservation.getEvenement().getLieu() + ", " + reservation.getEvenement().getVille())
        );
        
        // Section Réservation
        H3 resTitle = new H3("Détails de la réservation");
        resTitle.getStyle()
            .set("margin", "0 0 15px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px");
        
        Div resBox = new Div();
        resBox.getStyle()
            .set("padding", "20px")
            .set("background", "#F8F9FA")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px");
        
        resBox.add(
            createDetailRow("Nombre de places", String.valueOf(reservation.getNombrePlaces())),
            createDetailRow("Montant total", String.format("%.2f DH", reservation.getMontantTotal())),
            createDetailRow("Date de réservation", 
                reservation.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")))
        );
        
        // Commentaire si présent
        if (reservation.getCommentaire() != null && !reservation.getCommentaire().isEmpty()) {
            H3 commentTitle = new H3("Commentaire");
            commentTitle.getStyle()
                .set("margin", "0 0 15px 0")
                .set("color", "#2C3E50")
                .set("font-size", "18px");
            
            Paragraph commentValue = new Paragraph(reservation.getCommentaire());
            commentValue.getStyle()
                .set("margin", "0")
                .set("color", "#666")
                .set("line-height", "1.6")
                .set("padding", "20px")
                .set("background", "#F8F9FA")
                .set("border-radius", "12px");
            
            content.add(eventTitle, eventBox, resTitle, resBox, commentTitle, commentValue);
        } else {
            content.add(eventTitle, eventBox, resTitle, resBox);
        }
        
        // Ajouter le header et codeSection au début
        Div finalContent = new Div();
        finalContent.getStyle().set("padding", "30px");
        finalContent.add(header, codeStatutSection);
        
        if (reservation.getCommentaire() != null && !reservation.getCommentaire().isEmpty()) {
            finalContent.add(eventTitle, eventBox, resTitle, resBox);
            
            H3 commentTitle = new H3("Commentaire");
            commentTitle.getStyle()
                .set("margin", "0 0 15px 0")
                .set("color", "#2C3E50")
                .set("font-size", "18px");
            
            Paragraph commentValue = new Paragraph(reservation.getCommentaire());
            commentValue.getStyle()
                .set("margin", "0")
                .set("color", "#666")
                .set("line-height", "1.6")
                .set("padding", "20px")
                .set("background", "#F8F9FA")
                .set("border-radius", "12px");
            
            finalContent.add(commentTitle, commentValue);
        } else {
            finalContent.add(eventTitle, eventBox, resTitle, resBox);
        }
        
        dialog.add(finalContent);
        dialog.open();
    }
    
    private void confirmAnnulation(Reservation reservation) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Annuler la réservation ?");
        confirmDialog.setText("Êtes-vous sûr de vouloir annuler cette réservation ? " +
            "Cette action est irréversible.");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Non, garder");
        
        confirmDialog.setConfirmText("Oui, annuler");
        confirmDialog.setConfirmButtonTheme("error primary");
        
        confirmDialog.addConfirmListener(e -> {
            try {
                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                reservationService.annulerReservation(reservation.getId(), currentUser);
                
                Notification.show("Réservation annulée avec succès", 
                    3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                loadReservations(statutFilter.getValue());
                
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 
                    5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmDialog.open();
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