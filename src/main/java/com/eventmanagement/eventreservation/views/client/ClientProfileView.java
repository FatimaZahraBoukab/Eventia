package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("client/profile")
@RolesAllowed("CLIENT")
public class ClientProfileView extends Div {
    
    private final UserService userService;
    private User currentUser;
    
    private TextField nameField;
    private EmailField emailField;
    private PasswordField currentPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    
    public ClientProfileView(@Autowired UserService userService) {
        this.userService = userService;
        this.currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        ClientSidebar sidebar = new ClientSidebar("client/profile");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
   
        
        Div contentContainer = new Div();
        contentContainer.getStyle().set("padding", "30px");
        
        H2 pageTitle = new H2("Mon Profil");
        pageTitle.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 30px 0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        HorizontalLayout profileLayout = new HorizontalLayout();
        profileLayout.setWidthFull();
        profileLayout.getStyle().set("gap", "30px");
        
        profileLayout.add(createProfileCard(), createSecurityCard());
        
        contentContainer.add(pageTitle, profileLayout);
        mainContent.add( contentContainer);
        add(sidebar, mainContent);
    }
    
    
    private Div createProfileCard() {
        Div card = new Div();
        card.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "30px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("width", "400px");
        
        Div avatarSection = new Div();
        avatarSection.getStyle()
            .set("text-align", "center")
            .set("margin-bottom", "30px");
        
        Div avatar = new Div();
        avatar.getStyle()
            .set("width", "120px")
            .set("height", "120px")
            .set("border-radius", "50%")
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4B068 100%)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("margin", "0 auto 15px auto")
            .set("font-size", "48px")
            .set("color", "#ffffff")
            .set("font-weight", "700");
        
        String initials = currentUser != null && currentUser.getFullName() != null ? 
            currentUser.getFullName().substring(0, 1).toUpperCase() : "C";
        avatar.add(new Span(initials));
        
        H3 name = new H3(currentUser != null ? currentUser.getFullName() : "Client");
        name.getStyle()
            .set("margin", "0 0 5px 0")
            .set("color", "#333333")
            .set("font-size", "22px");
        
        Span role = new Span("Client");
        role.getStyle()
            .set("color", "#C8A050")
            .set("font-size", "14px")
            .set("font-weight", "600");
        
        avatarSection.add(avatar, name, role);
        
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(false);
        form.getStyle().set("gap", "15px");
        
        nameField = new TextField("Nom complet");
        nameField.setWidthFull();
        nameField.setValue(currentUser != null ? currentUser.getFullName() : "");
        nameField.getStyle().set("--lumo-border-radius", "8px");
        
        emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setValue(currentUser != null ? currentUser.getEmail() : "");
        emailField.getStyle().set("--lumo-border-radius", "8px");
        
        Button saveButton = new Button("Enregistrer les modifications");
        saveButton.setWidthFull();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle()
            .set("background", "#C8A050")
            .set("margin-top", "10px")
            .set("border-radius", "8px");
        
        saveButton.addClickListener(e -> updateProfile());
        
        form.add(nameField, emailField, saveButton);
        
        card.add(avatarSection, form);
        return card;
    }
    
    private Div createSecurityCard() {
        Div card = new Div();
        card.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "30px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("flex", "1");
        
        H3 title = new H3("Sécurité");
        title.getStyle()
            .set("margin", "0 0 25px 0")
            .set("color", "#333333")
            .set("font-size", "20px");
        
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(false);
        form.getStyle().set("gap", "15px");
        
        currentPasswordField = new PasswordField("Mot de passe actuel");
        currentPasswordField.setWidthFull();
        currentPasswordField.getStyle().set("--lumo-border-radius", "8px");
        
        newPasswordField = new PasswordField("Nouveau mot de passe");
        newPasswordField.setWidthFull();
        newPasswordField.getStyle().set("--lumo-border-radius", "8px");
        
        confirmPasswordField = new PasswordField("Confirmer le mot de passe");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.getStyle().set("--lumo-border-radius", "8px");
        
        Button changePasswordButton = new Button("Changer le mot de passe");
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.getStyle()
            .set("background", "#C8A050")
            .set("margin-top", "10px")
            .set("border-radius", "8px");
        
        changePasswordButton.addClickListener(e -> changePassword());
        
        form.add(currentPasswordField, newPasswordField, confirmPasswordField, changePasswordButton);
        
        Div infoBox = new Div();
        infoBox.getStyle()
            .set("background", "#f8f9fa")
            .set("border-left", "4px solid #C8A050")
            .set("padding", "15px")
            .set("border-radius", "5px")
            .set("margin-top", "30px");
        
        Span infoTitle = new Span("💡 Conseil de sécurité");
        infoTitle.getStyle()
            .set("display", "block")
            .set("font-weight", "600")
            .set("color", "#333333")
            .set("margin-bottom", "5px");
        
        Span infoText = new Span("Utilisez un mot de passe fort contenant au moins 8 caractères.");
        infoText.getStyle()
            .set("color", "#666666")
            .set("font-size", "14px")
            .set("line-height", "1.5");
        
        infoBox.add(infoTitle, infoText);
        
        card.add(title, form, infoBox);
        return card;
    }
    
    private void updateProfile() {
        String newName = nameField.getValue().trim();
        String newEmail = emailField.getValue().trim();
        
        if (newName.isEmpty() || newEmail.isEmpty()) {
            showNotification("Tous les champs sont obligatoires", NotificationVariant.LUMO_ERROR);
            return;
        }
        
        if (!newEmail.equals(currentUser.getEmail()) && userService.findByEmail(newEmail).isPresent()) {
            showNotification("Cet email est déjà utilisé", NotificationVariant.LUMO_ERROR);
            return;
        }
        
        try {
            currentUser.setFullName(newName);
            currentUser.setEmail(newEmail);
            User updatedUser = userService.updateUser(currentUser);
            VaadinSession.getCurrent().setAttribute(User.class, updatedUser);
            currentUser = updatedUser;
            showNotification("Profil mis à jour avec succès !", NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        } catch (Exception e) {
            showNotification("Erreur : " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void changePassword() {
        String currentPassword = currentPasswordField.getValue();
        String newPassword = newPasswordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showNotification("Tous les champs sont obligatoires", NotificationVariant.LUMO_ERROR);
            return;
        }
        
        if (newPassword.length() < 8) {
            showNotification("Le mot de passe doit contenir au moins 8 caractères", NotificationVariant.LUMO_ERROR);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showNotification("Les mots de passe ne correspondent pas", NotificationVariant.LUMO_ERROR);
            return;
        }
        
        try {
            boolean success = userService.changePassword(currentUser.getId(), currentPassword, newPassword);
            if (success) {
                showNotification("Mot de passe changé avec succès !", NotificationVariant.LUMO_SUCCESS);
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                showNotification("Mot de passe actuel incorrect", NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            showNotification("Erreur : " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }
}