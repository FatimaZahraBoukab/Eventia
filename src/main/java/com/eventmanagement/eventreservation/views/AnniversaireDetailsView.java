package com.eventmanagement.eventreservation.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.eventmanagement.eventreservation.views.components.PublicHeader;
import com.eventmanagement.eventreservation.views.components.Footer;

@Route("anniversaire-details")
public class AnniversaireDetailsView extends Div {
    
    public AnniversaireDetailsView() {
        setSizeFull();
        getStyle()
            .set("margin", "0")
            .set("padding", "0")
            .set("overflow-x", "hidden")
            .set("overflow-y", "auto");
        
        PublicHeader header = new PublicHeader();
        
        VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setPadding(false);
        contentContainer.setMargin(false);
        contentContainer.setSpacing(false);
        contentContainer.setWidthFull();
        contentContainer.getStyle().set("margin-top", "60px");
        
        Div hero = createHeroSection();
        Div details = createDetailsSection();
        Footer footer = new Footer();
        
        contentContainer.add(hero, details, footer);
        add(header, contentContainer);
    }
    
    private Div createHeroSection() {
        Div hero = new Div();
        hero.getStyle()
            .set("width", "100%")
            .set("height", "400px")
            .set("background-image", "url('images/anniversaire.jpg')")
            .set("background-size", "cover")
            .set("background-position", "center")
            .set("position", "relative")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "100%")
            .set("height", "100%")
            .set("background-color", "rgba(0, 0, 0, 0.5)");
        
        H1 title = new H1("Anniversaires");
        title.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "3.5rem")
            .set("color", "#ffffff")
            .set("position", "relative")
            .set("z-index", "1")
            .set("margin", "0");
        
        hero.add(overlay, title);
        return hero;
    }
    
    private Div createDetailsSection() {
        Div section = new Div();
        section.getStyle()
            .set("padding", "80px 40px")
            .set("background-color", "#ffffff");
        
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("1200px");
        container.getStyle().set("margin", "0 auto");
        
        H3 intro = new H3("Célébrez vos moments précieux");
        intro.getStyle()
            .set("font-family", "'Playfair Display', 'Georgia', serif")
            .set("font-size", "2rem")
            .set("color", "#c9a961")
            .set("margin-bottom", "30px")
            .set("text-align", "center");
        
        Paragraph description = new Paragraph(
            "Chaque anniversaire mérite d'être célébré avec style et joie. Que ce soit pour un " +
            "enfant, un adolescent ou un adulte, Eventia crée des fêtes d'anniversaire mémorables " +
            "et personnalisées. De la décoration thématique aux animations, nous transformons vos " +
            "rêves en réalité pour une journée extraordinaire."
        );
        description.getStyle()
            .set("font-size", "1.1rem")
            .set("line-height", "1.8")
            .set("color", "#555")
            .set("text-align", "center")
            .set("margin-bottom", "60px")
            .set("max-width", "900px")
            .set("margin-left", "auto")
            .set("margin-right", "auto");
        
        HorizontalLayout services = new HorizontalLayout();
        services.setWidthFull();
        services.setSpacing(true);
        services.getStyle()
            .set("gap", "40px")
            .set("flex-wrap", "wrap")
            .set("justify-content", "center")
            .set("margin-bottom", "50px");
        
        services.add(
            createServiceItem("🎈", "Décoration festive", "Thèmes personnalisés et colorés"),
            createServiceItem("🎂", "Gâteau sur mesure", "Pâtisserie créative selon vos envies"),
            createServiceItem("🎪", "Animations variées", "Jeux, spectacles et activités ludiques"),
            createServiceItem("🎁", "Cadeaux et surprises", "Moments magiques et inoubliables")
        );
        
        Button bookButton = new Button("Organiser mon anniversaire");
        bookButton.getStyle()
            .set("background-color", "#c9a961")
            .set("color", "#ffffff")
            .set("border", "none")
            .set("padding", "16px 50px")
            .set("font-size", "1.1rem")
            .set("font-weight", "600")
            .set("border-radius", "4px")
            .set("cursor", "pointer")
            .set("margin", "0 auto")
            .set("display", "block")
            .set("transition", "all 0.3s ease");
        
        bookButton.addClickListener(e -> {
            UI.getCurrent().navigate("contact");
        });
        
        container.add(intro, description, services, bookButton);
        section.add(container);
        return section;
    }
    
    private Div createServiceItem(String icon, String title, String description) {
        Div item = new Div();
        item.getStyle()
            .set("flex", "1")
            .set("min-width", "250px")
            .set("max-width", "300px")
            .set("text-align", "center")
            .set("padding", "30px 20px")
            .set("background-color", "#f9f9f9")
            .set("border-radius", "8px");
        
        Div iconDiv = new Div();
        iconDiv.setText(icon);
        iconDiv.getStyle()
            .set("font-size", "3rem")
            .set("margin-bottom", "20px");
        
        H3 itemTitle = new H3(title);
        itemTitle.getStyle()
            .set("font-size", "1.3rem")
            .set("color", "#2c2c2c")
            .set("margin-bottom", "10px")
            .set("font-weight", "600");
        
        Paragraph itemDesc = new Paragraph(description);
        itemDesc.getStyle()
            .set("font-size", "0.95rem")
            .set("color", "#666")
            .set("line-height", "1.6")
            .set("margin", "0");
        
        item.add(iconDiv, itemTitle, itemDesc);
        return item;
    }
}