package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Section Contact - Design Eventia
 * Formulaire de contact avec informations de l'entreprise
 */
public class ContactSection extends Div {
    
    public ContactSection() {
        addClassName("contact-section");
        setId("contact");
        setWidth("100%");
        getStyle()
            .set("background-color", "#faf8f5")
            .set("padding", "100px 40px");
        
        // Container principal
        HorizontalLayout mainContainer = new HorizontalLayout();
        mainContainer.setWidthFull();
        mainContainer.setSpacing(false);
        mainContainer.getStyle()
            .set("max-width", "1200px")
            .set("margin", "0 auto")
            .set("gap", "80px")
            .set("align-items", "flex-start");
        
        // Colonne gauche - Informations
        Div leftColumn = createLeftColumn();
        leftColumn.getStyle()
            .set("flex", "1")
            .set("min-width", "280px");
        
        // Colonne droite - Formulaire
        Div rightColumn = createRightColumn();
        rightColumn.getStyle()
            .set("flex", "1")
            .set("min-width", "320px");
        
        mainContainer.add(leftColumn, rightColumn);
        add(mainContainer);
        
        addStyles();
    }
    
    private Div createLeftColumn() {
        Div column = new Div();
        
        // Titre principal
        H2 title = new H2("Contactez-nous");
        title.getStyle()
            .set("font-family", "'Playfair Display', Georgia, serif")
            .set("font-size", "2.5rem")
            .set("font-weight", "400")
            .set("color", "#1a1a1a")
            .set("margin", "0 0 16px 0")
            .set("line-height", "1.2");
        
        // Sous-titre
        Paragraph subtitle = new Paragraph("Une question ? Un projet ? N'hésitez pas à nous contacter");
        subtitle.getStyle()
            .set("font-size", "1rem")
            .set("color", "#4a4a4a")
            .set("margin", "0 0 50px 0")
            .set("line-height", "1.6");
        
        // Container des informations de contact
        VerticalLayout contactInfo = new VerticalLayout();
        contactInfo.setPadding(false);
        contactInfo.setSpacing(false);
        contactInfo.getStyle().set("gap", "24px");
        
        contactInfo.add(
            createContactItem("location", "Notre Bureau", "123 Avenue des Événements, Tanger"),
            createContactItem("phone", "Téléphone", "+212 6 11 95 58 23"),
            createContactItem("email", "Email", "contact@eventia.com"),
            createContactItem("clock", "Horaires d'ouverture", "Lun - Ven: 9h - 18h")
        );
        
        column.add(title, subtitle, contactInfo);
        return column;
    }
    
    private Div createContactItem(String iconName, String label, String value) {
        Div item = new Div();
        item.getStyle()
            .set("display", "flex")
            .set("align-items", "flex-start")
            .set("gap", "14px");
        
        // Icône SVG en couleur dorée
        Div iconDiv = new Div();
        iconDiv.getElement().setProperty("innerHTML", getSvgIcon(iconName));
        iconDiv.getStyle()
            .set("flex-shrink", "0")
            .set("width", "32px")
            .set("height", "32px")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        // Conteneur texte
        Div textContainer = new Div();
        
        H3 labelText = new H3(label);
        labelText.getStyle()
            .set("font-size", "1rem")
            .set("font-weight", "600")
            .set("color", "#1a1a1a")
            .set("margin", "0 0 4px 0")
            .set("line-height", "1.4");
        
        Paragraph valueText = new Paragraph(value);
        valueText.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#4a4a4a")
            .set("margin", "0")
            .set("line-height", "1.5");
        
        textContainer.add(labelText, valueText);
        item.add(iconDiv, textContainer);
        
        return item;
    }
    
    private String getSvgIcon(String iconName) {
        String color = "#c9a961";
        
        switch(iconName) {
            case "location":
                return "<svg width='28' height='28' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z'></path>" +
                       "<circle cx='12' cy='10' r='3'></circle>" +
                       "</svg>";
            
            case "phone":
                return "<svg width='28' height='28' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z'></path>" +
                       "</svg>";
            
            case "email":
                return "<svg width='28' height='28' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z'></path>" +
                       "<polyline points='22,6 12,13 2,6'></polyline>" +
                       "</svg>";
            
            case "clock":
                return "<svg width='28' height='28' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<circle cx='12' cy='12' r='10'></circle>" +
                       "<polyline points='12 6 12 12 16 14'></polyline>" +
                       "</svg>";
            
            default:
                return "";
        }
    }
    
    private Div createRightColumn() {
        Div column = new Div();
        
        // Titre du formulaire
        H2 formTitle = new H2("Envoyez-nous un message");
        formTitle.getStyle()
            .set("font-family", "'Playfair Display', Georgia, serif")
            .set("font-size", "2.2rem")
            .set("font-weight", "400")
            .set("color", "#1a1a1a")
            .set("margin", "0 0 28px 0")
            .set("line-height", "1.2");
        
        // Formulaire
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(false);
        form.setWidthFull();
        form.getStyle().set("gap", "16px");
        
        // Champs du formulaire
        TextField nameField = createStyledTextField("Votre Nom");
        EmailField emailField = createStyledEmailField("Votre Email");
        TextField subjectField = createStyledTextField("Objet");
        TextArea messageField = createStyledTextArea("Votre Message");
        
        // Bouton d'envoi
        Button submitButton = createSubmitButton();
        
        // Gestion de l'envoi
        submitButton.addClickListener(e -> handleFormSubmit(nameField, emailField, subjectField, messageField));
        
        form.add(nameField, emailField, subjectField, messageField, submitButton);
        column.add(formTitle, form);
        
        return column;
    }
    
