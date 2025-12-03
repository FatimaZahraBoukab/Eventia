package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class HeroSection extends Div {
    
    public HeroSection() {
        // Définir l'ID pour la navigation
        setId("hero");
        
        // Configuration de la section hero en plein écran
        setWidth("100%");
        // Ajuster la hauteur pour compenser le header (100vh - 70px)
        setHeight("calc(100vh - 70px)");
        getStyle()
            .set("background-image", "url('images/hero1.webp')")
            .set("background-size", "cover")
            .set("background-position", "center")
            .set("background-attachment", "fixed")
            .set("position", "relative")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "flex-start")
            .set("padding", "0")
            .set("margin", "0")
            .set("overflow", "hidden");

        // Overlay sombre
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "100%")
            .set("height", "100%")
            .set("background-color", "rgba(0, 0, 0, 0.4)");

        // Container du contenu
        Div content = new Div();
        content.getStyle()
            .set("position", "relative")
            .set("z-index", "1")
            .set("color", "white")
            .set("padding-left", "80px")
            .set("max-width", "600px");

        // Sous-titre avec ligne dorée
        Div subtitleContainer = new Div();
        subtitleContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "15px")
            .set("margin-bottom", "20px");

        Div goldenLine = new Div();
        goldenLine.getStyle()
            .set("width", "80px")
            .set("height", "2px")
            .set("background-color", "#ffffff");

        Span subtitle = new Span("BIENVENUE À EVENTIA");
        subtitle.getStyle()
            .set("color", "#ffffff")
            .set("font-size", "14px")
            .set("letter-spacing", "2px")
            .set("font-weight", "500");

        subtitleContainer.add(goldenLine, subtitle);

        // Titre principal
        Span title = new Span("Eventia");
        title.getStyle()
            .set("font-size", "72px")
            .set("font-weight", "bold")
            .set("color", "white")
            .set("display", "block")
            .set("margin-bottom", "20px")
            .set("line-height", "1.2");

        // Description
        Span description = new Span("Réservez vos événements préférés avec facilité");
        description.getStyle()
            .set("font-size", "18px")
            .set("color", "rgba(255, 255, 255, 0.9)")
            .set("display", "block")
            .set("line-height", "1.6");

        content.add(subtitleContainer, title, description);
        add(overlay, content);
        
        // Ajouter les styles responsive
        addResponsiveStyles();
    }
    
    private void addResponsiveStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "@media (max-width: 768px) {" +
            "  #hero {" +
            "    height: calc(100vh - 70px) !important;" +
            "  }" +
            "  #hero > div:nth-child(2) {" +
            "    padding-left: 30px !important;" +
            "    padding-right: 30px !important;" +
            "  }" +
            "  #hero h1, #hero > div:nth-child(2) > span:nth-child(2) {" +
            "    font-size: 48px !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}