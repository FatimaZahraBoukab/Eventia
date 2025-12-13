package com.eventmanagement.eventreservation.views.admin;

import com.eventmanagement.eventreservation.entity.Role;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("admin/users")
public class AdminUsersView extends Div {
    
    private final UserService userService;
    private Grid<User> grid;
    private TextField searchField;
    private ComboBox<Role> roleFilter;
    private ComboBox<String> statusFilter;
    
    public AdminUsersView(@Autowired UserService userService) {
        this.userService = userService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        AdminSidebar sidebar = new AdminSidebar("admin/users");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        mainContent.add(createHeader(), createFilters(), createUsersGrid());
        add(sidebar, mainContent);
        
        updateList();
    }
    
    private Div createHeader() {
        Div header = new Div();
        header.getStyle()
            .set("margin-bottom", "25px");
        
        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setWidthFull();
        titleRow.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        titleRow.setAlignItems(HorizontalLayout.Alignment.CENTER);
        
        H2 title = new H2("Gestion des Utilisateurs");
        title.getStyle()
            .set("color", "#2C2C2C")
            .set("margin", "0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        Button addButton = new Button("Nouvel utilisateur", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.getStyle()
            .set("background", "#C8A050")
            .set("border-radius", "10px")
            .set("padding", "10px 20px")
            .set("font-weight", "600");
        
        addButton.addClickListener(e -> openCreateUserDialog());
        
        titleRow.add(title, addButton);
        header.add(titleRow);
        
        return header;
    }
    
    private Div createFilters() {
        Div filtersContainer = new Div();
        filtersContainer.getStyle()
            .set("background", "#FFFFFF")
            .set("border-radius", "12px")
            .set("padding", "20px")
            .set("margin-bottom", "20px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("border", "1px solid #F0EDE8");
        
        HorizontalLayout filtersLayout = new HorizontalLayout();
        filtersLayout.setWidthFull();
        filtersLayout.setAlignItems(HorizontalLayout.Alignment.END);
        filtersLayout.getStyle().set("gap", "15px");
        
        // Champ de recherche
        searchField = new TextField();
        searchField.setPlaceholder("Rechercher par nom ou email...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("350px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> updateList());
        searchField.getStyle()
            .set("--lumo-border-radius", "10px");
        
        // Filtre par rôle
        roleFilter = new ComboBox<>("Rôle");
        roleFilter.setItems(Role.values());
        roleFilter.setItemLabelGenerator(Role::getDisplayName);
        roleFilter.setPlaceholder("Tous les rôles");
        roleFilter.setWidth("200px");
        roleFilter.setClearButtonVisible(true);
        roleFilter.addValueChangeListener(e -> updateList());
        roleFilter.getStyle()
            .set("--lumo-border-radius", "10px");
        
        // Filtre par statut
        statusFilter = new ComboBox<>("Statut");
        statusFilter.setItems("Tous", "Actif", "Inactif");
        statusFilter.setValue("Tous");
        statusFilter.setWidth("180px");
        statusFilter.addValueChangeListener(e -> updateList());
        statusFilter.getStyle()
            .set("--lumo-border-radius", "10px");
        
        // Bouton réinitialiser les filtres
        Button resetBtn = new Button("Réinitialiser", VaadinIcon.REFRESH.create());
        resetBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        resetBtn.getStyle()
            .set("border-radius", "10px");
        resetBtn.addClickListener(e -> {
            searchField.clear();
            roleFilter.clear();
            statusFilter.setValue("Tous");
            updateList();
        });
        
        filtersLayout.add(searchField, roleFilter, statusFilter, resetBtn);
        filtersContainer.add(filtersLayout);
        
        return filtersContainer;
    }
    
    private Div createUsersGrid() {
        Div gridContainer = new Div();
        gridContainer.getStyle()
            .set("background", "#FFFFFF")
            .set("border-radius", "12px")
            .set("padding", "20px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("border", "1px solid #F0EDE8");
        
        grid = new Grid<>(User.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("600px");
        
        // Colonne ID
        grid.addColumn(User::getId)
            .setHeader("ID")
            .setWidth("70px")
            .setFlexGrow(0)
            .setSortable(true);
        
        // Colonne Nom complet
        grid.addColumn(User::getFullName)
            .setHeader("Nom complet")
            .setAutoWidth(true)
            .setSortable(true);
        
        // Colonne Email
        grid.addColumn(User::getEmail)
            .setHeader("Email")
            .setAutoWidth(true)
            .setSortable(true);
        
        // Colonne Rôle avec badge
        grid.addComponentColumn(user -> {
            Span badge = new Span(user.getRole().getDisplayName());
            
            String color = switch (user.getRole()) {
                case ADMIN -> "#E74C3C";
                case ORGANIZER -> "#5B9BD5";
                case CLIENT -> "#70AD47";
            };
            
            badge.getStyle()
                .set("background", color + "15")
                .set("color", color)
                .set("padding", "6px 14px")
                .set("border-radius", "20px")
                .set("font-size", "12px")
                .set("font-weight", "600")
                .set("display", "inline-block");
            
            return badge;
        }).setHeader("Rôle").setWidth("140px").setSortable(true);
        
        // Colonne Statut avec badge
        grid.addComponentColumn(user -> {
            Span badge = new Span(user.getEnabled() ? "Actif" : "Inactif");
            badge.getStyle()
                .set("background", user.getEnabled() ? "#70AD4715" : "#E74C3C15")
                .set("color", user.getEnabled() ? "#70AD47" : "#E74C3C")
                .set("padding", "6px 14px")
                .set("border-radius", "20px")
                .set("font-size", "12px")
                .set("font-weight", "600")
                .set("display", "inline-block");
            
            return badge;
        }).setHeader("Statut").setWidth("120px").setSortable(true);
        
        // Colonne Date d'inscription
        grid.addColumn(user -> user.getCreatedAt().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )).setHeader("Date d'inscription")
          .setWidth("150px")
          .setSortable(true);
        
        // Colonne Actions
        grid.addComponentColumn(user -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);
            actions.getStyle().set("gap", "8px");
            
            // Bouton Voir détails
            Button viewBtn = new Button(VaadinIcon.EYE.create());
            viewBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            viewBtn.getStyle()
                .set("color", "#5B9BD5")
                .set("cursor", "pointer");
            viewBtn.setTooltipText("Voir les détails");
            viewBtn.addClickListener(e -> openUserDetailsDialog(user));
            
            // Bouton Changer rôle
            Button roleBtn = new Button(VaadinIcon.USER_CARD.create());
            roleBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            roleBtn.getStyle()
                .set("color", "#C8A050")
                .set("cursor", "pointer");
            roleBtn.setTooltipText("Changer le rôle");
            roleBtn.addClickListener(e -> openChangeRoleDialog(user));
            
            // Bouton Activer/Désactiver
            Button toggleBtn = new Button(
                user.getEnabled() ? VaadinIcon.BAN.create() : VaadinIcon.CHECK_CIRCLE.create()
            );
            toggleBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            toggleBtn.getStyle()
                .set("color", user.getEnabled() ? "#ED7D31" : "#70AD47")
                .set("cursor", "pointer");
            toggleBtn.setTooltipText(user.getEnabled() ? "Désactiver" : "Activer");
            toggleBtn.addClickListener(e -> toggleUserStatus(user));
            
            // Bouton Supprimer
            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            deleteBtn.getStyle()
                .set("color", "#E74C3C")
                .set("cursor", "pointer");
            deleteBtn.setTooltipText("Supprimer");
            deleteBtn.addClickListener(e -> confirmDelete(user));
            
            actions.add(viewBtn, roleBtn, toggleBtn, deleteBtn);
            return actions;
        }).setHeader("Actions").setWidth("200px").setFlexGrow(0);
        
        gridContainer.add(grid);
        return gridContainer;
    }
    
    private void updateList() {
        List<User> users = userService.findAll();
        
        // Filtre par recherche (nom ou email)
        String searchTerm = searchField.getValue();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            users = users.stream()
                .filter(user -> 
                    user.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .collect(Collectors.toList());
        }
        
        // Filtre par rôle
        if (roleFilter.getValue() != null) {
            users = users.stream()
                .filter(user -> user.getRole().equals(roleFilter.getValue()))
                .collect(Collectors.toList());
        }
        
        // Filtre par statut
        String status = statusFilter.getValue();
        if (!"Tous".equals(status)) {
            boolean enabled = "Actif".equals(status);
            users = users.stream()
                .filter(user -> user.getEnabled() == enabled)
                .collect(Collectors.toList());
        }
        
        grid.setItems(users);
    }
    
    // Dialog pour voir les détails d'un utilisateur
    private void openUserDetailsDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.getElement().getStyle()
            .set("border-radius", "16px");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        
        H3 title = new H3("Détails de l'utilisateur");
        title.getStyle()
            .set("color", "#2C2C2C")
            .set("margin", "0 0 20px 0");
        
        Div detailsContainer = new Div();
        detailsContainer.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "15px");
        
