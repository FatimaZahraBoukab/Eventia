package com.eventmanagement.eventreservation.views.organizer;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventCategory;
import com.eventmanagement.eventreservation.entity.EventStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("organizer/events")
@RolesAllowed("ORGANIZER")
public class OrganizerEventsView extends Div {
    
    private final EventService eventService;
    private final UserService userService;
    
    private User currentUser;
    private Grid<Event> grid;
    
    // Filtres multiples
    private ComboBox<EventStatus> statusFilter;
    private ComboBox<EventCategory> categoryFilter;
    private TextField lieuFilter;
    private TextField villeFilter;
    private NumberField prixMinFilter;
    private NumberField prixMaxFilter;
    private DatePicker dateDebutFilter;
    private DatePicker dateFinFilter;
    private Button clearFiltersButton;
    
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
        
        // Filtres multiples
        Div filtersSection = createFiltersSection();
        
        // Grille des événements
        configureGrid();
        
        // Formulaire dans un dialog
        configureForm();
        
        mainContent.add(header, filtersSection, grid);
        add(sidebar, mainContent);
        
        updateGrid();
    }
    
    private HorizontalLayout createHeader() {
        H2 title = new H2("Mes Événements");
        title.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0")
            .set("font-size", "32px")
            .set("font-weight", "700")
            .set("letter-spacing", "-0.5px");
        
        Button createButton = new Button("Créer un événement", new Icon(VaadinIcon.PLUS));
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
            .set("color", "#FFFFFF")
            .set("border", "none")
            .set("border-radius", "25px")
            .set("padding", "12px 24px")
            .set("font-weight", "600")
            .set("box-shadow", "0 4px 15px rgba(200, 160, 80, 0.3)")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease");
        
        createButton.getElement().addEventListener("mouseenter", e -> {
            createButton.getStyle()
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 6px 20px rgba(200, 160, 80, 0.4)");
        });
        
        createButton.getElement().addEventListener("mouseleave", e -> {
            createButton.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 15px rgba(200, 160, 80, 0.3)");
        });
        
        createButton.addClickListener(e -> openFormForNewEvent());
        
        HorizontalLayout header = new HorizontalLayout(title, createButton);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "20px");
        
        return header;
    }
    
    private Div createFiltersSection() {
        Div filtersContainer = new Div();
        filtersContainer.getStyle()
            .set("background", "#ffffff")
            .set("padding", "25px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("border", "1px solid #e8e8e8")
            .set("margin-bottom", "20px");
        
    
        
        // Ligne 1: Statut, Catégorie, ville
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setWidthFull();
        row1.getStyle().set("gap", "15px");
        
        statusFilter = new ComboBox<>("Statut");
        statusFilter.setItems(EventStatus.values());
        statusFilter.setItemLabelGenerator(EventStatus::getDisplayName);
        statusFilter.setPlaceholder("Tous les statuts");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("200px");
        statusFilter.getStyle().set("--lumo-primary-color", "#C8A050");
        statusFilter.addValueChangeListener(e -> applyFilters());
        
        categoryFilter = new ComboBox<>("Catégorie");
        categoryFilter.setItems(EventCategory.values());
        categoryFilter.setItemLabelGenerator(EventCategory::getDisplayName);
        categoryFilter.setPlaceholder("Toutes les catégories");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.setWidth("200px");
        categoryFilter.getStyle().set("--lumo-primary-color", "#C8A050");
        categoryFilter.addValueChangeListener(e -> applyFilters());

        villeFilter = new TextField("Ville");
        villeFilter.setPlaceholder("Rechercher par ville...");
        villeFilter.setClearButtonVisible(true);
        villeFilter.setWidth("250px");
        villeFilter.getStyle().set("--lumo-primary-color", "#C8A050");
        villeFilter.addValueChangeListener(e -> applyFilters());
        
 prixMinFilter = new NumberField("Prix min (DH)");
        prixMinFilter.setPlaceholder("0");
        prixMinFilter.setClearButtonVisible(true);
        prixMinFilter.setWidth("150px");
        prixMinFilter.setMin(0);
        prixMinFilter.getStyle().set("--lumo-primary-color", "#C8A050");
        prixMinFilter.addValueChangeListener(e -> applyFilters());
        
        prixMaxFilter = new NumberField("Prix max (DH)");
        prixMaxFilter.setPlaceholder("10000");
        prixMaxFilter.setClearButtonVisible(true);
        prixMaxFilter.setWidth("150px");
        prixMaxFilter.setMin(0);
        prixMaxFilter.getStyle().set("--lumo-primary-color", "#C8A050");
        prixMaxFilter.addValueChangeListener(e -> applyFilters());

        // Bouton pour effacer tous les filtres
        clearFiltersButton = new Button("Réinitialiser les filtres", new Icon(VaadinIcon.REFRESH));
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearFiltersButton.getStyle()
            .set("color", "#C8A050")
            .set("cursor", "pointer")
            .set("margin-left", "auto");
        clearFiltersButton.addClickListener(e -> clearAllFilters());
        
        row1.add(statusFilter, categoryFilter, villeFilter , prixMinFilter ,prixMaxFilter ,clearFiltersButton);
        
       
    
        
        filtersContainer.add( row1 );
        return filtersContainer;
    }
    
    private void applyFilters() {
        List<Event> events = eventService.findByOrganisateur(currentUser);
        
        // Filtrer par statut
        if (statusFilter.getValue() != null) {
            events = events.stream()
                .filter(e -> e.getStatut() == statusFilter.getValue())
                .collect(Collectors.toList());
        }
        
        // Filtrer par catégorie
        if (categoryFilter.getValue() != null) {
            events = events.stream()
                .filter(e -> e.getCategorie() == categoryFilter.getValue())
                .collect(Collectors.toList());
        }
        
       
        
        // Filtrer par ville
        if (villeFilter.getValue() != null && !villeFilter.getValue().trim().isEmpty()) {
            String villeSearch = villeFilter.getValue().toLowerCase().trim();
            events = events.stream()
                .filter(e -> e.getVille().toLowerCase().contains(villeSearch))
                .collect(Collectors.toList());
        }
        
        // Filtrer par prix minimum
        if (prixMinFilter.getValue() != null) {
            double prixMin = prixMinFilter.getValue();
            events = events.stream()
                .filter(e -> e.getPrixUnitaire() >= prixMin)
                .collect(Collectors.toList());
        }
        
        // Filtrer par prix maximum
        if (prixMaxFilter.getValue() != null) {
            double prixMax = prixMaxFilter.getValue();
            events = events.stream()
                .filter(e -> e.getPrixUnitaire() <= prixMax)
                .collect(Collectors.toList());
        
        grid.setItems(events);
        grid.setVisible(!events.isEmpty()); }
    }
    
    private void clearAllFilters() {
        statusFilter.clear();
        categoryFilter.clear();
        lieuFilter.clear();
        villeFilter.clear();
        prixMinFilter.clear();
        prixMaxFilter.clear();
        dateDebutFilter.clear();
        dateFinFilter.clear();
        
        updateGrid();
        
        Notification.show("Filtres réinitialisés", 2000, Notification.Position.BOTTOM_START)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    
    private void configureGrid() {
        grid = new Grid<>(Event.class, false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setHeight("auto");
        grid.setAllRowsVisible(true);
        
        // Colonne Image
        grid.addComponentColumn(event -> {
            if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
                Image img = new Image(event.getImagePath(), "Image événement");
                img.setWidth("80px");
                img.setHeight("60px");
                img.getStyle()
                    .set("object-fit", "cover")
                    .set("border-radius", "8px")
                    .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
                return img;
            } else {
                Div emptyDiv = new Div();
                emptyDiv.setWidth("80px");
                emptyDiv.setHeight("60px");
                emptyDiv.getStyle()
                    .set("background", "linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 100%)")
                    .set("border-radius", "8px")
                    .set("display", "flex")
                    .set("align-items", "center")
                    .set("justify-content", "center")
                    .set("border", "2px dashed #C8A050");
                
                Span noImageIcon = new Span("🖼️");
                noImageIcon.getStyle()
                    .set("font-size", "24px")
                    .set("opacity", "0.4");
                
                emptyDiv.add(noImageIcon);
                return emptyDiv;
            }
        }).setHeader("Image").setWidth("100px").setFlexGrow(0);
        
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
            capacityText.getStyle()
                .set("font-weight", "600")
                .set("color", "#555555");
            
            container.add(capacityText);
            return container;
        }).setHeader("Capacité").setAutoWidth(true);
        
        // Colonne Prix
        grid.addComponentColumn(event -> {
            Span priceSpan = new Span(String.format("%.2f DH", event.getPrixUnitaire()));
            priceSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "#C8A050");
            return priceSpan;
        }).setHeader("Prix").setAutoWidth(true);
        
        // Colonne Statut
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
            .set("border-radius", "16px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("border", "1px solid #e8e8e8")
            .set("overflow", "visible");
    }
    
    private Span createStatusBadge(EventStatus status) {
        Span badge = new Span(status.getDisplayName());
        badge.getStyle()
            .set("padding", "6px 16px")
            .set("border-radius", "20px")
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "0.5px");
        
        switch (status) {
            case PUBLIE:
                badge.getStyle()
                    .set("background", "linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%)")
                    .set("color", "#155724")
                    .set("border", "1px solid #c3e6cb");
                break;
            case BROUILLON:
                badge.getStyle()
                    .set("background", "linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%)")
                    .set("color", "#856404")
                    .set("border", "1px solid #ffeaa7");
                break;
            case ANNULE:
                badge.getStyle()
                    .set("background", "linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%)")
                    .set("color", "#721c24")
                    .set("border", "1px solid #f5c6cb");
                break;
            case TERMINE:
                badge.getStyle()
                    .set("background", "linear-gradient(135deg, #e2e3e5 0%, #d6d8db 100%)")
                    .set("color", "#383d41")
                    .set("border", "1px solid #d6d8db");
                break;
        }
        
        return badge;
    }
    
    private HorizontalLayout createActionsLayout(Event event) {
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editButton.getElement().setAttribute("title", "Modifier");
        editButton.getStyle()
            .set("color", "#C8A050")
            .set("cursor", "pointer");
        editButton.addClickListener(e -> openFormForEdit(event));
        
        Button publishButton = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
        publishButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        publishButton.getElement().setAttribute("title", "Publier");
        publishButton.setVisible(event.getStatut() == EventStatus.BROUILLON);
        publishButton.getStyle()
            .set("color", "#28a745")
            .set("cursor", "pointer");
        publishButton.addClickListener(e -> confirmPublish(event));
        
        Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        cancelButton.getElement().setAttribute("title", "Annuler");
        cancelButton.setVisible(event.getStatut() == EventStatus.PUBLIE);
        cancelButton.getStyle()
            .set("color", "#dc3545")
            .set("cursor", "pointer");
        cancelButton.addClickListener(e -> confirmCancel(event));
        
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        deleteButton.getElement().setAttribute("title", "Supprimer");
        deleteButton.getStyle()
            .set("color", "#dc3545")
            .set("cursor", "pointer");
        deleteButton.addClickListener(e -> confirmDelete(event));
        
        HorizontalLayout actions = new HorizontalLayout(editButton, publishButton, cancelButton, deleteButton);
        actions.setSpacing(false);
        actions.getStyle().set("gap", "8px");
        
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
        List<Event> events = eventService.findByOrganisateur(currentUser);
        grid.setItems(events);
        grid.setVisible(!events.isEmpty());
    }
}