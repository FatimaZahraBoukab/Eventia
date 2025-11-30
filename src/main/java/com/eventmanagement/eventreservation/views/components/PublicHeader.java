package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PublicHeader extends Div {

    public PublicHeader() {
        setId("public-header");
        setWidthFull();
        getStyle()
            .set("background-color", "#FFFFFF")
            .set("padding", "0")
            .set("margin", "0")
            .set("display", "flex")
            .set("align-items", "center")
            .set("height", "60px")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set("right", "0")
            .set("z-index", "1000");

        // <CHANGE> Logo container (LEFT) - moved from right
        Div leftContainer = new Div();
        leftContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("padding-left", "40px")
            .set("flex", "0 0 auto");

        Span logo = new Span("Eventia");
        logo.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "700")
            .set("color", "#D4AF37")
            .set("letter-spacing", "1px");

        leftContainer.add(logo);

        // Center navigation container
        HorizontalLayout centerContainer = new HorizontalLayout();
        centerContainer.getStyle()
            .set("flex", "1")
            .set("justify-content", "center")
            .set("gap", "45px")
            .set("margin", "0");
        centerContainer.setPadding(false);
        centerContainer.setSpacing(false);

        String[] menuItems = {"Accueil", "À propos", "Nos services", "Avis", "Contact"};
        for (String item : menuItems) {
            Span menuSpan = new Span(item);
            menuSpan.getStyle()
                .set("color", "#000000")
                .set("font-weight", "500")
                .set("font-size", "14px")
                .set("cursor", "pointer")
                .set("transition", "color 0.3s ease");

            menuSpan.getElement().addEventListener("mouseenter", e -> {
                menuSpan.getStyle().set("color", "#D4AF37");
            });

            menuSpan.getElement().addEventListener("mouseleave", e -> {
                menuSpan.getStyle().set("color", "#000000");
            });

            centerContainer.add(menuSpan);
        }

        // <CHANGE> Buttons container (RIGHT) - moved from left
        HorizontalLayout rightContainer = new HorizontalLayout();
        rightContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "15px")
            .set("padding-right", "40px")
            .set("flex", "0 0 auto");
        rightContainer.setPadding(false);
        rightContainer.setSpacing(false);

        Button connectButton = new Button("SE CONNECTER");
        connectButton.getStyle()
            .set("background-color", "transparent")
            .set("color", "#000000")
            .set("border", "none")
            .set("font-size", "12px")
            .set("font-weight", "700")
            .set("cursor", "pointer")
            .set("padding", "0")
            .set("letter-spacing", "0.5px");

        connectButton.getElement().addEventListener("mouseenter", e -> {
            connectButton.getStyle().set("color", "#D4AF37");
        });

        connectButton.getElement().addEventListener("mouseleave", e -> {
            connectButton.getStyle().set("color", "#000000");
        });

        Button signupButton = new Button("S'INSCRIRE");
        signupButton.getStyle()
            .set("background-color", "#000000")
            .set("color", "#FFFFFF")
            .set("border", "2px solid #000000")
            .set("padding", "8px 24px")
            .set("font-size", "12px")
            .set("font-weight", "700")
            .set("border-radius", "4px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("letter-spacing", "0.5px");

        signupButton.getElement().addEventListener("mouseenter", e -> {
            signupButton.getStyle()
                .set("background-color", "#D4AF37")
                .set("border", "2px solid #D4AF37")
                .set("color", "#000000");
        });

        signupButton.getElement().addEventListener("mouseleave", e -> {
            signupButton.getStyle()
                .set("background-color", "#000000")
                .set("border", "2px solid #000000")
                .set("color", "#FFFFFF");
        });

        rightContainer.add(connectButton, signupButton);

        add(leftContainer, centerContainer, rightContainer);
    }
}