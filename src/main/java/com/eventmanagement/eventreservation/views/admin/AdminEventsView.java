package com.eventmanagement.eventreservation.views.admin;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventCategory;
import com.eventmanagement.eventreservation.entity.EventStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.UserService;
import com.eventmanagement.eventreservation.views.organizer.EventForm;
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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("admin/events")
public class AdminEventsView extends Div {
    
    private final EventService eventService;
    private final UserService userService;
    
    private User currentUser;
    private Grid<Event> grid;
    
    // Filtres
    private ComboBox<EventStatus> statusFilter;
    private ComboBox<EventCategory> categoryFilter;
    private TextField villeFilter;
    private TextField organizerFilter;
    private NumberField prixMinFilter;
    private NumberField prixMaxFilter;
    private Button clearFiltersButton;
    
    private EventForm form;
    private Dialog formDialog;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public AdminEventsView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
        
        try {
            // Récupérer l'utilisateur admin depuis la session
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
            
            AdminSidebar sidebar = new AdminSidebar("admin/events");
            
            Div mainContent = new Div();
            mainContent.getStyle()
                .set("flex", "1")
                .set("margin-left", "260px")
                .set("padding", "30px")
                .set("overflow-y", "auto");
            
            // Header
            HorizontalLayout header = createHeader();
            
            // Filtres
            Div filtersSection = createFiltersSection();
            
            // Grille des événements
            configureGrid();
            
            // Formulaire dans un dialog
            configureForm();
            
            mainContent.add(header, filtersSection, grid);
            add(sidebar, mainContent);
            
            updateGrid();
            
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Erreur lors du chargement de la page: " + e.getMessage(), 
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private HorizontalLayout createHeader() {
        H2 title = new H2("Gestion des Événements");
        title.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0")
            .set("font-size", "32px")
            .set("font-weight", "700");
        
        Span subtitle = new Span("Tous les événements de la plateforme");
        subtitle.getStyle()
            .set("color", "#7f8c8d")
            .set("font-size", "14px");
        
        Div titleSection = new Div(title, subtitle);
        
        HorizontalLayout header = new HorizontalLayout(titleSection);
        header.setWidthFull();
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
        
        HorizontalLayout filtersRow = new HorizontalLayout();
        filtersRow.setWidthFull();
        filtersRow.getStyle().set("gap", "15px");
        filtersRow.setAlignItems(FlexComponent.Alignment.END);
        
        // Filtre Statut
        statusFilter = new ComboBox<>("Statut");
        statusFilter.setItems(EventStatus.values());
        statusFilter.setItemLabelGenerator(EventStatus::getDisplayName);
        statusFilter.setPlaceholder("Tous les statuts");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("180px");
        statusFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre Catégorie
        categoryFilter = new ComboBox<>("Catégorie");
        categoryFilter.setItems(EventCategory.values());
        categoryFilter.setItemLabelGenerator(EventCategory::getDisplayName);
        categoryFilter.setPlaceholder("Toutes");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.setWidth("180px");
        categoryFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre Ville
        villeFilter = new TextField("Ville");
        villeFilter.setPlaceholder("Rechercher...");
        villeFilter.setClearButtonVisible(true);
        villeFilter.setWidth("200px");
        villeFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre Organisateur
        organizerFilter = new TextField("Organisateur");
        organizerFilter.setPlaceholder("Nom...");
        organizerFilter.setClearButtonVisible(true);
        organizerFilter.setWidth("200px");
        organizerFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre Prix Min
        prixMinFilter = new NumberField("Prix min (DH)");
        prixMinFilter.setPlaceholder("0");
        prixMinFilter.setClearButtonVisible(true);
        prixMinFilter.setWidth("140px");
        prixMinFilter.setMin(0);
        prixMinFilter.addValueChangeListener(e -> applyFilters());
        
        // Filtre Prix Max
        prixMaxFilter = new NumberField("Prix max (DH)");
        prixMaxFilter.setPlaceholder("10000");
        prixMaxFilter.setClearButtonVisible(true);
        prixMaxFilter.setWidth("140px");
        prixMaxFilter.setMin(0);
        prixMaxFilter.addValueChangeListener(e -> applyFilters());
        
        // Bouton Réinitialiser
        clearFiltersButton = new Button("Réinitialiser", new Icon(VaadinIcon.REFRESH));
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearFiltersButton.getStyle()
            .set("color", "#C8A050")
            .set("cursor", "pointer");
        clearFiltersButton.addClickListener(e -> clearAllFilters());
        
        filtersRow.add(statusFilter, categoryFilter, villeFilter, organizerFilter, 
                       prixMinFilter, prixMaxFilter, clearFiltersButton);
        
        filtersContainer.add(filtersRow);
        return filtersContainer;
    }
    
    private void applyFilters() {
        try {
            List<Event> events = eventService.findAll();
            
            // Filtre par statut
            if (statusFilter.getValue() != null) {
                events = events.stream()
                    .filter(e -> e.getStatut() == statusFilter.getValue())
                    .collect(Collectors.toList());
            }
            
            // Filtre par catégorie
            if (categoryFilter.getValue() != null) {
                events = events.stream()
                    .filter(e -> e.getCategorie() == categoryFilter.getValue())
                    .collect(Collectors.toList());
            }
            
            // Filtre par ville
            if (villeFilter.getValue() != null && !villeFilter.getValue().trim().isEmpty()) {
                String villeSearch = villeFilter.getValue().toLowerCase().trim();
                events = events.stream()
                    .filter(e -> e.getVille() != null && e.getVille().toLowerCase().contains(villeSearch))
                    .collect(Collectors.toList());
            }
            
            // Filtre par organisateur
            if (organizerFilter.getValue() != null && !organizerFilter.getValue().trim().isEmpty()) {
                String organizerSearch = organizerFilter.getValue().toLowerCase().trim();
                events = events.stream()
                    .filter(e -> e.getOrganisateur() != null && 
                           e.getOrganisateur().getFullName() != null &&
                           e.getOrganisateur().getFullName().toLowerCase().contains(organizerSearch))
                    .collect(Collectors.toList());
            }
            
            // Filtre par prix minimum
            if (prixMinFilter.getValue() != null) {
                double prixMin = prixMinFilter.getValue();
                events = events.stream()
                    .filter(e -> e.getPrixUnitaire() != null && e.getPrixUnitaire() >= prixMin)
                    .collect(Collectors.toList());
            }
            
            // Filtre par prix maximum
            if (prixMaxFilter.getValue() != null) {
                double prixMax = prixMaxFilter.getValue();
                events = events.stream()
                    .filter(e -> e.getPrixUnitaire() != null && e.getPrixUnitaire() <= prixMax)
                    .collect(Collectors.toList());
            }
            
            grid.setItems(events);
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Erreur lors de l'application des filtres: " + e.getMessage(), 
                3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void clearAllFilters() {
        statusFilter.clear();
        categoryFilter.clear();
        villeFilter.clear();
        organizerFilter.clear();
        prixMinFilter.clear();
        prixMaxFilter.clear();
        
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
            try {
                if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
                    Image img = new Image(event.getImagePath(), "Image");
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
                        .set("background", "#f5f5f5")
                        .set("border-radius", "8px")
                        .set("display", "flex")
                        .set("align-items", "center")
                        .set("justify-content", "center");
                    Span icon = new Span("🖼️");
                    icon.getStyle().set("font-size", "24px").set("opacity", "0.4");
                    emptyDiv.add(icon);
                    return emptyDiv;
                }
            } catch (Exception e) {
                return new Span("Error");
            }
        }).setHeader("Image").setWidth("100px").setFlexGrow(0);
        
        // Colonne ID Organisateur
        grid.addColumn(event -> {
            try {
                return event.getOrganisateur() != null ? event.getOrganisateur().getId() : "-";
            } catch (Exception e) {
                return "-";
            }
        }).setHeader("ID Org.").setAutoWidth(true).setFlexGrow(0);
        
       
        
        // Colonne Titre
        grid.addColumn(event -> event.getTitre() != null ? event.getTitre() : "-")
            .setHeader("Titre")
            .setAutoWidth(true)
            .setFlexGrow(2);
        
        // Colonne Ville
        grid.addColumn(event -> event.getVille() != null ? event.getVille() : "-")
            .setHeader("Ville")
            .setAutoWidth(true);
        
        // Colonne Prix
        grid.addComponentColumn(event -> {
            try {
                Span priceSpan = new Span(event.getPrixUnitaire() != null ? 
                    String.format("%.2f DH", event.getPrixUnitaire()) : "0.00 DH");
                priceSpan.getStyle()
                    .set("font-weight", "600")
                    .set("color", "#C8A050");
                return priceSpan;
            } catch (Exception e) {
                return new Span("-");
            }
        }).setHeader("Prix").setAutoWidth(true);
        
        // Colonne Date
        grid.addColumn(event -> {
            try {
                return event.getDateDebut() != null ? event.getDateDebut().format(DATE_FORMATTER) : "-";
            } catch (Exception e) {
                return "-";
            }
        }).setHeader("Date").setAutoWidth(true);
        
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
            .set("border", "1px solid #e8e8e8");
    }
    
