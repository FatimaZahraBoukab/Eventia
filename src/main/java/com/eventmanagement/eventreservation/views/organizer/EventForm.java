package com.eventmanagement.eventreservation.views.organizer;

import com.eventmanagement.eventreservation.entity.Event;
import com.eventmanagement.eventreservation.entity.EventCategory;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class EventForm extends Div {
    
    private Event event;
    private Binder<Event> binder = new Binder<>(Event.class);
    
    // Champs du formulaire
    private TextField titre = new TextField("Titre de l'événement");
    private TextArea description = new TextArea("Description");
    private ComboBox<EventCategory> categorie = new ComboBox<>("Catégorie");
    private DateTimePicker dateDebut = new DateTimePicker("Date et heure de début");
    private DateTimePicker dateFin = new DateTimePicker("Date et heure de fin");
    private TextField lieu = new TextField("Lieu");
    private TextField ville = new TextField("Ville");
    private IntegerField capaciteMax = new IntegerField("Capacité maximale");
    private NumberField prixUnitaire = new NumberField("Prix unitaire (DH)");
    private TextField imageUrl = new TextField("URL de l'image (optionnel)");
    
    // Boutons
    private Button saveButton = new Button("Enregistrer");
    private Button cancelButton = new Button("Annuler");
    
    public EventForm() {
        addClassName("event-form");
        
        configureFields();
        configureBinder();
        
        FormLayout formLayout = new FormLayout();
        formLayout.add(
            titre, categorie,
            dateDebut, dateFin,
            lieu, ville,
            capaciteMax, prixUnitaire,
            description,
            imageUrl
        );
        
        formLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        
        formLayout.setColspan(description, 2);
        formLayout.setColspan(imageUrl, 2);
        
        HorizontalLayout buttons = createButtonsLayout();
        
        add(formLayout, buttons);
        
        setStyle();
    }
    
    private void configureFields() {
        // Titre
        titre.setPlaceholder("Ex: Concert de musique classique");
        titre.setMinLength(5);
        titre.setMaxLength(100);
        
        // Description
        description.setPlaceholder("Décrivez votre événement...");
        description.setMaxLength(1000);
        description.setHeight("150px");
        
        // Catégorie
        categorie.setItems(EventCategory.values());
        categorie.setItemLabelGenerator(EventCategory::getDisplayName);
        categorie.setPlaceholder("Sélectionnez une catégorie");
        
        // Lieu et ville
        lieu.setPlaceholder("Ex: Théâtre Mohammed V");
        ville.setPlaceholder("Ex: Rabat");
        
        // Capacité
        capaciteMax.setMin(1);
        capaciteMax.setStep(1);
        capaciteMax.setPlaceholder("Ex: 500");
        
        // Prix
        prixUnitaire.setMin(0);
        prixUnitaire.setStep(0.01);
        prixUnitaire.setPlaceholder("Ex: 150.00");
        
        // URL de l'image
        imageUrl.setPlaceholder("Ex: https://example.com/image.jpg");
        imageUrl.setHelperText("Collez l'URL d'une image hébergée en ligne");
    }
    
    private void configureBinder() {
        binder.forField(titre)
            .asRequired("Le titre est obligatoire")
            .withValidator(t -> t.length() >= 5 && t.length() <= 100, 
                "Le titre doit contenir entre 5 et 100 caractères")
            .bind(Event::getTitre, Event::setTitre);
        
        binder.forField(description)
            .bind(Event::getDescription, Event::setDescription);
        
        binder.forField(categorie)
            .asRequired("La catégorie est obligatoire")
            .bind(Event::getCategorie, Event::setCategorie);
        
        binder.forField(dateDebut)
            .asRequired("La date de début est obligatoire")
            .bind(Event::getDateDebut, Event::setDateDebut);
        
        binder.forField(dateFin)
            .bind(Event::getDateFin, Event::setDateFin);
        
        binder.forField(lieu)
            .asRequired("Le lieu est obligatoire")
            .bind(Event::getLieu, Event::setLieu);
        
        binder.forField(ville)
            .asRequired("La ville est obligatoire")
            .bind(Event::getVille, Event::setVille);
        
        binder.forField(capaciteMax)
            .asRequired("La capacité est obligatoire")
            .withValidator(cap -> cap != null && cap > 0, "La capacité doit être supérieure à 0")
            .bind(Event::getCapaciteMax, Event::setCapaciteMax);
        
        binder.forField(prixUnitaire)
            .asRequired("Le prix est obligatoire")
            .withValidator(prix -> prix != null && prix >= 0, "Le prix ne peut pas être négatif")
            .bind(Event::getPrixUnitaire, Event::setPrixUnitaire);
        
        binder.forField(imageUrl)
            .bind(Event::getImagePath, Event::setImagePath);
    }
    
    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        saveButton.addClickListener(e -> validateAndSave());
        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));
        
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.getStyle()
            .set("margin-top", "20px")
            .set("justify-content", "flex-end");
        
        return buttonsLayout;
    }
    
    private void validateAndSave() {
        try {
            if (event == null) {
                event = new Event();
            }
            
            binder.writeBean(event);
            
            // Validation supplémentaire des dates
            if (event.getDateFin() != null && event.getDateFin().isBefore(event.getDateDebut())) {
                Notification.show("La date de fin doit être après la date de début", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            fireEvent(new SaveEvent(this, event));
        } catch (ValidationException e) {
            Notification.show("Veuillez corriger les erreurs du formulaire", 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    public void setEvent(Event event) {
        this.event = event;
        binder.readBean(event);
    }
    
    private void setStyle() {
        getStyle()
            .set("background", "#ffffff")
            .set("padding", "30px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)");
    }
    
    // Events
    public static abstract class EventFormEvent extends ComponentEvent<EventForm> {
        private Event event;
        
        protected EventFormEvent(EventForm source, Event event) {
            super(source, false);
            this.event = event;
        }
        
        public Event getEvent() {
            return event;
        }
    }
    
    public static class SaveEvent extends EventFormEvent {
        SaveEvent(EventForm source, Event event) {
            super(source, event);
        }
    }
    
    public static class CancelEvent extends EventFormEvent {
        CancelEvent(EventForm source) {
            super(source, null);
        }
    }
    
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}