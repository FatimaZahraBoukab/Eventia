package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class ServicesSection extends Div {

    public ServicesSection() {
        addClassName("services-section");
        setId("services");
        setWidth("100%");
        getStyle()
            .set("background-color", "#ffffff")
            .set("padding", "80px 40px");

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1400px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(false);
        container.setAlignItems(Alignment.CENTER);

        Div header = createSectionHeader();
        HorizontalLayout servicesGrid = createServicesGrid();
        Button viewAllButton = createViewAllButton();

        container.add(header, servicesGrid, viewAllButton);
        add(container);

        addStyles();
    }

    private Div createSectionHeader() {
        Div headerDiv = new Div();
        headerDiv.getStyle()
            .set("text-align", "center")
            .set("margin-bottom", "40px");

        Span services = new Span("ÉVÉNEMENTS");
        services.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#c9a961")
            .set("letter-spacing", "3px")
            .set("font-weight", "500")
            .set("display", "block")
            .set("margin-bottom", "10px");

        H2 title = new H2();
        title.getElement().setProperty("innerHTML",
                "Offrez-vous le <span style='color: #c9a961;'>Meilleur</span>");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "2.8rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "10px")
            .set("font-weight", "400");

        Paragraph subtitle = new Paragraph("Des événements prêts à vivre, à portée de clic");
        subtitle.getStyle()
            .set("font-size", "1rem")
            .set("color", "#666");

        headerDiv.add(services, title, subtitle);
        return headerDiv;
    }

    private HorizontalLayout createServicesGrid() {
        HorizontalLayout grid = new HorizontalLayout();
        grid.setWidthFull();
        grid.getStyle()
            .set("gap", "30px")
            .set("justify-content", "center")
            .set("margin-bottom", "25px")
            .set("max-width", "1200px")
            .set("margin", "0 auto")
            .set("padding-right", "60px");

        grid.add(
            createServiceCard("images/DJ1.jpg", "Soirée DJ", "Marrakech", "mariage-details"),
            createServiceCard("images/confi.jpg", "Conférence du Digital", "Rabat", "conference-details"),
            createServiceCard("images/gala1.jpg", "Gala artistique", "Casablanca", "anniversaire-details")
        );

        return grid;
    }

    private Div createServiceCard(String imagePath, String title, String ville, String route) {
        Div card = new Div();
        card.addClassName("service-card");
        card.getStyle()
            .set("width", "340px")
            .set("background-color", "#ffffff")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.1)")
            .set("cursor", "pointer")
            .set("position", "relative");

        Div imageContainer = new Div();
        imageContainer.getStyle()
            .set("height", "350px")
            .set("position", "relative")
            .set("overflow", "hidden");

        Image serviceImage = new Image(imagePath, title);
        serviceImage.getStyle()
            .set("width", "100%")
            .set("height", "100%")
            .set("object-fit", "cover")
            .set("transition", "transform 0.3s ease");

        // ✅ Badge Ville
        Span cityBadge = new Span(ville);
        cityBadge.getStyle()
            .set("position", "absolute")
            .set("top", "16px")
            .set("left", "16px")
            .set("background", "rgba(255,255,255,0.9)")
            .set("padding", "6px 14px")
            .set("font-size", "0.75rem")
            .set("font-weight", "500")
            .set("border-radius", "20px")
            .set("color", "#2c2c2c")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)");

        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("bottom", "0")
            .set("left", "0")
            .set("right", "0")
            .set("padding", "25px")
            .set("background", "rgba(255,255,255,0.95)")
            .set("text-align", "center");

        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
            .set("font-family", "'Playfair Display', serif")
            .set("font-size", "1.6rem")
            .set("color", "#c9a961")
            .set("margin", "0");

        overlay.add(cardTitle);
        imageContainer.add(serviceImage, cityBadge, overlay);
        card.add(imageContainer);

        card.addClickListener(e -> UI.getCurrent().navigate(route));
        return card;
    }

    private Button createViewAllButton() {
        Button button = new Button("Découvrir tous nos événements");
        button.getStyle()
            .set("background-color", "#c9a961")
            .set("color", "#ffffff")
            .set("padding", "14px 40px")
            .set("font-weight", "600");
        button.addClickListener(e -> UI.getCurrent().navigate("tous-evenements"));
        return button;
    }

    private void addStyles() {
        getElement().executeJs(
            "const style=document.createElement('style');" +
            "style.textContent=`@media(max-width:968px){.service-card{width:100%!important;}}`;" +
            "document.head.appendChild(style);"
        );
    }
}
