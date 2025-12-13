package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

@Route("client/events")
@RolesAllowed("CLIENT")
public class ClientEventsView extends Div {
    
    public ClientEventsView() {
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
        contentContainer.getStyle().set("padding", "30px");
        
        H2 title = new H2("Événements disponibles");
        title.getStyle()
            .set("color", "#333333")
            .set("margin", "0 0 30px 0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        Div emptyState = createEmptyState();
        
        contentContainer.add(title, emptyState);
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
    
    private Div createEmptyState() {
        Div container = new Div();
        container.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("padding", "60px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.08)")
            .set("text-align", "center");
        
        Span icon = new Span("📅");
        icon.getStyle()
            .set("font-size", "64px")
            .set("display", "block")
            .set("margin-bottom", "20px");
        
        Span message = new Span("Aucun événement disponible pour le moment");
        message.getStyle()
            .set("color", "#666666")
            .set("font-size", "18px")
            .set("display", "block")
            .set("margin-bottom", "10px");
        
        Span subtitle = new Span("Les nouveaux événements apparaîtront ici");
        subtitle.getStyle()
            .set("color", "#999999")
            .set("font-size", "14px")
            .set("display", "block");
        
        container.add(icon, message, subtitle);
        return container;
    }
}