package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.Reservation;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.ReservationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;



@Route("client/event/reserve")
@RolesAllowed("CLIENT")
public class ReservationFormView extends Div implements HasUrlParameter<Long> {
    
    private final EventService eventService;
    private final ReservationService reservationService;
    private Event currentEvent;
    
  
    
    private IntegerField nombrePlacesField;
    private Span prixUnitaireSpan;
    private Span montantTotalSpan;
    private TextArea commentaireField;
    private Button validerButton;
    
    public ReservationFormView(EventService eventService, ReservationService reservationService) {
        this.eventService = eventService;
        this.reservationService = reservationService;
    }
    
    @Override
    public void setParameter(BeforeEvent event, Long eventId) {
        currentEvent = eventService.findById(eventId).orElse(null);
        
        if (currentEvent == null) {
            Notification.show("Événement non trouvé", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            UI.getCurrent().navigate("client/events");
            return;
        }
        
        buildUI();
    }
    
    private void buildUI() {
        removeAll();
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        ClientSidebar sidebar = new ClientSidebar("client/events");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
        Div header = createClientHeader();
        
        Div contentContainer = new Div();
        contentContainer.getStyle()
            .set("padding", "40px")
            .set("max-width", "1000px")
            .set("margin", "0 auto");
        
        // Bouton retour
        Button retourButton = new Button("Retour aux événements", new Icon(VaadinIcon.ARROW_LEFT));
        retourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        retourButton.addClickListener(e -> UI.getCurrent().navigate("client/events"));
        retourButton.getStyle().set("margin-bottom", "20px");
        
       
        
        // Formulaire de réservation uniquement (carte événement supprimée)
        Div reservationForm = createReservationForm();
        
        contentContainer.add(retourButton, reservationForm);
        mainContent.add(header, contentContainer);
        add(sidebar, mainContent);
    }
    
    private Div createClientHeader() {
        Div header = new Div();
        header.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "20px 30px")
            .set("border-bottom", "1px solid #E8E6E3")
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("align-items", "center")
            .set("box-shadow", "0 1px 3px rgba(0,0,0,0.04)");
        
        H3 pageTitle = new H3("Nouvelle réservation");
        pageTitle.getStyle()
            .set("margin", "0")
            .set("color", "#2C3E50")
            .set("font-size", "20px")
            .set("font-weight", "600");
        
        HorizontalLayout rightSection = new HorizontalLayout();
        rightSection.setSpacing(true);
        rightSection.setAlignItems(FlexComponent.Alignment.CENTER);
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Client";
        String userInitial = userName.substring(0, 1).toUpperCase();
        
        Span nameSpan = new Span(userName);
        nameSpan.getStyle()
            .set("color", "#2C2C2C")
            .set("font-weight", "500")
            .set("margin-right", "12px");
        
        Div avatar = new Div();
        avatar.getStyle()
            .set("width", "40px")
            .set("height", "40px")
            .set("border-radius", "50%")
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6E 100%)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("color", "#FFFFFF")
            .set("font-weight", "700")
            .set("box-shadow", "0 2px 8px rgba(200, 160, 80, 0.25)");
        avatar.add(new Span(userInitial));
        
