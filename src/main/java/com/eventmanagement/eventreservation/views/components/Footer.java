package com.eventmanagement.eventreservation.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Footer de l'application Eventia - Version améliorée
 */
public class Footer extends Div {
    
    public Footer() {
        addClassName("footer");
        setWidth("100%");
        getStyle()
            .set("background-color", "#1a1a1a")
            .set("color", "#ffffff")
            .set("padding", "60px 40px 30px 40px");
        
        // Container principal
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1200px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(false);
        
        // Contenu principal du footer
        HorizontalLayout mainContent = createMainContent();
        
        // Ligne de copyright
        Div copyright = createCopyright();
        
        container.add(mainContent, copyright);
        add(container);
        
        addStyles();
    }
    
    private HorizontalLayout createMainContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.getStyle()
            .set("gap", "50px")
            .set("margin-bottom", "40px")
            .set("flex-wrap", "wrap")
            .set("justify-content", "space-between");
        
        // Colonne 1 - À propos
        Div aboutColumn = createAboutColumn();
        
        // Colonne 2 - Liens rapides
        Div linksColumn = createLinksColumn();
        
        // Colonne 3 - Contact
        Div contactColumn = createContactColumn();
        
        // Colonne 4 - Réseaux sociaux
        Div socialColumn = createSocialColumn();
        
