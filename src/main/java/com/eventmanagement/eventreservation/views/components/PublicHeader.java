package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PublicHeader extends Div {

    public PublicHeader() {
        setId("public-header");
        setWidthFull();
        
        // HEADER BLANC PAR DÉFAUT
        getStyle()
            .set("background-color", "#ffffff")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
            .set("padding", "0")
            .set("margin", "0")
            .set("display", "flex")
            .set("align-items", "center")
            .set("height", "70px")
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set("right", "0")
            .set("z-index", "1000")
            .set("transition", "all 0.3s ease");

        // Logo container (GAUCHE)
        Div leftContainer = new Div();
        leftContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("padding-left", "50px")
            .set("flex", "0 0 auto");

        Span logo = new Span("Eventia");
        logo.setId("logo-text");
        logo.getStyle()
            .set("font-size", "28px")
            .set("font-weight", "700")
            .set("color", "#c9a961")
            .set("letter-spacing", "1px")
            .set("font-family", "'Playfair Display', Georgia, serif")
            .set("cursor", "pointer")
            .set("transition", "color 0.3s ease");
        
        // Logo clique pour retourner en haut
        logo.addClickListener(e -> scrollToSection("hero"));

        leftContainer.add(logo);

        // Navigation container (CENTRE)
        HorizontalLayout centerContainer = new HorizontalLayout();
        centerContainer.setId("nav-container");
        centerContainer.getStyle()
            .set("flex", "1")
            .set("justify-content", "center")
            .set("gap", "40px")
            .set("margin", "0");
        centerContainer.setPadding(false);
        centerContainer.setSpacing(false);

        // Créer les liens de navigation
        centerContainer.add(createNavLink("Accueil", "hero"));
        centerContainer.add(createNavLink("À propos", "about"));
        centerContainer.add(createNavLink("Nos événements", "services"));
        centerContainer.add(createNavLink("Avis", "avis"));
        centerContainer.add(createNavLink("Contact", "contact"));

        // Buttons container (DROITE)
        HorizontalLayout rightContainer = new HorizontalLayout();
        rightContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "20px")
            .set("padding-right", "50px")
            .set("flex", "0 0 auto");
        rightContainer.setPadding(false);
        rightContainer.setSpacing(false);

        Button connectButton = new Button("SE CONNECTER");
        connectButton.setId("connect-button");
        connectButton.getStyle()
            .set("background-color", "transparent")
            .set("color", "#2c2c2c")
            .set("border", "none")
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("cursor", "pointer")
            .set("padding", "0")
            .set("letter-spacing", "1px")
            .set("transition", "color 0.3s ease");

        connectButton.getElement().addEventListener("mouseenter", e -> {
            connectButton.getStyle().set("color", "#c9a961");
        });

        connectButton.getElement().addEventListener("mouseleave", e -> {
            connectButton.getStyle().set("color", "#2c2c2c");
        });
        
        connectButton.addClickListener(e -> {
            UI.getCurrent().navigate("login");
        });

        Button signupButton = new Button("S'INSCRIRE");
        signupButton.getStyle()
            .set("background-color", "#c9a961")
            .set("color", "#ffffff")
            .set("border", "2px solid #c9a961")
            .set("padding", "10px 28px")
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("border-radius", "25px")
            .set("cursor", "pointer")
            .set("transition", "all 0.3s ease")
            .set("letter-spacing", "1px");

        signupButton.getElement().addEventListener("mouseenter", e -> {
            signupButton.getStyle()
                .set("background-color", "transparent")
                .set("border-color", "#c9a961")
                .set("color", "#c9a961")
                .set("transform", "translateY(-2px)")
                .set("box-shadow", "0 4px 12px rgba(201, 169, 97, 0.3)");
        });

        signupButton.getElement().addEventListener("mouseleave", e -> {
            signupButton.getStyle()
                .set("background-color", "#c9a961")
                .set("border-color", "#c9a961")
                .set("color", "#ffffff")
                .set("transform", "translateY(0)")
                .set("box-shadow", "none");
        });
        
        signupButton.addClickListener(e -> {
            UI.getCurrent().navigate("register");
        });

        rightContainer.add(connectButton, signupButton);
        add(leftContainer, centerContainer, rightContainer);
        
        addNavigationStyles();
    }
    
    private Span createNavLink(String text, String sectionId) {
        Span menuSpan = new Span(text);
        menuSpan.addClassName("nav-link");
        menuSpan.getStyle()
            .set("color", "#2c2c2c")
            .set("font-weight", "500")
            .set("font-size", "15px")
            .set("cursor", "pointer")
            .set("transition", "color 0.3s ease")
            .set("position", "relative")
            .set("padding-bottom", "5px")
            .set("user-select", "none");

        // Effet hover
        menuSpan.getElement().addEventListener("mouseenter", e -> {
            menuSpan.getStyle().set("color", "#c9a961");
        });

        menuSpan.getElement().addEventListener("mouseleave", e -> {
            menuSpan.getStyle().set("color", "#2c2c2c");
        });
        
        // IMPORTANT: Utiliser addClickListener au lieu de addEventListener
        menuSpan.addClickListener(e -> {
            scrollToSection(sectionId);
        });

        return menuSpan;
    }
    
    private void scrollToSection(String sectionId) {
        getElement().executeJs(
            "var target = document.getElementById('" + sectionId + "');" +
            "console.log('Clicking on section: " + sectionId + "');" +
            "console.log('Target element:', target);" +
            "if (target) {" +
            "  var headerOffset = 70;" +
            "  var elementPosition = target.getBoundingClientRect().top;" +
            "  var offsetPosition = elementPosition + window.pageYOffset - headerOffset;" +
            "  console.log('Scrolling to position:', offsetPosition);" +
            "  window.scrollTo({" +
            "    top: offsetPosition," +
            "    behavior: 'smooth'" +
            "  });" +
            "} else {" +
            "  console.error('Section " + sectionId + " not found!');" +
            "}"
        );
    }
    
    private void addNavigationStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            ".nav-link::after {" +
            "  content: '';" +
            "  position: absolute;" +
            "  bottom: 0;" +
            "  left: 50%;" +
            "  transform: translateX(-50%);" +
            "  width: 0;" +
            "  height: 2px;" +
            "  background-color: #c9a961;" +
            "  transition: width 0.3s ease;" +
            "}" +
            ".nav-link:hover::after {" +
            "  width: 100%;" +
            "}" +
            "@media (max-width: 1200px) {" +
            "  #public-header {" +
            "    padding: 0 30px !important;" +
            "  }" +
            "  #nav-container {" +
            "    gap: 30px !important;" +
            "  }" +
            "}" +
            "@media (max-width: 968px) {" +
            "  #public-header {" +
            "    padding: 0 20px !important;" +
            "  }" +
            "  #nav-container {" +
            "    display: none !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}