        rightSection.add(nameSpan, avatar);
        header.add(pageTitle, rightSection);
        return header;
    }
    
    private Div createReservationForm() {
        Div formCard = new Div();
        formCard.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "16px")
            .set("padding", "35px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)");
        
        H3 formTitle = new H3("Informations de réservation");
        formTitle.getStyle()
            .set("margin", "0 0 25px 0")
            .set("color", "#2C3E50")
            .set("font-size", "22px")
            .set("font-weight", "700");
        
        // Nombre de places
        nombrePlacesField = new IntegerField("Nombre de places");
        nombrePlacesField.setValue(1);
        nombrePlacesField.setMin(1);
        nombrePlacesField.setMax(10);
        nombrePlacesField.setStepButtonsVisible(true);
        nombrePlacesField.setHelperText("Maximum 10 places par réservation");
        nombrePlacesField.getStyle()
            .set("width", "100%")
            .set("margin-bottom", "20px");
        
        nombrePlacesField.addValueChangeListener(e -> updateMontantTotal());
        
        // Récapitulatif des prix
        Div pricingSummary = createPricingSummary();
        
        // Commentaire optionnel
        commentaireField = new TextArea("Commentaire (optionnel)");
        commentaireField.setPlaceholder("Ajoutez des informations supplémentaires...");
        commentaireField.setMaxLength(500);
        commentaireField.getStyle()
            .set("width", "100%")
            .set("margin-bottom", "25px");
        
        // Récapitulatif avant validation
        Div finalRecap = createFinalRecapitulatif();
        
        // Boutons
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.getStyle().set("gap", "15px");
        
        Button annulerButton = new Button("Annuler");
        annulerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        annulerButton.addClickListener(e -> UI.getCurrent().navigate("client/events"));
        
        validerButton = new Button("Confirmer la réservation", new Icon(VaadinIcon.CHECK_CIRCLE));
        validerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        validerButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
            .set("border", "none")
            .set("padding", "12px 30px")
            .set("font-weight", "700")
            .set("box-shadow", "0 4px 15px rgba(200, 160, 80, 0.3)");
        
        validerButton.addClickListener(e -> confirmerReservation());
        
        buttons.add(annulerButton, validerButton);
        
        formCard.add(formTitle, nombrePlacesField, pricingSummary, commentaireField, finalRecap, buttons);
        return formCard;
    }
    
    private Div createPricingSummary() {
        Div summary = new Div();
        summary.getStyle()
            .set("background", "#F8F9FA")
            .set("padding", "25px")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px")
            .set("border", "2px solid #E8E8E8");
        
        // Prix unitaire
        HorizontalLayout prixUnitaireLine = new HorizontalLayout();
        prixUnitaireLine.setWidthFull();
        prixUnitaireLine.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        prixUnitaireLine.setAlignItems(FlexComponent.Alignment.CENTER);
        
        Span prixLabel = new Span("Prix unitaire:");
        prixLabel.getStyle()
            .set("font-size", "16px")
            .set("color", "#666")
            .set("font-weight", "500");
        
        prixUnitaireSpan = new Span(
            currentEvent.getPrixUnitaire() == 0 ? "Gratuit" : 
            String.format("%.2f DH", currentEvent.getPrixUnitaire())
        );
        prixUnitaireSpan.getStyle()
            .set("font-size", "18px")
            .set("color", "#2C3E50")
            .set("font-weight", "700");
        
        prixUnitaireLine.add(prixLabel, prixUnitaireSpan);
        
        // Séparateur
        Hr separator = new Hr();
        separator.getStyle()
            .set("margin", "15px 0")
            .set("border", "none")
            .set("border-top", "1px dashed #DDD");
        
        // Montant total
        HorizontalLayout montantTotalLine = new HorizontalLayout();
        montantTotalLine.setWidthFull();
        montantTotalLine.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        montantTotalLine.setAlignItems(FlexComponent.Alignment.CENTER);
        
        Span totalLabel = new Span("Montant total:");
        totalLabel.getStyle()
            .set("font-size", "18px")
            .set("color", "#2C3E50")
            .set("font-weight", "700");
        
        montantTotalSpan = new Span(
            currentEvent.getPrixUnitaire() == 0 ? "Gratuit" : 
            String.format("%.2f DH", currentEvent.getPrixUnitaire())
        );
        montantTotalSpan.getStyle()
            .set("font-size", "24px")
            .set("color", "#C8A050")
            .set("font-weight", "800");
        
        montantTotalLine.add(totalLabel, montantTotalSpan);
        
        summary.add(prixUnitaireLine, separator, montantTotalLine);
        return summary;
    }
    
    private Div createFinalRecapitulatif() {
        Div recap = new Div();
        recap.getStyle()
            .set("background", "linear-gradient(135deg, #FFF8E7 0%, #FFF4D6 100%)")
            .set("padding", "25px")
            .set("border-radius", "12px")
            .set("margin-bottom", "25px")
            .set("border", "2px solid #C8A050");
        
        Div iconHeader = new Div();
        iconHeader.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "12px")
            .set("margin-bottom", "15px");
        
        Icon infoIcon = VaadinIcon.INFO_CIRCLE.create();
        infoIcon.getStyle()
            .set("color", "#C8A050")
            .set("width", "24px")
            .set("height", "24px");
        
        Span recapTitle = new Span("Important");
        recapTitle.getStyle()
            .set("font-size", "16px")
            .set("font-weight", "700")
            .set("color", "#C8A050");
        
        iconHeader.add(infoIcon, recapTitle);
        
        Paragraph recapText = new Paragraph(
            "Votre réservation sera soumise à l'organisateur pour validation. " +
            "Vous recevrez un code de réservation que vous pourrez utiliser pour suivre le statut de votre demande."
        );
        recapText.getStyle()
            .set("margin", "0")
            .set("color", "#666")
            .set("line-height", "1.6")
            .set("font-size", "14px");
        
        recap.add(iconHeader, recapText);
        return recap;
    }
    
    private void updateMontantTotal() {
        Integer nombrePlaces = nombrePlacesField.getValue();
        if (nombrePlaces != null && nombrePlaces > 0) {
            Double montant = currentEvent.getPrixUnitaire() * nombrePlaces;
            montantTotalSpan.setText(
                currentEvent.getPrixUnitaire() == 0 ? "Gratuit" : 
                String.format("%.2f DH", montant)
            );
            
            // Vérifier la disponibilité
            Integer placesDisponibles = reservationService.getPlacesDisponibles(currentEvent);
            if (nombrePlaces > placesDisponibles) {
                validerButton.setEnabled(false);
                Notification.show(
                    "Seulement " + placesDisponibles + " place(s) disponible(s)",
                    3000,
                    Notification.Position.MIDDLE
                ).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                validerButton.setEnabled(true);
            }
        }
    }
    
    private void confirmerReservation() {
        try {
            User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
            
            Integer nombrePlaces = nombrePlacesField.getValue();
            String commentaire = commentaireField.getValue();
            
            if (nombrePlaces == null || nombrePlaces <= 0) {
                Notification.show("Veuillez sélectionner un nombre de places valide", 
                    3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            // Créer la réservation
            Reservation reservation = reservationService.createReservation(
                currentUser,
                currentEvent,
                nombrePlaces,
                commentaire
            );
            
            // Afficher le dialog de succès avec le code
            showSuccessDialog(reservation);
            
        } catch (Exception e) {
            Notification.show("Erreur: " + e.getMessage(), 
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void showSuccessDialog(Reservation reservation) {
        Dialog successDialog = new Dialog();
        successDialog.setWidth("600px");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().set("text-align", "center");
        
        // Icône de succès
        Div iconCircle = new Div();
        iconCircle.getStyle()
            .set("width", "80px")
            .set("height", "80px")
            .set("border-radius", "50%")
            .set("background", "linear-gradient(135deg, #4CAF50 0%, #45a049 100%)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("margin-bottom", "20px")
            .set("box-shadow", "0 4px 20px rgba(76, 175, 80, 0.3)");
        
        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.getStyle()
            .set("color", "#ffffff")
            .set("width", "50px")
            .set("height", "50px");
        iconCircle.add(checkIcon);
        
        H2 successTitle = new H2("Réservation enregistrée !");
        successTitle.getStyle()
            .set("color", "#2C3E50")
            .set("margin", "0 0 15px 0")
            .set("font-weight", "700");
        
        Paragraph message = new Paragraph(
            "Votre demande de réservation a été enregistrée avec succès. " +
            "Elle est en attente de confirmation par l'organisateur."
        );
        message.getStyle()
            .set("color", "#666")
            .set("line-height", "1.6")
            .set("margin-bottom", "25px");
        
        // Code de réservation
        Div codeBox = new Div();
        codeBox.getStyle()
            .set("background", "#F8F9FA")
            .set("padding", "20px")
            .set("border-radius", "12px")
            .set("border", "2px dashed #C8A050")
            .set("margin-bottom", "20px")
            .set("width", "100%");
        
        Span codeLabel = new Span("Votre code de réservation");
        codeLabel.getStyle()
            .set("display", "block")
            .set("font-size", "12px")
            .set("color", "#999")
            .set("text-transform", "uppercase")
            .set("margin-bottom", "8px");
        
        Span codeValue = new Span(reservation.getCodeReservation());
        codeValue.getStyle()
            .set("display", "block")
            .set("font-size", "28px")
            .set("color", "#C8A050")
            .set("font-weight", "800")
            .set("letter-spacing", "2px");
        
        codeBox.add(codeLabel, codeValue);
        
        Button voirReservationsButton = new Button("Voir mes réservations", 
            new Icon(VaadinIcon.LIST));
        voirReservationsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        voirReservationsButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6A 100%)")
            .set("margin-top", "10px");
        voirReservationsButton.addClickListener(e -> {
            successDialog.close();
            UI.getCurrent().navigate("client/reservations");
        });
        
        layout.add(iconCircle, successTitle, message, codeBox, voirReservationsButton);
        successDialog.add(layout);
        successDialog.open();
    }
}