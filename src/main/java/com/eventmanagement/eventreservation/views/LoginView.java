package com.eventmanagement.eventreservation.views;

import com.eventmanagement.eventreservation.entity.Role;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

@SuppressWarnings("unused")
@Route("login")
public class LoginView extends Div {
    
    private final UserService userService;
    
    public LoginView(UserService userService) {
        this.userService = userService;
        
        setWidthFull();
        setHeightFull();
        getStyle()
             .set("background-image", "url('images/hero1.webp')")
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
        
        // Carte principale
        Div card = new Div();
        card.getStyle()
            .set("background", "rgba(255, 255, 255, 0.95)")
            .set("backdrop-filter", "blur(10px)")
            .set("padding", "50px 45px")
            .set("border-radius", "20px")
            .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.2)")
            .set("width", "420px")
            .set("position", "relative")
            .set("z-index", "10");
        
        // Titre
        H2 title = new H2("Connexion à votre compte");
        title.getStyle()
            .set("text-align", "center")
            .set("color", "#333333")
            .set("font-size", "1.8rem")
            .set("font-weight", "700")
            .set("margin", "0 0 35px 0");
        
        // Formulaire
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(false);
        form.getStyle().set("gap", "18px");
        
        // Champ Email / Nom d'utilisateur
        EmailField emailField = new EmailField();
        emailField.setPlaceholder("Nom d'utilisateur");
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
        
        // Bouton de connexion
        Button loginButton = new Button("SE CONNECTER");
        loginButton.setWidthFull();
        loginButton.getStyle()
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
        
        loginButton.getElement().addEventListener("mouseenter", e -> {
            loginButton.getStyle()
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 6px 20px #c9a961");
        });
        
        loginButton.getElement().addEventListener("mouseleave", e -> {
            loginButton.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 15px #c9a961)");
        });
        
        loginButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            
            if (email.isEmpty() || password.isEmpty()) {
                showNotification("Veuillez remplir tous les champs", NotificationVariant.LUMO_ERROR);
                return;
            }
            
            try {
                Optional<User> user = userService.authenticate(email, password);
                
                if (user.isPresent()) {
                    VaadinSession.getCurrent().setAttribute(User.class, user.get());
                    showNotification("Connexion réussie ! Bienvenue", NotificationVariant.LUMO_SUCCESS);
                    redirectByRole(user.get().getRole());
                } else {
                    showNotification("Nom d'utilisateur ou mot de passe incorrect", NotificationVariant.LUMO_ERROR);
                }
            } catch (RuntimeException ex) {
                // Gérer l'erreur de compte désactivé
                if (ex.getMessage().contains("désactivé")) {
                    showNotification(ex.getMessage(), NotificationVariant.LUMO_ERROR, 5000);
                } else {
                    showNotification("Erreur de connexion", NotificationVariant.LUMO_ERROR);
                }
            }
        });
        
        // Lien créer un compte
        Div registerLinkDiv = new Div();
        registerLinkDiv.getStyle()
            .set("text-align", "center")
            .set("margin-top", "5px");
        
        RouterLink registerLink = new RouterLink("Créez votre compte →", RegisterView.class);
        registerLink.getStyle()
            .set("color", "#999999")
            .set("text-decoration", "none")
            .set("font-size", "0.9rem")
            .set("transition", "color 0.3s ease");
        
        registerLink.getElement().addEventListener("mouseenter", e -> {
            registerLink.getStyle().set("color", "#C8A050");
        });
        
        registerLink.getElement().addEventListener("mouseleave", e -> {
            registerLink.getStyle().set("color", "#999999");
        });
        
        registerLinkDiv.add(registerLink);
        
        form.add(emailField, passwordField, loginButton, registerLinkDiv);
        card.add(title, form);
        add(decorativeShapes, card);
    }
    
    private void redirectByRole(Role role) {
        switch (role) {
            case ADMIN:
                UI.getCurrent().navigate("admin/dashboard");
                break;
            case ORGANIZER:
                UI.getCurrent().navigate("organizer/dashboard");
                break;
            case CLIENT:
                UI.getCurrent().navigate("client/dashboard");
                break;
        }
    }
    
    private void showNotification(String message, NotificationVariant variant) {
        showNotification(message, variant, 3000);
    }
    
    private void showNotification(String message, NotificationVariant variant, int duration) {
        Notification notification = new Notification(message, duration);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }
}