        detailsContainer.add(
            createDetailRow("ID", String.valueOf(user.getId())),
            createDetailRow("Nom complet", user.getFullName()),
            createDetailRow("Email", user.getEmail()),
            createDetailRow("Rôle", user.getRole().getDisplayName()),
            createDetailRow("Statut", user.getEnabled() ? "Actif" : "Inactif"),
            createDetailRow("Date d'inscription", 
                user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")))
        );
        
        Button closeBtn = new Button("Fermer", e -> dialog.close());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        closeBtn.getStyle().set("border-radius", "8px");
        
        layout.add(title, detailsContainer, closeBtn);
        dialog.add(layout);
        dialog.open();
    }
    
    private Div createDetailRow(String label, String value) {
        Div row = new Div();
        row.getStyle()
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("padding", "10px")
            .set("background", "#FAF9F7")
            .set("border-radius", "8px");
        
        Span labelSpan = new Span(label + ":");
        labelSpan.getStyle()
            .set("font-weight", "600")
            .set("color", "#6B6B6B");
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("color", "#2C2C2C");
        
        row.add(labelSpan, valueSpan);
        return row;
    }
    
    // Dialog pour changer le rôle
    private void openChangeRoleDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        
        H3 title = new H3("Changer le rôle");
        title.getStyle()
            .set("color", "#2C2C2C")
            .set("margin", "0 0 20px 0");
        
