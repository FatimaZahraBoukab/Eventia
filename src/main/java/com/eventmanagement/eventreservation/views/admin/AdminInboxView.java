package com.eventmanagement.eventreservation.views.admin;

import com.eventmanagement.eventreservation.entity.ContactMessage;
import com.eventmanagement.eventreservation.service.ContactMessageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Route("admin/inbox")
@RolesAllowed("ADMIN")
public class AdminInboxView extends Div {
    
    @Autowired
    private ContactMessageService contactMessageService;
    
    private Grid<ContactMessage> messageGrid;
    private TextField searchField;
    private Select<String> filterSelect;
    private Span unreadBadge;
    
    public AdminInboxView(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#f5f5f5");
        
        AdminSidebar sidebar = new AdminSidebar("admin/inbox");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "30px")
            .set("overflow-y", "auto");
        
        mainContent.add(createHeader(), createContent());
        add(sidebar, mainContent);
        
        loadMessages();
    }
    
    private Div createHeader() {
        Div header = new Div();
        header.getStyle().set("margin-bottom", "30px");
        
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthFull();
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        
        HorizontalLayout titleWithBadge = new HorizontalLayout();
        titleWithBadge.setAlignItems(FlexComponent.Alignment.CENTER);
        titleWithBadge.setSpacing(true);
        
        H2 title = new H2("Boîte de réception");
        title.getStyle()
            .set("color", "#333333")
            .set("margin", "0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        unreadBadge = new Span();
        updateUnreadBadge();
        
        titleWithBadge.add(title, unreadBadge);
        
        Button refreshButton = new Button("Actualiser", new Icon(VaadinIcon.REFRESH));
        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshButton.addClickListener(e -> loadMessages());
        
        titleLayout.add(titleWithBadge, refreshButton);
        
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.getStyle().set("margin-top", "20px");
        
        searchField = new TextField();
        searchField.setPlaceholder("Rechercher dans les messages...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("350px");
        searchField.addValueChangeListener(e -> performSearch());
        
        filterSelect = new Select<>();
        filterSelect.setPlaceholder("Filtrer");
        filterSelect.setItems("Tous", "Non lus", "Lus", "Important");
        filterSelect.setValue("Tous");
        filterSelect.setWidth("150px");
        filterSelect.addValueChangeListener(e -> applyFilter());
        
        Button deleteSelectedButton = new Button("Supprimer", new Icon(VaadinIcon.TRASH));
        deleteSelectedButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteSelectedButton.addClickListener(e -> deleteSelectedMessages());
        
        toolbar.add(searchField, filterSelect, deleteSelectedButton);
        header.add(titleLayout, toolbar);
        
        return header;
    }
    
    private void updateUnreadBadge() {
        long unreadCount = contactMessageService.countUnreadMessages();
        if (unreadCount > 0) {
            unreadBadge.setText(String.valueOf(unreadCount));
            unreadBadge.getStyle()
                .set("background", "#c9a961")
                .set("color", "white")
                .set("border-radius", "12px")
                .set("padding", "2px 10px")
                .set("font-size", "14px")
                .set("font-weight", "600");
            unreadBadge.setVisible(true);
        } else {
            unreadBadge.setVisible(false);
        }
    }
    
    private Div createContent() {
        Div content = new Div();
        content.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("overflow", "hidden");
        
        messageGrid = new Grid<>(ContactMessage.class, false);
        messageGrid.setHeight("calc(100vh - 300px)");
        messageGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        messageGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        
        // Colonne statut (lu/non lu)
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            Icon icon = message.isRead() 
                ? VaadinIcon.ENVELOPE_OPEN.create() 
                : VaadinIcon.ENVELOPE.create();
            icon.setSize("20px");
            icon.setColor(message.isRead() ? "#999999" : "#c9a961");
            return icon;
        })).setHeader("").setWidth("50px").setFlexGrow(0);
        
        // Colonne important (étoile)
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            Icon icon = VaadinIcon.STAR.create();
            icon.setSize("18px");
            icon.setColor(message.isImportant() ? "#c9a961" : "#e0e0e0");
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(e -> {
                contactMessageService.toggleImportant(message.getId());
                loadMessages();
            });
            return icon;
        })).setHeader("").setWidth("50px").setFlexGrow(0);
        
        // Colonne nom
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            Span name = new Span(message.getName());
            name.getStyle()
                .set("font-weight", message.isRead() ? "400" : "600")
                .set("color", "#333333");
            return name;
        })).setHeader("Nom").setWidth("180px").setFlexGrow(0);
        
        // Colonne email avec lien mailto
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            com.vaadin.flow.component.html.Anchor emailLink = 
                new com.vaadin.flow.component.html.Anchor("mailto:" + message.getEmail(), message.getEmail());
            emailLink.getStyle()
                .set("color", "#c9a961")
                .set("text-decoration", "none")
                .set("cursor", "pointer");
            emailLink.getElement().setAttribute("target", "_blank");
            return emailLink;
        })).setHeader("Email").setWidth("220px").setFlexGrow(0);
        
        // Colonne sujet
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            Span subject = new Span(message.getSubject());
            subject.getStyle()
                .set("font-weight", message.isRead() ? "400" : "600")
                .set("color", "#333333");
            return subject;
        })).setHeader("Sujet").setAutoWidth(true).setFlexGrow(1);
        
        // Colonne date
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Span date = new Span(message.getSentAt().format(formatter));
            date.getStyle()
                .set("font-size", "13px")
                .set("color", "#666666");
            return date;
        })).setHeader("Date").setWidth("140px").setFlexGrow(0);
        
        // Colonne actions
        messageGrid.addColumn(new ComponentRenderer<>(message -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);
            
            Button viewButton = new Button(new Icon(VaadinIcon.EYE));
            viewButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            viewButton.getStyle().set("color", "#c9a961");
            viewButton.addClickListener(e -> openMessageDialog(message));
            
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteMessage(message));
            
            actions.add(viewButton, deleteButton);
            return actions;
        })).setHeader("Actions").setWidth("120px").setFlexGrow(0);
        
        content.add(messageGrid);
        return content;
    }
    
    private void loadMessages() {
        List<ContactMessage> messages = contactMessageService.getAllMessages();
        if (messages.isEmpty()) {
            messageGrid.setItems();
            showEmptyState();
        } else {
            messageGrid.setItems(messages);
        }
        updateUnreadBadge();
    }
    
    private void performSearch() {
        String searchTerm = searchField.getValue();
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            List<ContactMessage> results = contactMessageService.searchMessages(searchTerm);
            messageGrid.setItems(results);
        } else {
            applyFilter();
        }
    }
    
    private void applyFilter() {
        String filter = filterSelect.getValue();
        List<ContactMessage> messages;
        
        switch (filter) {
            case "Non lus":
                messages = contactMessageService.getUnreadMessages();
                break;
            case "Lus":
                messages = contactMessageService.getReadMessages();
                break;
            case "Important":
                messages = contactMessageService.getImportantMessages();
                break;
            default:
                messages = contactMessageService.getAllMessages();
        }
        
        messageGrid.setItems(messages);
    }
    
    private void openMessageDialog(ContactMessage message) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        dialog.setCloseOnOutsideClick(false);
        
        // Marquer comme lu
        if (!message.isRead()) {
            contactMessageService.markAsRead(message.getId());
            loadMessages();
        }
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        
        // En-tête
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        
        H3 title = new H3("Détails du message");
        title.getStyle().set("margin", "0").set("color", "#333333");
        
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addClickListener(e -> dialog.close());
        
        header.add(title, closeButton);
        
        // Informations
        Div infoSection = new Div();
        infoSection.getStyle()
            .set("background", "#faf8f5")
            .set("padding", "20px")
            .set("border-radius", "8px")
            .set("margin", "10px 0");
        
        addInfoRow(infoSection, "De:", message.getName());
        
        // Email avec lien mailto
        HorizontalLayout emailRow = new HorizontalLayout();
        emailRow.setWidthFull();
        emailRow.setSpacing(true);
        emailRow.getStyle().set("margin-bottom", "8px");
        
        Span emailLabel = new Span("Email:");
        emailLabel.getStyle()
            .set("font-weight", "600")
            .set("color", "#333333")
            .set("min-width", "80px");
        
        com.vaadin.flow.component.html.Anchor emailLink = 
            new com.vaadin.flow.component.html.Anchor("mailto:" + message.getEmail(), message.getEmail());
        emailLink.getStyle()
            .set("color", "#c9a961")
            .set("text-decoration", "underline")
            .set("cursor", "pointer");
        emailLink.getElement().setAttribute("target", "_blank");
        
        emailRow.add(emailLabel, emailLink);
        infoSection.add(emailRow);
        
        addInfoRow(infoSection, "Sujet:", message.getSubject());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        addInfoRow(infoSection, "Date:", message.getSentAt().format(formatter));
        
        // Message
        Div messageContent = new Div();
        messageContent.getStyle()
            .set("background", "#ffffff")
            .set("border", "1px solid #e0e0e0")
            .set("border-radius", "8px")
            .set("padding", "20px")
            .set("margin", "10px 0")
            .set("line-height", "1.6")
            .set("color", "#333333");
        messageContent.setText(message.getMessage());
        
        // Actions
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        actions.setSpacing(true);
        
        Button markUnreadButton = new Button("Marquer non lu", new Icon(VaadinIcon.ENVELOPE));
        markUnreadButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        markUnreadButton.addClickListener(e -> {
            contactMessageService.markAsUnread(message.getId());
            loadMessages();
            dialog.close();
            Notification.show("Message marqué comme non lu", 2000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        
        Button deleteButton = new Button("Supprimer", new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            deleteMessage(message);
            dialog.close();
        });
        
        actions.add(markUnreadButton, deleteButton);
        
        layout.add(header, infoSection, new H3("Message"), messageContent, actions);
        dialog.add(layout);
        dialog.open();
    }
    
    private void addInfoRow(Div container, String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setSpacing(true);
        row.getStyle().set("margin-bottom", "8px");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-weight", "600")
            .set("color", "#333333")
            .set("min-width", "80px");
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("color", "#666666");
        
        row.add(labelSpan, valueSpan);
        container.add(row);
    }
    
    private void deleteMessage(ContactMessage message) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmer la suppression");
        dialog.setText("Êtes-vous sûr de vouloir supprimer ce message ? Cette action est irréversible.");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Annuler");
        
        dialog.setConfirmText("Supprimer");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            contactMessageService.deleteMessage(message.getId());
            loadMessages();
            Notification.show("Message supprimé avec succès", 2000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        
        dialog.open();
    }
    
    private void deleteSelectedMessages() {
        Set<ContactMessage> selectedMessages = messageGrid.getSelectedItems();
        
        if (selectedMessages.isEmpty()) {
            Notification.show("Veuillez sélectionner au moins un message", 2000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmer la suppression");
        dialog.setText("Êtes-vous sûr de vouloir supprimer " + selectedMessages.size() + " message(s) ?");
        
        dialog.setCancelable(true);
        dialog.setCancelText("Annuler");
        
        dialog.setConfirmText("Supprimer");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            selectedMessages.forEach(msg -> contactMessageService.deleteMessage(msg.getId()));
            messageGrid.deselectAll();
            loadMessages();
            Notification.show("Messages supprimés avec succès", 2000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        
        dialog.open();
    }
    
    private void showEmptyState() {
        // Cette méthode peut être implémentée pour afficher un état vide élégant
    }
}