package com.eventmanagement.eventreservation.views;

import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@SuppressWarnings("unused")
@Route("register")
public class RegisterView extends Div {
    
    private final UserService userService;
    
    public RegisterView(UserService userService) {
        this.userService = userService;
        
        setWidthFull();
        setHeightFull();
        getStyle()
            .set("background-image", "url('images/hero1.webp')")  // votre image ici
             .set("background-size", "cover")
             .set("background-position", "center")
             .set("background-repeat", "no-repeat")
             .set("display", "flex")
             .set("align-items", "center")
             .set("justify-content", "center")
             .set("padding", "0")
             .set("margin", "0")
             .set("position", "fixed")
             .set("top", "0")
             .set("left", "0")
             .set("overflow", "hidden");
        
        // Formes décoratives arrière-plan
        Div decorativeShapes = new Div();
        decorativeShapes.getStyle()
            .set("position", "absolute")
            .set("width", "100%")
            .set("height", "100%")
            .set("overflow", "hidden")
            .set("pointer-events", "none");
        
        // Grande forme en haut gauche
        Div shape1 = new Div();
        shape1.getStyle()
            .set("position", "absolute")
            .set("width", "400px")
            .set("height", "400px")
            .set("background", "rgba(255, 255, 255, 0.1)")
            .set("border-radius", "50%")
            .set("top", "-150px")
            .set("left", "-100px")
            .set("filter", "blur(40px)");
        
        // Forme moyenne en bas
        Div shape2 = new Div();
        shape2.getStyle()
            .set("position", "absolute")
            .set("width", "300px")
            .set("height", "300px")
            .set("background", "rgba(255, 255, 255, 0.15)")
            .set("border-radius", "50%")
            .set("bottom", "-100px")
            .set("left", "50px")
            .set("filter", "blur(50px)");
        
        // Petite forme à droite
        Div shape3 = new Div();
        shape3.getStyle()
            .set("position", "absolute")
            .set("width", "200px")
            .set("height", "200px")
            .set("background", "rgba(255, 255, 255, 0.12)")
            .set("border-radius", "50%")
            .set("top", "30%")
            .set("right", "-50px")
            .set("filter", "blur(45px)");
        
        decorativeShapes.add(shape1, shape2, shape3);
        
        // Card principal
        Div card = new Div();
        card.getStyle()
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("backdrop-filter", "blur(10px)")
            .set("padding", "45px 40px")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.2)")
            .set("width", "420px")
            .set("position", "relative")
            .set("z-index", "10");
        
        // Titre
        H2 title = new H2("Créer un compte");
        title.getStyle()
            .set("text-align", "center")
            .set("color", "#333333")
            .set("font-size", "1.8rem")
            .set("font-weight", "700")
            .set("margin", "0 0 30px 0");
        
        // Formulaire
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(false);
        form.getStyle().set("gap", "16px");
        
        // Champ Nom complet
        TextField fullNameField = new TextField();
        fullNameField.setPlaceholder("Nom complet");
        fullNameField.setWidthFull();
        fullNameField.getStyle()
            .set("--lumo-border-radius", "25px")
            .set("background-color", "#F0F0F0")
            .set("border", "none")
            .set("border-radius","15px")
            .set("padding-left", "5px")
            .set("--vaadin-input-field-background", "#F0F0F0")
            .set("--vaadin-input-field-border-radius", "15px");
        
        fullNameField.getElement().executeJs(
            "this.shadowRoot.querySelector('[part=\"input-field\"]').style.paddingLeft = '40px';"
        );
        
        // Champ Email
        EmailField emailField = new EmailField();
        emailField.setPlaceholder("Email");
        emailField.setWidthFull();
        emailField.getStyle()
            .set("--lumo-border-radius", "25px")
            .set("background-color", "#F0F0F0")
            .set("border", "none")
            .set("border-radius", "15px")
            .set("padding-left", "5px")
            .set("--vaadin-input-field-background", "#F0F0F0")
            .set("--vaadin-input-field-border-radius", "15px");
        
        emailField.getElement().executeJs(
            "this.shadowRoot.querySelector('[part=\"input-field\"]').style.paddingLeft = '40px';"
        );
        
