package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

@Route("client/support")
@RolesAllowed("CLIENT")
public class ClientSupportView extends Div {
    
    public ClientSupportView() {
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        ClientSidebar sidebar = new ClientSidebar("client/support");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
        Div header = createClientHeader();
        
        Div contentContainer = new Div();
        contentContainer.getStyle()
            .set("padding", "30px")
            .set("max-width", "1000px")
            .set("margin", "0 auto");
        
        H2 pageTitle = new H2("Support et assistance");
        pageTitle.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 15px 0")
            .set("font-size", "32px")
            .set("font-weight", "700")
            .set("text-align", "center");
        
        Span subtitle = new Span("Besoin d'aide ? Notre équipe d'experts est disponible pour vous accompagner dans toutes vos démarches.");
        subtitle.getStyle()
            .set("color", "#666666")
            .set("font-size", "16px")
            .set("display", "block")
            .set("text-align", "center")
            .set("margin-bottom", "50px");
        
        HorizontalLayout contactCards = new HorizontalLayout();
        contactCards.setWidthFull();
        contactCards.getStyle()
            .set("gap", "30px")
            .set("justify-content", "center")
            .set("flex-wrap", "wrap");
        
        contactCards.add(
            createContactCard(
                VaadinIcon.ENVELOPE,
                "Nous contacter par email",
                "contact@eventia.com",
                "Envoyer un email",
                "mailto:contact@eventia.com"
            ),
            createContactCard(
                VaadinIcon.PHONE,
                "Nous appeler",
                "Du lundi au vendredi, de 9h à 18h",
                "+212 6 11 95 58 23",
                "tel:+212611955823"
            )
        );
        
        contentContainer.add(pageTitle, subtitle, contactCards);
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
        
        Button reserverButton = new Button("Réserver", new Icon(VaadinIcon.PLUS));
        reserverButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reserverButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6E 100%)")
            .set("border", "none")
            .set("color", "#FFFFFF")
            .set("font-weight", "600")
            .set("border-radius", "25px")
            .set("padding", "10px 24px")
            .set("box-shadow", "0 4px 12px rgba(200, 160, 80, 0.25)")
            .set("transition", "all 0.3s ease");
        
        reserverButton.addClickListener(e -> UI.getCurrent().navigate("client/events"));
        
        HorizontalLayout rightSection = new HorizontalLayout();
        rightSection.setSpacing(true);
        rightSection.setAlignItems(FlexComponent.Alignment.CENTER);
        rightSection.getStyle().set("gap", "15px");
        
        Div supportIcon = new Div();
        supportIcon.getStyle()
            .set("width", "40px")
            .set("height", "40px")
            .set("border-radius", "50%")
            .set("background", "rgba(200, 160, 80, 0.1)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease");
        
        Icon helpIcon = VaadinIcon.QUESTION_CIRCLE.create();
        helpIcon.getStyle()
            .set("color", "#C8A050")
            .set("width", "22px")
            .set("height", "22px");
        supportIcon.add(helpIcon);
        supportIcon.getElement().addEventListener("click", e -> UI.getCurrent().navigate("client/support"));
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Client";
        String userInitial = userName.substring(0, 1).toUpperCase();
        
        HorizontalLayout profileSection = new HorizontalLayout();
        profileSection.setSpacing(true);
        profileSection.setAlignItems(HorizontalLayout.Alignment.CENTER);
        profileSection.getStyle()
            .set("cursor", "pointer")
            .set("padding", "8px 15px")
            .set("border-radius", "30px")
            .set("transition", "all 0.3s ease");
        
        Span nameSpan = new Span(userName);
        nameSpan.getStyle()
            .set("color", "#2C2C2C")
            .set("font-weight", "500")
            .set("font-size", "14px")
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
            .set("font-size", "16px")
            .set("box-shadow", "0 2px 8px rgba(200, 160, 80, 0.25)");
        
        avatar.add(new Span(userInitial));
        profileSection.add(nameSpan, avatar);
        profileSection.getElement().addEventListener("click", e -> UI.getCurrent().navigate("client/profile"));
        
        rightSection.add(supportIcon, profileSection);
        header.add(reserverButton, rightSection);
        return header;
    }
    
    private Div createContactCard(VaadinIcon iconType, String title, String info, String buttonText, String link) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "40px 35px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
            .set("text-align", "center")
            .set("flex", "1")
            .set("min-width", "320px")
            .set("max-width", "420px")
            .set("transition", "transform 0.3s ease, box-shadow 0.3s ease");
        
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-5px)")
                .set("box-shadow", "0 8px 30px rgba(200, 160, 80, 0.15)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)");
        });
        
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
            .set("width", "80px")
            .set("height", "80px")
            .set("border-radius", "50%")
            .set("background", "linear-gradient(135deg, rgba(200, 160, 80, 0.1) 0%, rgba(212, 175, 110, 0.15) 100%)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("margin", "0 auto 25px auto");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("color", "#C8A050")
            .set("width", "36px")
            .set("height", "36px");
        
        iconWrapper.add(icon);
        
        H3 titleText = new H3(title);
        titleText.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 15px 0")
            .set("font-size", "20px")
            .set("font-weight", "600");
        
        Span infoText = new Span(info);
        infoText.getStyle()
            .set("color", "#666666")
            .set("font-size", "15px")
            .set("display", "block")
            .set("margin-bottom", "25px")
            .set("line-height", "1.5");
        
        Button actionButton = new Button(buttonText);
        actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        actionButton.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6E 100%)")
            .set("border", "none")
            .set("color", "#FFFFFF")
            .set("font-weight", "600")
            .set("border-radius", "25px")
            .set("padding", "12px 32px")
            .set("box-shadow", "0 4px 12px rgba(200, 160, 80, 0.25)")
            .set("transition", "all 0.3s ease")
            .set("font-size", "15px");
        
        actionButton.getElement().addEventListener("click", e -> {
            getElement().executeJs("window.location.href = '" + link + "'");
        });
        
        card.add(iconWrapper, titleText, infoText, actionButton);
        return card;
    }
}