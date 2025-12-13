package com.eventmanagement.eventreservation.views.organizer;

import com.eventmanagement.eventreservation.views.organizer.EventForm;
import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
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

@Route("organizer/events")
@RolesAllowed("ORGANIZER")
public class OrganizerEventsView extends Div {
    
    private final EventService eventService;
    private final UserService userService;
    
    private User currentUser;
    private Grid<Event> grid;
    private ComboBox<EventStatus> statusFilter;
    private EventForm form;
    private Dialog formDialog;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public OrganizerEventsView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
        
        // Récupérer l'utilisateur depuis la session Vaadin
        Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
        
        if (userId == null) {
            Notification.show("Session expirée. Veuillez vous reconnecter.", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            UI.getCurrent().navigate("login");
            return;
        }
        
        this.currentUser = userService.findById(userId).orElse(null);
        
        if (currentUser == null) {
            Notification.show("Utilisateur non trouvé", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            UI.getCurrent().navigate("login");
            return;
        }
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        OrganizerSidebar sidebar = new OrganizerSidebar("organizer/events");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        // Header avec titre et bouton
        HorizontalLayout header = createHeader();
        
        // Filtre par statut
        HorizontalLayout filterLayout = createFilterLayout();
        
        // Grille des événements
        configureGrid();
        
        // Formulaire dans un dialog
        configureForm();
        
        mainContent.add(header, filterLayout, grid);
        add(sidebar, mainContent);
        
        updateGrid();
    }
    
    private HorizontalLayout createHeader() {
        H2 title = new H2("Mes Événements");
        title.getStyle()
            .set("color", "#333333")
            .set("margin", "0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        Button createButton = new Button("Créer un événement", new Icon(VaadinIcon.PLUS));
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.getStyle()
            .set("background-color", "#4CAF50")
            .set("border-radius", "8px");
        createButton.addClickListener(e -> openFormForNewEvent());
        
        HorizontalLayout header = new HorizontalLayout(title, createButton);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "20px");
        
        return header;
    }
    
    private HorizontalLayout createFilterLayout() {
        statusFilter = new ComboBox<>("Filtrer par statut");
        statusFilter.setItems(EventStatus.values());
        statusFilter.setItemLabelGenerator(EventStatus::getDisplayName);
        statusFilter.setPlaceholder("Tous les statuts");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("250px");
        
        statusFilter.addValueChangeListener(e -> updateGrid());
        
        HorizontalLayout filterLayout = new HorizontalLayout(statusFilter);
        filterLayout.getStyle().set("margin-bottom", "20px");
        
        return filterLayout;
    }
    
    private void configureGrid() {
        grid = new Grid<>(Event.class, false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        
        // Colonne Titre
        grid.addColumn(Event::getTitre)
            .setHeader("Titre")
            .setAutoWidth(true)
            .setFlexGrow(2);
        
        // Colonne Catégorie
        grid.addColumn(event -> event.getCategorie().getDisplayName())
            .setHeader("Catégorie")
            .setAutoWidth(true);
        
        // Colonne Date
        grid.addColumn(event -> event.getDateDebut().format(DATE_FORMATTER))
            .setHeader("Date de début")
            .setAutoWidth(true);
        
        // Colonne Lieu
        grid.addColumn(event -> event.getLieu() + ", " + event.getVille())
            .setHeader("Lieu")
            .setAutoWidth(true)
            .setFlexGrow(1);
        
        // Colonne Capacité
        grid.addComponentColumn(event -> {
            Div container = new Div();
            
            Span capacityText = new Span(event.getCapaciteMax() + " places");
            capacityText.getStyle().set("font-weight", "600");
            
            container.add(capacityText);
            return container;
        }).setHeader("Capacité").setAutoWidth(true);
        
        // Colonne Prix
        grid.addColumn(event -> String.format("%.2f DH", event.getPrixUnitaire()))
            .setHeader("Prix")
            .setAutoWidth(true);
        
        // Colonne Statut avec badge coloré
        grid.addComponentColumn(event -> createStatusBadge(event.getStatut()))
            .setHeader("Statut")
            .setAutoWidth(true);
        
        // Colonne Actions
        grid.addComponentColumn(event -> createActionsLayout(event))
            .setHeader("Actions")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        grid.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)");
    }
    
    private Span createStatusBadge(EventStatus status) {
        Span badge = new Span(status.getDisplayName());
        badge.getStyle()
            .set("padding", "4px 12px")
            .set("border-radius", "12px")
            .set("font-size", "12px")
            .set("font-weight", "600");
        
        switch (status) {
            case PUBLIE:
                badge.getStyle()
                    .set("background-color", "#e8f5e9")
                    .set("color", "#2e7d32");
                break;
            case BROUILLON:
                badge.getStyle()
                    .set("background-color", "#fff3e0")
                    .set("color", "#f57c00");
                break;
            case ANNULE:
                badge.getStyle()
                    .set("background-color", "#ffebee")
                    .set("color", "#c62828");
                break;
            case TERMINE:
                badge.getStyle()
                    .set("background-color", "#e0e0e0")
                    .set("color", "#616161");
                break;
        }
        
        return badge;
    }
    
    private HorizontalLayout createActionsLayout(Event event) {
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editButton.getElement().setAttribute("title", "Modifier");
        editButton.addClickListener(e -> openFormForEdit(event));
        
        Button publishButton = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
        publishButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
        publishButton.getElement().setAttribute("title", "Publier");
        publishButton.setVisible(event.getStatut() == EventStatus.BROUILLON);
        publishButton.addClickListener(e -> confirmPublish(event));
        
        Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        cancelButton.getElement().setAttribute("title", "Annuler");
        cancelButton.setVisible(event.getStatut() == EventStatus.PUBLIE);
        cancelButton.addClickListener(e -> confirmCancel(event));
        
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        deleteButton.getElement().setAttribute("title", "Supprimer");
        deleteButton.addClickListener(e -> confirmDelete(event));
        
        HorizontalLayout actions = new HorizontalLayout(editButton, publishButton, cancelButton, deleteButton);
        actions.setSpacing(false);
        
        return actions;
    }
    
    private void configureForm() {
        form = new EventForm();
        
        form.addListener(EventForm.SaveEvent.class, this::saveEvent);
        form.addListener(EventForm.CancelEvent.class, e -> closeFormDialog());
        
        formDialog = new Dialog();
        formDialog.setModal(true);
        formDialog.setDraggable(false);
        formDialog.setResizable(false);
        formDialog.setWidth("800px");
        formDialog.setMaxHeight("90vh");
        
        formDialog.add(form);
    }
    
    private void openFormForNewEvent() {
        Event newEvent = new Event();
        newEvent.setOrganisateur(currentUser);
        newEvent.setStatut(EventStatus.BROUILLON);
        
        form.setEvent(newEvent);
        formDialog.setHeaderTitle("Créer un nouvel événement");
        formDialog.open();
    }
    
    private void openFormForEdit(Event event) {
        form.setEvent(event);
        formDialog.setHeaderTitle("Modifier l'événement");
        formDialog.open();
    }
    
    private void saveEvent(EventForm.SaveEvent event) {
        try {
            Event eventToSave = event.getEvent();
            
            // Sauvegarder l'image si elle a été uploadée
            if (event.getFileName() != null && event.getFileStream() != null) {
                String imagePath = eventService.saveEventImage(event.getFileName(), event.getFileStream());
                eventToSave.setImagePath(imagePath);
            }
            
            // Créer ou mettre à jour l'événement
            if (eventToSave.getId() == null) {
                eventService.createEvent(eventToSave);
                Notification.show("Événement créé avec succès!", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                eventService.updateEvent(eventToSave);
                Notification.show("Événement modifié avec succès!", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
            
            closeFormDialog();
            updateGrid();
            
        } catch (Exception e) {
            Notification.show("Erreur: " + e.getMessage(), 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void confirmPublish(Event event) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Publier l'événement");
        dialog.setText("Voulez-vous vraiment publier cet événement ? Il sera visible par tous les clients.");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Annuler");
        
        dialog.setConfirmText("Publier");
        dialog.setConfirmButtonTheme("primary");
        
        dialog.addConfirmListener(e -> {
            try {
                eventService.publierEvent(event.getId(), currentUser);
                Notification.show("Événement publié avec succès!", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        dialog.open();
    }
    
    private void confirmCancel(Event event) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Annuler l'événement");
        dialog.setText("Voulez-vous vraiment annuler cet événement ? Cette action informera tous les participants.");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Retour");
        
        dialog.setConfirmText("Annuler l'événement");
        dialog.setConfirmButtonTheme("error primary");
        
        dialog.addConfirmListener(e -> {
            try {
                eventService.annulerEvent(event.getId(), currentUser);
                Notification.show("Événement annulé", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        dialog.open();
    }
    
    private void confirmDelete(Event event) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Supprimer l'événement");
        dialog.setText("Voulez-vous vraiment supprimer cet événement ? Cette action est irréversible.");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Annuler");
        
        dialog.setConfirmText("Supprimer");
        dialog.setConfirmButtonTheme("error primary");
        
        dialog.addConfirmListener(e -> {
            try {
                eventService.deleteEvent(event.getId(), currentUser);
                Notification.show("Événement supprimé", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        dialog.open();
    }
    
    private void closeFormDialog() {
        formDialog.close();
    }
    
    private void updateGrid() {
        List<Event> events;
        
        if (statusFilter.getValue() != null) {
            events = eventService.findByOrganisateurAndStatut(currentUser, statusFilter.getValue());
        } else {
            events = eventService.findByOrganisateur(currentUser);
        }
        
        grid.setItems(events);
        grid.setVisible(!events.isEmpty());
    }
}