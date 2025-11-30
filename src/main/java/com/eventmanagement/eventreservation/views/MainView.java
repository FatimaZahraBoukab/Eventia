package com.eventmanagement.eventreservation.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.eventmanagement.eventreservation.views.components.PublicHeader;
import com.eventmanagement.eventreservation.views.components.HeroSection;

@Route("")
public class MainView extends VerticalLayout {
    
    public MainView() {
        // <CHANGE> Remove all spacing and set full dimensions
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setWidth("100%");
        setHeight("100%");
        getStyle().set("margin", "0")
                  .set("padding", "0")
                  .set("overflow", "hidden");

        PublicHeader header = new PublicHeader();
        HeroSection hero = new HeroSection();

        add(header, hero);
    }
}