        layout.add(aboutColumn, linksColumn, contactColumn, socialColumn);
        return layout;
    }
    
    private Div createAboutColumn() {
        Div column = new Div();
        column.getStyle()
            .set("flex", "1")
            .set("min-width", "200px");
        
        Span logo = new Span("Eventia");
        logo.getStyle()
            .set("font-size", "1.8rem")
            .set("font-weight", "700")
            .set("color", "#c9a961")
            .set("display", "block")
            .set("margin-bottom", "15px")
            .set("font-family", "'Playfair Display', Georgia, serif");
        
        Span description = new Span("Votre partenaire événementiel pour des moments inoubliables.");
        description.getStyle()
            .set("color", "#aaa")
            .set("line-height", "1.6")
            .set("font-size", "0.9rem");
        
        column.add(logo, description);
        return column;
    }
    
    private Div createLinksColumn() {
        Div column = new Div();
        column.getStyle()
            .set("flex", "1")
            .set("min-width", "150px");
        
        H3 title = new H3("Liens rapides");
        title.getStyle()
            .set("font-size", "1.1rem")
            .set("margin-bottom", "20px")
            .set("color", "#ffffff")
            .set("font-weight", "600");
        
        // Configuration des liens avec leurs sections correspondantes
        String[][] linksData = {
            {"Accueil", "hero"},
            {"À propos", "about"},
            {"Services", "services"},
            {"Avis", "avis"},
            {"Contact", "contact"}
        };
        
        VerticalLayout linksList = new VerticalLayout();
        linksList.setPadding(false);
        linksList.setSpacing(false);
        linksList.getStyle().set("gap", "10px");
        
        for (String[] linkData : linksData) {
            Span linkSpan = createNavigationLink(linkData[0], linkData[1]);
            linksList.add(linkSpan);
        }
        
        column.add(title, linksList);
        return column;
    }
    
    private Span createNavigationLink(String text, String sectionId) {
        Span linkSpan = new Span(text);
        linkSpan.getStyle()
            .set("color", "#aaa")
            .set("cursor", "pointer")
            .set("font-size", "0.9rem")
            .set("transition", "color 0.3s ease");
        
        // Effet hover
        linkSpan.getElement().addEventListener("mouseenter", e -> {
            linkSpan.getStyle().set("color", "#c9a961");
        });
        
        linkSpan.getElement().addEventListener("mouseleave", e -> {
            linkSpan.getStyle().set("color", "#aaa");
        });
        
        // Navigation vers la section
        linkSpan.getElement().addEventListener("click", e -> {
            UI.getCurrent().getPage().executeJs(
                "const element = document.getElementById($0);" +
                "if (element) {" +
                "  element.scrollIntoView({ behavior: 'smooth', block: 'start' });" +
                "}",
                sectionId
            );
        });
        
        return linkSpan;
    }
    
    private Div createContactColumn() {
        Div column = new Div();
        column.getStyle()
            .set("flex", "1")
            .set("min-width", "200px");
        
        H3 title = new H3("Contact");
        title.getStyle()
            .set("font-size", "1.1rem")
            .set("margin-bottom", "20px")
            .set("color", "#ffffff")
            .set("font-weight", "600");
        
        VerticalLayout contactList = new VerticalLayout();
        contactList.setPadding(false);
        contactList.setSpacing(false);
        contactList.getStyle().set("gap", "12px");
        
        contactList.add(
            createContactItem("phone", "+212 6 11 95 58 23"),
            createContactItem("email", "contact@eventia.com"),
            createContactItem("location", "Tanger, Maroc")
        );
        
        column.add(title, contactList);
        return column;
    }
    
    private Div createContactItem(String iconType, String text) {
        Div item = new Div();
        item.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "12px");
        
        // Icône SVG dorée
        Div iconDiv = new Div();
        iconDiv.getElement().setProperty("innerHTML", getContactIcon(iconType));
        iconDiv.getStyle()
            .set("flex-shrink", "0")
            .set("width", "20px")
            .set("height", "20px")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        Span textSpan = new Span(text);
        textSpan.getStyle()
            .set("color", "#aaa")
            .set("font-size", "0.9rem");
        
        item.add(iconDiv, textSpan);
        return item;
    }
    
    private String getContactIcon(String iconType) {
        String color = "#c9a961";
        
        switch(iconType) {
            case "phone":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z'></path>" +
                       "</svg>";
            
            case "email":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z'></path>" +
                       "<polyline points='22,6 12,13 2,6'></polyline>" +
                       "</svg>";
            
            case "location":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<path d='M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z'></path>" +
                       "<circle cx='12' cy='10' r='3'></circle>" +
                       "</svg>";
            
            default:
                return "";
        }
    }
    
    private Div createSocialColumn() {
        Div column = new Div();
        column.getStyle()
            .set("flex", "1")
            .set("min-width", "150px");
        
        H3 title = new H3("Suivez-nous");
        title.getStyle()
            .set("font-size", "1.1rem")
            .set("margin-bottom", "20px")
            .set("color", "#ffffff")
            .set("font-weight", "600");
        
        HorizontalLayout socialIcons = new HorizontalLayout();
        socialIcons.setSpacing(true);
        socialIcons.getStyle().set("gap", "15px");
        
        // Facebook
        Anchor facebookLink = createSocialLink(
            "https://www.facebook.com",
            getSocialIcon("facebook")
        );
        
        // Instagram
        Anchor instagramLink = createSocialLink(
            "https://www.instagram.com",
            getSocialIcon("instagram")
        );
        
        // X (Twitter)
        Anchor xLink = createSocialLink(
            "https://www.x.com",
            getSocialIcon("x")
        );
        
        socialIcons.add(facebookLink, instagramLink, xLink);
        column.add(title, socialIcons);
        return column;
    }
    
    private Anchor createSocialLink(String url, String svgIcon) {
        Anchor link = new Anchor(url, "");
        link.setTarget("_blank");
        link.getStyle()
            .set("text-decoration", "none")
            .set("display", "inline-flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("width", "40px")
            .set("height", "40px")
            .set("border-radius", "50%")
            .set("background-color", "rgba(201, 169, 97, 0.1)")
            .set("transition", "all 0.3s ease")
            .set("cursor", "pointer");
        
        Div iconDiv = new Div();
        iconDiv.getElement().setProperty("innerHTML", svgIcon);
        iconDiv.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        link.getElement().addEventListener("mouseenter", e -> {
            link.getStyle()
                .set("background-color", "#c9a961")
                .set("transform", "translateY(-3px)")
                .set("box-shadow", "0 5px 15px rgba(201, 169, 97, 0.3)");
        });
        
        link.getElement().addEventListener("mouseleave", e -> {
            link.getStyle()
                .set("background-color", "rgba(201, 169, 97, 0.1)")
                .set("transform", "translateY(0)")
                .set("box-shadow", "none");
        });
        
        link.add(iconDiv);
        return link;
    }
    
    private String getSocialIcon(String platform) {
        String color = "#c9a961";
        
        switch(platform) {
            case "facebook":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='" + color + "'>" +
                       "<path d='M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z'/>" +
                       "</svg>";
            
            case "instagram":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='none' stroke='" + color + "' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>" +
                       "<rect x='2' y='2' width='20' height='20' rx='5' ry='5'></rect>" +
                       "<path d='M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z'></path>" +
                       "<line x1='17.5' y1='6.5' x2='17.51' y2='6.5'></line>" +
                       "</svg>";
            
            case "x":
                return "<svg width='20' height='20' viewBox='0 0 24 24' fill='" + color + "'>" +
                       "<path d='M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z'/>" +
                       "</svg>";
            
            default:
                return "";
        }
    }
    
    private Div createCopyright() {
        Div copyright = new Div();
        copyright.getStyle()
            .set("text-align", "center")
            .set("padding-top", "30px")
            .set("border-top", "1px solid #333")
            .set("margin-top", "40px");
        
        Span text = new Span("© 2025 Eventia. Tous droits réservés.");
        text.getStyle()
            .set("color", "#777")
            .set("font-size", "0.85rem");
        
        copyright.add(text);
        return copyright;
    }
    
    private void addStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "@media (max-width: 968px) {" +
            "  .footer {" +
            "    padding: 40px 20px 20px 20px !important;" +
            "  }" +
            "}" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }
}