        // Champ Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPlaceholder("Mot de passe");
        passwordField.setWidthFull();
        passwordField.getStyle()
            .set("--lumo-border-radius", "25px")
            .set("background-color", "#F0F0F0")
            .set("border", "none")
            .set("border-radius", "15px")
            .set("padding-left", "5px")
            .set("--vaadin-input-field-background", "#F0F0F0")
            .set("--vaadin-input-field-border-radius", "15px");
        
        passwordField.getElement().executeJs(
            "this.shadowRoot.querySelector('[part=\"input-field\"]').style.paddingLeft = '40px';"
        );
        
        // Champ Confirmer Password
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPlaceholder("Confirmer le mot de passe");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.getStyle()
            .set("--lumo-border-radius", "25px")
            .set("background-color", "#F0F0F0")
            .set("border", "none")
            .set("border-radius", "15px")
            .set("padding-left", "5px")
            .set("--vaadin-input-field-background", "#F0F0F0")
            .set("--vaadin-input-field-border-radius", "15px");
        
        confirmPasswordField.getElement().executeJs(
            "this.shadowRoot.querySelector('[part=\"input-field\"]').style.paddingLeft = '40px';"
        );
        
        // Bouton Register
        Button registerButton = new Button("S'inscrire");
        registerButton.setWidthFull();
        registerButton.getStyle()
           .set("background", "linear-gradient(90deg, #c9a961 0%, #c9a961 50%, #c9a961 100%)")
            .set("color", "#FFFFFF")
            .set("border", "none")
            .set("padding", "14px")
            .set("font-size", "1rem")
            .set("font-weight", "700")
            .set("border-radius", "25px")
            .set("cursor", "pointer")
            .set("margin-top", "8px")
            .set("transition", "all 0.3s ease")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "1px")
            .set("box-shadow", "0 4px 15px rgba(155, 89, 182, 0.3)");
        
        registerButton.getElement().addEventListener("mouseenter", e -> {
            registerButton.getStyle()
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 6px 20px #c9a961");
        });
        
        registerButton.getElement().addEventListener("mouseleave", e -> {
            registerButton.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 15px c9a961)");
        });
        
        registerButton.addClickListener(e -> {
            String fullName = fullNameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();
            
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showNotification("Veuillez remplir tous les champs", NotificationVariant.LUMO_ERROR);
                return;
            }
            
            if (password.length() < 6) {
                showNotification("Le mot de passe doit contenir au moins 6 caractères", NotificationVariant.LUMO_ERROR);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showNotification("Les mots de passe ne correspondent pas", NotificationVariant.LUMO_ERROR);
                return;
            }
            
            try {
                User user = userService.registerClient(fullName, email, password);
                showNotification("✓ Compte créé avec succès ! Redirection...", NotificationVariant.LUMO_SUCCESS);
                
                UI.getCurrent().getPage().executeJs(
                    "setTimeout(function() { window.location.href = '/login'; }, 1500);"
                );
                
            } catch (RuntimeException ex) {
                showNotification(ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        
        // Lien connexion
        Div loginLinkDiv = new Div();
        loginLinkDiv.getStyle()
            .set("text-align", "center")
            .set("margin-top", "25px");
        
        Span loginText = new Span("Vous avez déjà un compte ? ");
        loginText.getStyle()
            .set("color", "#666666")
            .set("font-size", "0.9rem");
        
        RouterLink loginLink = new RouterLink("Connexion", LoginView.class);
        loginLink.getStyle()
            .set("color", "#C8A050")
            .set("text-decoration", "none")
            .set("font-weight", "600")
            .set("transition", "color 0.3s ease");
        
        loginLink.getElement().addEventListener("mouseenter", e -> {
            loginLink.getStyle().set("color", "#D4AF6A");
        });
        
        loginLink.getElement().addEventListener("mouseleave", e -> {
            loginLink.getStyle().set("color", "#C8A050");
        });
        
        loginLinkDiv.add(loginText, loginLink);
        
        form.add(fullNameField, emailField, passwordField, confirmPasswordField, registerButton, loginLinkDiv);
        card.add(title, form);
        add(decorativeShapes, card);
    }
    
    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }
}