    private Span createStatusBadge(EventStatus status) {
        Span badge = new Span(status != null ? status.getDisplayName() : "Inconnu");
        badge.getStyle()
            .set("padding", "6px 16px")
            .set("border-radius", "20px")
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("text-transform", "uppercase");
        
        if (status != null) {
            switch (status) {
                case PUBLIE:
                    badge.getStyle()
                        .set("background", "#d4edda")
                        .set("color", "#155724");
                    break;
                case BROUILLON:
                    badge.getStyle()
                        .set("background", "#fff3cd")
                        .set("color", "#856404");
                    break;
                case ANNULE:
                    badge.getStyle()
                        .set("background", "#f8d7da")
                        .set("color", "#721c24");
                    break;
                case TERMINE:
                    badge.getStyle()
                        .set("background", "#e2e3e5")
                        .set("color", "#383d41");
                    break;
            }
        }
        
        return badge;
    }
    
    private HorizontalLayout createActionsLayout(Event event) {
        Button viewButton = new Button(new Icon(VaadinIcon.EYE));
        viewButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        viewButton.getElement().setAttribute("title", "Voir");
        viewButton.getStyle().set("color", "#3498db");
        viewButton.addClickListener(e -> openFormForView(event));
        
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editButton.getElement().setAttribute("title", "Modifier");
        editButton.getStyle().set("color", "#C8A050");
        editButton.addClickListener(e -> openFormForEdit(event));
        
        Button publishButton = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
        publishButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        publishButton.getElement().setAttribute("title", "Publier");
        publishButton.setVisible(event.getStatut() == EventStatus.BROUILLON);
        publishButton.getStyle().set("color", "#28a745");
        publishButton.addClickListener(e -> confirmPublish(event));
        
        Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        cancelButton.getElement().setAttribute("title", "Annuler");
        cancelButton.setVisible(event.getStatut() == EventStatus.PUBLIE);
        cancelButton.getStyle().set("color", "#dc3545");
        cancelButton.addClickListener(e -> confirmCancel(event));
        
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        deleteButton.getElement().setAttribute("title", "Supprimer");
        deleteButton.getStyle().set("color", "#dc3545");
        deleteButton.addClickListener(e -> confirmDelete(event));
        
        HorizontalLayout actions = new HorizontalLayout(viewButton, editButton, publishButton, 
                                                         cancelButton, deleteButton);
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
    
    private void openFormForView(Event event) {
        form.setEvent(event);
        formDialog.setHeaderTitle("Détails de l'événement");
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
            eventService.updateEvent(eventToSave);
            
            Notification.show("Événement modifié avec succès!", 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            closeFormDialog();
            updateGrid();
            
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Erreur: " + e.getMessage(), 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void confirmPublish(Event event) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Publier l'événement");
        dialog.setText("Voulez-vous publier cet événement ?");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Annuler");
        
        dialog.setConfirmText("Publier");
        dialog.setConfirmButtonTheme("primary");
        
        dialog.addConfirmListener(e -> {
            try {
                event.setStatut(EventStatus.PUBLIE);
                eventService.updateEvent(event);
                
                Notification.show("Événement publié!", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                ex.printStackTrace();
                Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        dialog.open();
    }
    
    private void confirmCancel(Event event) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Annuler l'événement");
        dialog.setText("Voulez-vous annuler cet événement ?");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Retour");
        
        dialog.setConfirmText("Annuler l'événement");
        dialog.setConfirmButtonTheme("error primary");
        
        dialog.addConfirmListener(e -> {
            try {
                event.setStatut(EventStatus.ANNULE);
                eventService.updateEvent(event);
                
                Notification.show("Événement annulé", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                ex.printStackTrace();
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
                eventService.deleteEvent(event.getId(), event.getOrganisateur());
                
                Notification.show("Événement supprimé", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (Exception ex) {
                ex.printStackTrace();
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
        try {
            List<Event> events = eventService.findAll();
            grid.setItems(events);
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Erreur lors du chargement des événements: " + e.getMessage(), 
                3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}