    private TextField createStyledTextField(String placeholder) {
        TextField field = new TextField();
        field.setPlaceholder(placeholder);
        field.setWidthFull();
        field.getStyle()
            .set("--vaadin-input-field-background", "transparent")
            .set("--vaadin-input-field-border-color", "#d4d4d4");
        
        return field;
    }
    
    private EmailField createStyledEmailField(String placeholder) {
        EmailField field = new EmailField();
        field.setPlaceholder(placeholder);
        field.setWidthFull();
        field.getStyle()
            .set("--vaadin-input-field-background", "transparent")
            .set("--vaadin-input-field-border-color", "#d4d4d4");
        
        return field;
    }
    
    private TextArea createStyledTextArea(String placeholder) {
        TextArea area = new TextArea();
        area.setPlaceholder(placeholder);
        area.setWidthFull();
        area.setHeight("110px");
        area.getStyle()
            .set("--vaadin-input-field-background", "transparent")
            .set("--vaadin-input-field-border-color", "#d4d4d4");
        
        return area;
    }
    
    private Button createSubmitButton() {
        Button button = new Button("Envoyer ma demande");
        button.getStyle()
            .set("background", "linear-gradient(135deg, #c9a961 0%, #b8972f 100%)")
            .set("color", "#ffffff")
            .set("border", "none")
            .set("padding", "12px 38px")
            .set("font-size", "1rem")
            .set("font-weight", "500")
            .set("border-radius", "30px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("margin-top", "6px")
            .set("box-shadow", "0 4px 12px rgba(201, 169, 97, 0.25)");
        
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 6px 20px rgba(201, 169, 97, 0.35)");
        });
        
        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 12px rgba(201, 169, 97, 0.25)");
        });
        
        return button;
    }
    
    private void handleFormSubmit(TextField nameField, EmailField emailField, 
                                   TextField subjectField, TextArea messageField) {
        // Validation basique
        if (nameField.isEmpty() || emailField.isEmpty() || 
            subjectField.isEmpty() || messageField.isEmpty()) {
            Notification notification = Notification.show(
                "Veuillez remplir tous les champs du formulaire",
                3000,
                Notification.Position.TOP_CENTER
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        
        // Validation email
        if (!emailField.getValue().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Notification notification = Notification.show(
                "Veuillez entrer une adresse email valide",
                3000,
                Notification.Position.TOP_CENTER
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        
        // Simulation d'envoi réussi
        Notification notification = Notification.show(
            "✓ Votre message a été envoyé avec succès ! Nous vous répondrons dans les plus brefs délais.",
            4000,
            Notification.Position.TOP_CENTER
        );
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        
        // Réinitialiser le formulaire
        nameField.clear();
        emailField.clear();
        subjectField.clear();
        messageField.clear();
    }
    
    private void addStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "/* Styles pour les champs de formulaire */" +
            "vaadin-text-field::part(input-field)," +
            "vaadin-email-field::part(input-field)," +
            "vaadin-text-area::part(input-field) {" +
            "  background-color: transparent !important;" +
            "  border: none !important;" +
            "  border-bottom: 1px solid #d4d4d4 !important;" +
            "  border-radius: 0 !important;" +
            "  padding: 10px 0 !important;" +
            "  box-shadow: none !important;" +
            "  font-size: 0.95rem !important;" +
            "}" +
            "" +
            "vaadin-text-field::part(input-field):hover," +
            "vaadin-email-field::part(input-field):hover," +
            "vaadin-text-area::part(input-field):hover {" +
            "  border-bottom-color: #c9a961 !important;" +
            "}" +
            "" +
            "vaadin-text-field::part(input-field):focus," +
            "vaadin-email-field::part(input-field):focus," +
            "vaadin-text-area::part(input-field):focus {" +
            "  border-bottom: 2px solid #c9a961 !important;" +
            "  box-shadow: none !important;" +
            "}" +
            "" +
            "/* Placeholder styling */" +
            "vaadin-text-field input::placeholder," +
            "vaadin-email-field input::placeholder," +
            "vaadin-text-area textarea::placeholder {" +
            "  color: #999 !important;" +
            "  opacity: 1 !important;" +
            "}" +
            "" +
            "/* Responsive design */" +
            "@media (max-width: 1024px) {" +
            "  .contact-section > div {" +
            "    flex-direction: column !important;" +
            "    gap: 60px !important;" +
            "  }" +
            "}" +
            "" +
            "@media (max-width: 768px) {" +
            "  .contact-section {" +
            "    padding: 60px 20px !important;" +
            "  }" +
            "  .contact-section h2 {" +
            "    font-size: 2rem !important;" +
            "  }" +
            "}" +
            "" +
            "@media (max-width: 480px) {" +
            "  .contact-section {" +
            "    padding: 40px 16px !important;" +
            "  }" +
            "  .contact-section h2 {" +
            "    font-size: 1.75rem !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}