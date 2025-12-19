package com.eventmanagement.eventreservation.views.admin;

import com.eventmanagement.eventreservation.entity.*;
import com.eventmanagement.eventreservation.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Route("admin/dashboard")
public class AdminDashboardView extends Div {
    
    private final UserService userService;
    private final EventService eventService;
    private final ReservationService reservationService;
    private final ContactMessageService contactMessageService;
    
    public AdminDashboardView(@Autowired UserService userService,
                             @Autowired EventService eventService,
                             @Autowired ReservationService reservationService,
                             @Autowired ContactMessageService contactMessageService) {
        this.userService = userService;
        this.eventService = eventService;
        this.reservationService = reservationService;
        this.contactMessageService = contactMessageService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        AdminSidebar sidebar = new AdminSidebar("admin/dashboard");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
        Div header = createAdminHeader();
        
        Div contentContainer = new Div();
        contentContainer.getStyle().set("padding", "30px");
        
       
        HorizontalLayout statsCards = createStatsCards();
        Div chartsSection = createChartsSection();
        
        contentContainer.add( statsCards, chartsSection);
        mainContent.add(header, contentContainer);
        add(sidebar, mainContent);
    }
    
    private Div createAdminHeader() {
        Div header = new Div();
        header.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "20px 30px")
            .set("border-bottom", "1px solid #E8E6E3")
            .set("display", "flex")
            .set("justify-content", "flex-end")
            .set("align-items", "center")
            .set("box-shadow", "0 1px 3px rgba(0,0,0,0.04)");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Admin";
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
            .set("box-shadow", "0 2px 8px rgba(231, 76, 60, 0.25)");
        
        Span initial = new Span(userInitial);
        avatar.add(initial);
        
        profileSection.add(nameSpan, avatar);
        
        profileSection.getElement().addEventListener("mouseenter", e -> {
            profileSection.getStyle().set("background", "rgba(231, 76, 60, 0.08)");
        });
        
        profileSection.getElement().addEventListener("mouseleave", e -> {
            profileSection.getStyle().set("background", "transparent");
        });
        