        Span currentRoleLabel = new Span("Rôle actuel: " + user.getRole().getDisplayName());
        currentRoleLabel.getStyle()
            .set("color", "#6B6B6B")
            .set("margin-bottom", "15px");
        
        ComboBox<Role> roleCombo = new ComboBox<>("Nouveau rôle");
        roleCombo.setItems(Role.values());
        roleCombo.setItemLabelGenerator(Role::getDisplayName);
        roleCombo.setValue(user.getRole());
        roleCombo.setWidthFull();
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        buttons.getStyle().set("margin-top", "20px");
        
        Button cancelBtn = new Button("Annuler", e -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        Button saveBtn = new Button("Enregistrer", e -> {
            user.setRole(roleCombo.getValue());
            userService.updateUser(user);
            updateList();
            dialog.close();
            showNotification("Rôle modifié avec succès", NotificationVariant.LUMO_SUCCESS);
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.getStyle().set("background", "#C8A050");
        
        buttons.add(cancelBtn, saveBtn);
        layout.add(title, currentRoleLabel, roleCombo, buttons);
        dialog.add(layout);
        dialog.open();
    }
    
    // Toggle statut utilisateur
    private void toggleUserStatus(User user) {
        String action = user.getEnabled() ? "désactiver" : "activer";
        
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Confirmer l'action");
        confirmDialog.setText("Voulez-vous vraiment " + action + " cet utilisateur ?");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Annuler");
        
        confirmDialog.setConfirmText("Confirmer");
        confirmDialog.setConfirmButtonTheme("primary");
        
        confirmDialog.addConfirmListener(e -> {
            userService.toggleUserStatus(user.getId());
            updateList();
            showNotification("Statut modifié avec succès", NotificationVariant.LUMO_SUCCESS);
        });
        
        confirmDialog.open();
    }
    
    // Confirmer suppression
    private void confirmDelete(User user) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Supprimer l'utilisateur");
        confirmDialog.setText("Êtes-vous sûr de vouloir supprimer " + user.getFullName() + " ? Cette action est irréversible.");
        
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Annuler");
        
        confirmDialog.setConfirmText("Supprimer");
        confirmDialog.setConfirmButtonTheme("error primary");
        
        confirmDialog.addConfirmListener(e -> {
            try {
                userService.deleteUser(user.getId());
                updateList();
                showNotification("Utilisateur supprimé avec succès", NotificationVariant.LUMO_SUCCESS);
            } catch (Exception ex) {
                showNotification("Erreur lors de la suppression", NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmDialog.open();
    }
    
    // Dialog pour créer un nouvel utilisateur
    private void openCreateUserDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        
        H3 title = new H3("Créer un nouvel utilisateur");
        title.getStyle()
            .set("color", "#2C2C2C")
            .set("margin", "0 0 20px 0");
        
        FormLayout formLayout = new FormLayout();
        
        TextField nameField = new TextField("Nom complet");
        nameField.setRequired(true);
        nameField.setWidthFull();
        
        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setWidthFull();
        
        PasswordField passwordField = new PasswordField("Mot de passe");
        passwordField.setRequired(true);
        passwordField.setWidthFull();
        
        ComboBox<Role> roleCombo = new ComboBox<>("Rôle");
        roleCombo.setItems(Role.values());
        roleCombo.setItemLabelGenerator(Role::getDisplayName);
        roleCombo.setValue(Role.CLIENT);
        roleCombo.setRequired(true);
        roleCombo.setWidthFull();
        
        formLayout.add(nameField, emailField, passwordField, roleCombo);
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        buttons.getStyle().set("margin-top", "20px");
        
        Button cancelBtn = new Button("Annuler", e -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        Button createBtn = new Button("Créer", e -> {
            try {
                Role selectedRole = roleCombo.getValue();
                switch (selectedRole) {
                    case ADMIN -> userService.createAdmin(nameField.getValue(), emailField.getValue(), passwordField.getValue());
                    case ORGANIZER -> userService.createOrganizer(nameField.getValue(), emailField.getValue(), passwordField.getValue());
                    case CLIENT -> userService.registerClient(nameField.getValue(), emailField.getValue(), passwordField.getValue());
                }
                updateList();
                dialog.close();
                showNotification("Utilisateur créé avec succès", NotificationVariant.LUMO_SUCCESS);
            } catch (Exception ex) {
                showNotification("Erreur: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBtn.getStyle().set("background", "#C8A050");
        
        buttons.add(cancelBtn, createBtn);
        layout.add(title, formLayout, buttons);
        dialog.add(layout);
        dialog.open();
    }
    
    private void showNotification(String text, NotificationVariant variant) {
        Notification notification = new Notification(text, 3000);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.TOP_END);
        notification.open();
    }
}