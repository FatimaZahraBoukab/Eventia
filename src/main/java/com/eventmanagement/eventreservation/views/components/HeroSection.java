package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class HeroSection extends Div {
    
    public HeroSection() {
        // <CHANGE> Set full viewport height without scroll
        setWidth("100%");
        setHeight("calc(100vh - 60px)");
        getStyle().set("margin-top", "60px")
                  .set("background-image", "url('images/hero.jpg')")
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

        // Overlay
        Div overlay = new Div();
        overlay.getStyle().set("position", "absolute")
                          .set("top", "0")
                          .set("left", "0")
                          .set("width", "100%")
                          .set("height", "100%")
                          .set("background-color", "rgba(0, 0, 0, 0.4)");

        // Content Container
        Div content = new Div();
        content.getStyle().set("position", "relative")
                          .set("z-index", "1")
                          .set("color", "white")
                          .set("padding-left", "80px")
                          .set("max-width", "600px");

        // Subtitle with gold accent line
        Div subtitleContainer = new Div();
        subtitleContainer.getStyle().set("display", "flex")
                                     .set("align-items", "center")
                                     .set("gap", "15px")
                                     .set("margin-bottom", "20px");

        Div goldenLine = new Div();
        goldenLine.getStyle().set("width", "80px")
                             .set("height", "2px")
                             .set("background-color", "#ffffffff");

        Span subtitle = new Span("BIENVENUE À EVENTIA");
        subtitle.getStyle().set("color", "#ffffffff")
                           .set("font-size", "14px")
                           .set("letter-spacing", "2px")
                           .set("font-weight", "500");

        subtitleContainer.add(goldenLine, subtitle);

        // Main Title
        Span title = new Span("Eventia");
        title.getStyle().set("font-size", "72px")
                        .set("font-weight", "bold")
                        .set("color", "white")
                        .set("display", "block")
                        .set("margin-bottom", "20px")
                        .set("line-height", "1.2");

        // Description
        Span description = new Span("Réservez vos événements préférés avec facilité");
        description.getStyle().set("font-size", "18px")
                              .set("color", "rgba(255, 255, 255, 0.9)")
                              .set("display", "block")
                              .set("line-height", "1.6");

        content.add(subtitleContainer, title, description);
        add(overlay, content);
    }
}