        profileSection.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate("admin/profile");
        });
        
        header.add(profileSection);
        return header;
    }
    
   
    
    private HorizontalLayout createStatsCards() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.getStyle()
            .set("gap", "20px")
            .set("flex-wrap", "wrap");
        
        long totalUsers = userService.findAll().size();
        long totalEvents = eventService.findAll().size();
        long totalReservations = reservationService.findAll().size();
        long unreadMessages = contactMessageService.countUnreadMessages();
        
        layout.add(
            createStatCard("Utilisateurs", String.valueOf(totalUsers), 
                VaadinIcon.USERS, "#3498db", "admin/users"),
            createStatCard("Événements", String.valueOf(totalEvents), 
                VaadinIcon.CALENDAR, "#9b59b6", "admin/events"),
            createStatCard("Réservations", String.valueOf(totalReservations), 
                VaadinIcon.TICKET, "#e67e22", "admin/reservations"),
            createStatCard("Messages", String.valueOf(unreadMessages), 
                VaadinIcon.ENVELOPE, "#e74c3c", "admin/inbox")
        );
        
        return layout;
    }
    
    private Div createStatCard(String title, String value, VaadinIcon iconType, String color, String route) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "25px")
            .set("border-radius", "14px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("flex", "1")
            .set("min-width", "220px")
            .set("border", "1px solid #F0EDE8")
            .set("transition", "transform 0.3s ease, box-shadow 0.3s ease")
            .set("cursor", "pointer");
        
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                .set("transform", "translateY(-4px)")
                .set("box-shadow", "0 6px 24px rgba(0,0,0,0.1)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                .set("transform", "translateY(0)")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)");
        });
        
        card.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate(route);
        });
        
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "15px");
        
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
            .set("width", "48px")
            .set("height", "48px")
            .set("border-radius", "12px")
            .set("background", color + "15")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        Icon icon = iconType.create();
        icon.getStyle()
            .set("color", color)
            .set("width", "24px")
            .set("height", "24px");
        
        iconWrapper.add(icon);
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("font-size", "34px")
            .set("font-weight", "700")
            .set("color", "#2C2C2C");
        
        header.add(iconWrapper, valueSpan);
        
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
            .set("color", "#6B6B6B")
            .set("font-size", "14px")
            .set("font-weight", "500")
            .set("display", "block");
        
        card.add(header, titleSpan);
        return card;
    }
    
    private Div createChartsSection() {
        Div section = new Div();
        section.getStyle()
            .set("margin-top", "30px")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "30px");
        
        
        
        HorizontalLayout chartsRow = new HorizontalLayout();
        chartsRow.setWidthFull();
        chartsRow.getStyle().set("gap", "30px");
        
        Div eventsStatusChart = createEventsStatusChart();
        eventsStatusChart.getStyle().set("flex", "2");
        
        Div usersDistributionChart = createUsersDistributionChart();
        usersDistributionChart.getStyle().set("flex", "1");
        
        chartsRow.add(eventsStatusChart, usersDistributionChart);
        
        section.add(chartsRow);
        
        return section;
    }
    
    private Div createUsersDistributionChart() {
        Div chartCard = new Div();
        chartCard.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "30px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("min-height", "450px")
            .set("display", "flex")
            .set("flex-direction", "column");
        
        H3 title = new H3("👥 Distribution des utilisateurs");
        title.getStyle()
            .set("margin", "0 0 30px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px")
            .set("font-weight", "600");
        
        List<User> allUsers = userService.findAll();
        
        long admins = allUsers.stream().filter(u -> u.getRole() == Role.ADMIN).count();
        long organizers = allUsers.stream().filter(u -> u.getRole() == Role.ORGANIZER).count();
        long clients = allUsers.stream().filter(u -> u.getRole() == Role.CLIENT).count();
        long total = admins + organizers + clients;
        
        Div donutContainer = new Div();
        donutContainer.getStyle()
            .set("flex", "1")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        if (total > 0) {
            Div donut = createDonutChart(admins, organizers, clients, total);
            Div legend = createUserLegend(admins, organizers, clients, total);
            donutContainer.add(donut, legend);
        } else {
            Span empty = new Span("Aucun utilisateur");
            empty.getStyle().set("color", "#999").set("font-style", "italic");
            donutContainer.add(empty);
        }
        
        chartCard.add(title, donutContainer);
        return chartCard;
    }
    
    private Div createDonutChart(long admins, long organizers, long clients, long total) {
        Div wrapper = new Div();
        wrapper.getStyle()
            .set("position", "relative")
            .set("width", "260px")
            .set("height", "260px")
            .set("margin-bottom", "30px");
        
        String id = "donut-" + UUID.randomUUID().toString().substring(0, 8);
        Div svg = new Div();
        svg.setId(id);
        svg.getStyle().set("width", "100%").set("height", "100%");
        
        double pctAdmins = (admins * 100.0) / total;
        double pctOrganizers = (organizers * 100.0) / total;
        double pctClients = (clients * 100.0) / total;
        
        double angleAdmins = (pctAdmins * 360) / 100;
        double angleOrganizers = (pctOrganizers * 360) / 100;
        double angleClients = (pctClients * 360) / 100;
        
        String svgContent = String.format("""
            <svg viewBox="0 0 200 200" style="width: 100%%; height: 100%%; transform: rotate(-90deg);">
                <defs>
                    <filter id="glow-%s"><feGaussianBlur stdDeviation="2"/></filter>
                </defs>
                <circle cx="100" cy="100" r="75" fill="none" stroke="#F5F5F5" stroke-width="32"/>
                <circle cx="100" cy="100" r="75" fill="none" stroke="#e74c3c" 
                        stroke-width="32" stroke-dasharray="%f 471" stroke-linecap="round" opacity="0.9">
                    <title>Admins: %d (%.1f%%)</title>
                </circle>
                <circle cx="100" cy="100" r="75" fill="none" stroke="#3498db" 
                        stroke-width="32" stroke-dasharray="%f 471" stroke-dashoffset="-%f"
                        stroke-linecap="round" opacity="0.9">
                    <title>Organisateurs: %d (%.1f%%)</title>
                </circle>
                <circle cx="100" cy="100" r="75" fill="none" stroke="#2ecc71" 
                        stroke-width="32" stroke-dasharray="%f 471" stroke-dashoffset="-%f"
                        stroke-linecap="round" opacity="0.9">
                    <title>Clients: %d (%.1f%%)</title>
                </circle>
            </svg>
            """, 
            id,
            (angleAdmins / 360) * 471, admins, pctAdmins,
            (angleOrganizers / 360) * 471, (angleAdmins / 360) * 471, organizers, pctOrganizers,
            (angleClients / 360) * 471, ((angleAdmins + angleOrganizers) / 360) * 471, clients, pctClients
        );
        
        svg.getElement().setProperty("innerHTML", svgContent);
        
        Div center = new Div();
        center.getStyle()
            .set("position", "absolute")
            .set("top", "50%")
            .set("left", "50%")
            .set("transform", "translate(-50%, -50%)")
            .set("text-align", "center");
        
        Span num = new Span(String.valueOf(total));
        num.getStyle()
            .set("display", "block")
            .set("font-size", "42px")
            .set("font-weight", "800")
            .set("color", "#2C3E50");
        
        Span lbl = new Span("Total");
        lbl.getStyle()
            .set("font-size", "11px")
            .set("color", "#999")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "1.5px")
            .set("font-weight", "600");
        
        center.add(num, lbl);
        wrapper.add(svg, center);
        return wrapper;
    }
    
    private Div createUserLegend(long admins, long organizers, long clients, long total) {
        Div legend = new Div();
        legend.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "12px")
            .set("width", "100%")
            .set("padding", "0 20px");
        
        legend.add(
            createLegendItem("Admins", admins, total, "#e74c3c"),
            createLegendItem("Organisateurs", organizers, total, "#3498db"),
            createLegendItem("Clients", clients, total, "#2ecc71")
        );
        
        return legend;
    }
    
    private Div createLegendItem(String label, long value, long total, String color) {
        Div item = new Div();
        item.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "space-between")
            .set("padding", "10px 14px")
            .set("background", "#F8F9FA")
            .set("border-radius", "8px")
            .set("transition", "all 0.3s ease");
        
        item.getElement().addEventListener("mouseenter", e -> {
            item.getStyle()
                .set("background", "#F0F1F3")
                .set("transform", "translateX(5px)");
        });
        
        item.getElement().addEventListener("mouseleave", e -> {
            item.getStyle()
                .set("background", "#F8F9FA")
                .set("transform", "translateX(0)");
        });
        
        Div left = new Div();
        left.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "10px");
        
        Div dot = new Div();
        dot.getStyle()
            .set("width", "10px")
            .set("height", "10px")
            .set("border-radius", "50%")
            .set("background", color)
            .set("box-shadow", "0 2px 6px " + color + "66");
        
        Span lbl = new Span(label);
        lbl.getStyle()
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("color", "#2C3E50");
        
        left.add(dot, lbl);
        
        Div right = new Div();
        right.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "10px");
        
        double pct = (value * 100.0) / total;
        
        Span val = new Span(String.valueOf(value));
        val.getStyle()
            .set("font-size", "15px")
            .set("font-weight", "700")
            .set("color", "#2C3E50");
        
        Span pctSpan = new Span(String.format("%.1f%%", pct));
        pctSpan.getStyle()
            .set("font-size", "12px")
            .set("font-weight", "600")
            .set("color", "#999")
            .set("background", "#FFFFFF")
            .set("padding", "3px 8px")
            .set("border-radius", "12px");
        
        right.add(val, pctSpan);
        item.add(left, right);
        return item;
    }
    
    private Div createEventsStatusChart() {
        Div chartCard = new Div();
        chartCard.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "30px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("min-height", "400px");
        
        H3 title = new H3("📅 Statut des événements");
        title.getStyle()
            .set("margin", "0 0 30px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px")
            .set("font-weight", "600");
        
        List<Event> allEvents = eventService.findAll();
        
        long publies = allEvents.stream().filter(e -> e.getStatut() == EventStatus.PUBLIE).count();
        long brouillons = allEvents.stream().filter(e -> e.getStatut() == EventStatus.BROUILLON).count();
        long annules = allEvents.stream().filter(e -> e.getStatut() == EventStatus.ANNULE).count();
        long termines = allEvents.stream().filter(e -> e.getStatut() == EventStatus.TERMINE).count();
        
        Div barsContainer = new Div();
        barsContainer.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "20px")
            .set("margin-top", "20px");
        
        long maxValue = Math.max(Math.max(publies, brouillons), Math.max(annules, termines));
        
        barsContainer.add(
            createBarItem("Publiés", publies, maxValue, "#27ae60"),
            createBarItem("Brouillons", brouillons, maxValue, "#f39c12"),
            createBarItem("Annulés", annules, maxValue, "#e74c3c"),
            createBarItem("Terminés", termines, maxValue, "#95a5a6")
        );
        
        chartCard.add(title, barsContainer);
        return chartCard;
    }
    
    private Div createBarItem(String label, long value, long maxValue, String color) {
        Div item = new Div();
        item.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "8px");
        
        Div labelRow = new Div();
        labelRow.getStyle()
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("align-items", "center");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-size", "14px")
            .set("font-weight", "600")
            .set("color", "#2C3E50");
        
        Span valueSpan = new Span(String.valueOf(value));
        valueSpan.getStyle()
            .set("font-size", "16px")
            .set("font-weight", "700")
            .set("color", color);
        
        labelRow.add(labelSpan, valueSpan);
        
        Div barBackground = new Div();
        barBackground.getStyle()
            .set("width", "100%")
            .set("height", "12px")
            .set("background", "#F0F0F0")
            .set("border-radius", "20px")
            .set("overflow", "hidden")
            .set("position", "relative");
        
        double percentage = maxValue > 0 ? (value * 100.0) / maxValue : 0;
        
        Div barFill = new Div();
        barFill.getStyle()
            .set("width", "0%")
            .set("height", "100%")
            .set("background", "linear-gradient(90deg, " + color + " 0%, " + color + "CC 100%)")
            .set("border-radius", "20px")
            .set("transition", "width 1s ease-out")
            .set("box-shadow", "0 2px 8px " + color + "44");
        
        barBackground.add(barFill);
        
        // Animation au chargement
        barFill.getElement().executeJs("setTimeout(() => { this.style.width = '" + percentage + "%'; }, 100);");
        
        item.add(labelRow, barBackground);
        return item;
    }
}