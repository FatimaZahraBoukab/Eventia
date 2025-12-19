package com.eventmanagement.eventreservation.views.client;

import com.eventmanagement.eventreservation.entity.Reservation;
import com.eventmanagement.eventreservation.entity.ReservationStatus;
import com.eventmanagement.eventreservation.entity.User;
import com.eventmanagement.eventreservation.service.EventService;
import com.eventmanagement.eventreservation.service.ReservationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;


@Route("client/dashboard")
@RolesAllowed("CLIENT")
public class ClientDashboardView extends Div {
    
    private final EventService eventService;
    private final ReservationService reservationService;
    
    public ClientDashboardView(EventService eventService, ReservationService reservationService) {
        this.eventService = eventService;
        this.reservationService = reservationService;
        
        setSizeFull();
        getStyle()
            .set("display", "flex")
            .set("margin", "0")
            .set("padding", "0")
            .set("background-color", "#FAF9F7");
        
        ClientSidebar sidebar = new ClientSidebar("client/dashboard");
        
        Div mainContent = new Div();
        mainContent.getStyle()
            .set("flex", "1")
            .set("margin-left", "260px")
            .set("padding", "0")
            .set("overflow-y", "auto");
        
        Div header = createClientHeader();
        
        Div contentContainer = new Div();
        contentContainer.getStyle().set("padding", "30px");
        
        Div welcomePanel = createWelcomePanel();
        HorizontalLayout statsCards = createRealStatsCards();
        Div chartsSection = createChartsSection();
        
        contentContainer.add(welcomePanel, statsCards, chartsSection);
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
            .set("cursor", "pointer");
        
        Icon helpIcon = VaadinIcon.QUESTION_CIRCLE.create();
        helpIcon.getStyle()
            .set("color", "#C8A050")
            .set("width", "22px")
            .set("height", "22px");
        supportIcon.add(helpIcon);
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Client";
        String userInitial = userName.substring(0, 1).toUpperCase();
        
        HorizontalLayout profileSection = new HorizontalLayout();
        profileSection.setSpacing(true);
        profileSection.setAlignItems(HorizontalLayout.Alignment.CENTER);
        profileSection.getStyle()
            .set("cursor", "pointer")
            .set("padding", "8px 15px")
            .set("border-radius", "30px");
        
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
        
        Span initial = new Span(userInitial);
        avatar.add(initial);
        
        profileSection.add(nameSpan, avatar);
        profileSection.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate("client/profile");
        });
        
        rightSection.add(supportIcon, profileSection);
        header.add(reserverButton, rightSection);
        return header;
    }
    
    private Div createWelcomePanel() {
        Div panel = new Div();
        panel.getStyle()
            .set("background", "linear-gradient(135deg, #C8A050 0%, #D4AF6E 100%)")
            .set("padding", "35px")
            .set("border-radius", "16px")
            .set("margin-bottom", "30px")
            .set("box-shadow", "0 4px 20px rgba(200, 160, 80, 0.15)");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        String userName = currentUser != null ? currentUser.getFullName() : "Client";
        
        H2 title = new H2("Bienvenue " + userName + " 👋");
        title.getStyle()
            .set("color", "#FFFFFF")
            .set("margin", "0 0 10px 0")
            .set("font-size", "28px")
            .set("font-weight", "700");
        
        Span subtitle = new Span("Découvrez des événements incroyables et réservez vos places en quelques clics.");
        subtitle.getStyle()
            .set("color", "rgba(255,255,255,0.95)")
            .set("font-size", "16px")
            .set("line-height", "1.5");
        
        panel.add(title, subtitle);
        return panel;
    }
    
    private HorizontalLayout createRealStatsCards() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.getStyle()
            .set("gap", "20px")
            .set("flex-wrap", "wrap");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        
        long eventsDisponibles = eventService.findPublishedEvents().size();
        List<Reservation> mesReservations = reservationService.findByUtilisateur(currentUser);
        long totalReservations = mesReservations.size();
        long enAttente = mesReservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.EN_ATTENTE)
            .count();
        long confirmees = mesReservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.CONFIRMEE)
            .count();
        
        layout.add(
            createStatCard("Événements disponibles", String.valueOf(eventsDisponibles), 
                VaadinIcon.CALENDAR, "#5B9BD5"),
            createStatCard("Mes réservations", String.valueOf(totalReservations), 
                VaadinIcon.TICKET, "#C8A050"),
            createStatCard("En attente", String.valueOf(enAttente), 
                VaadinIcon.CLOCK, "#ED7D31"),
            createStatCard("Confirmées", String.valueOf(confirmees), 
                VaadinIcon.CHECK_CIRCLE, "#70AD47")
        );
        
        return layout;
    }
    
    private Div createStatCard(String title, String value, VaadinIcon iconType, String color) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "25px")
            .set("border-radius", "14px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("flex", "1")
            .set("min-width", "220px")
            .set("border", "1px solid #F0EDE8")
            .set("transition", "transform 0.3s ease, box-shadow 0.3s ease");
        
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
        
        Div curveChart = createCurveChart();
        curveChart.getStyle().set("flex", "2");
        
        Div donutChart = createDonutChart();
        donutChart.getStyle().set("flex", "1");
        
        chartsRow.add(curveChart, donutChart);
        section.add(chartsRow);
        
        return section;
    }
    
    private Div createCurveChart() {
        Div chartCard = new Div();
        chartCard.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "30px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("min-height", "450px");
        
        H3 title = new H3("📈 Évolution des réservations - Ce mois");
        title.getStyle()
            .set("margin", "0 0 30px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px")
            .set("font-weight", "600");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        List<Reservation> reservations = reservationService.findByUtilisateur(currentUser);
        
        // Calculer les 4 dernières semaines du mois en cours
        Map<String, Long> weeklyData = calculateWeeklyData(reservations);
        
        Div chartContainer = createSmoothCurveChart(weeklyData);
        
        chartCard.add(title, chartContainer);
        return chartCard;
    }
    
    private Map<String, Long> calculateWeeklyData(List<Reservation> reservations) {
        Map<String, Long> weeklyData = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        
        // Obtenir le premier jour du mois
        LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        
        // Calculer les semaines
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        
        // Compter les réservations par semaine
        Map<Integer, Long> weekCounts = new HashMap<>();
        
        for (Reservation r : reservations) {
            LocalDate dateRes = r.getDateReservation().toLocalDate();
            if (dateRes.getMonthValue() == currentMonth && dateRes.getYear() == currentYear) {
                int weekOfMonth = dateRes.get(weekFields.weekOfMonth());
                weekCounts.put(weekOfMonth, weekCounts.getOrDefault(weekOfMonth, 0L) + 1);
            }
        }
        
        // Déterminer le nombre de semaines dans le mois
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        int maxWeek = lastDayOfMonth.get(weekFields.weekOfMonth());
        
        // Créer les labels pour chaque semaine
        for (int week = 1; week <= maxWeek; week++) {
            String label = "Sem " + week;
            weeklyData.put(label, weekCounts.getOrDefault(week, 0L));
        }
        
        return weeklyData;
    }
    
    private Div createSmoothCurveChart(Map<String, Long> data) {
        Div chartWrapper = new Div();
        chartWrapper.getStyle()
            .set("width", "100%")
            .set("height", "350px")
            .set("position", "relative")
            .set("padding", "20px 10px");
        
        if (data.isEmpty() || data.values().stream().allMatch(v -> v == 0)) {
            Span emptyMessage = new Span("Aucune réservation ce mois-ci");
            emptyMessage.getStyle()
                .set("color", "#999")
                .set("font-style", "italic")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("height", "100%");
            chartWrapper.add(emptyMessage);
            return chartWrapper;
        }
        
        long maxValue = data.values().stream().max(Long::compare).orElse(1L);
        
        // Container pour la grille et le graphique
        Div gridContainer = new Div();
        gridContainer.getStyle()
            .set("position", "relative")
            .set("width", "100%")
            .set("height", "280px")
            .set("border-left", "2px solid #E8E8E8")
            .set("border-bottom", "2px solid #E8E8E8")
            .set("margin-bottom", "20px");
        
        // Créer le SVG pour la courbe
        String svgId = "curve-" + UUID.randomUUID().toString().substring(0, 8);
        Div svgContainer = new Div();
        svgContainer.setId(svgId);
        svgContainer.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "100%")
            .set("height", "100%")
            .set("pointer-events", "none");
        
        // Calculer les points
        List<String> labels = new ArrayList<>(data.keySet());
        List<Long> values = new ArrayList<>(data.values());
        
        double width = 100.0;
        double height = 100.0;
        double stepX = width / (labels.size() - 1);
        
        StringBuilder pathD = new StringBuilder("M ");
        StringBuilder areaD = new StringBuilder("M ");
        
        for (int i = 0; i < values.size(); i++) {
            double x = i * stepX;
            double y = height - ((values.get(i) * height) / maxValue);
            
            if (i == 0) {
                pathD.append(x).append(" ").append(y);
                areaD.append(x).append(" ").append(height).append(" L ").append(x).append(" ").append(y);
            } else {
                pathD.append(" L ").append(x).append(" ").append(y);
                areaD.append(" L ").append(x).append(" ").append(y);
            }
        }
        
        areaD.append(" L ").append(width).append(" ").append(height).append(" Z");
        
        String svgContent = String.format("""
            <svg viewBox="0 0 100 100" preserveAspectRatio="none" 
                 style="width: 100%%; height: 100%%; position: absolute; top: 0; left: 0;">
                <defs>
                    <linearGradient id="gradient-%s" x1="0" x2="0" y1="0" y2="1">
                        <stop offset="0%%" stop-color="#667eea" stop-opacity="0.4"/>
                        <stop offset="100%%" stop-color="#667eea" stop-opacity="0.05"/>
                    </linearGradient>
                </defs>
                <path d="%s" fill="url(#gradient-%s)" />
                <path d="%s" fill="none" stroke="#667eea" stroke-width="1" 
                      stroke-linecap="round" stroke-linejoin="round" 
                      style="filter: drop-shadow(0px 3px 6px rgba(102, 126, 234, 0.4))"/>
            </svg>
            """, svgId, areaD.toString(), svgId, pathD.toString());
        
        svgContainer.getElement().setProperty("innerHTML", svgContent);
        
        // Ajouter les points interactifs
        Div pointsContainer = new Div();
        pointsContainer.getStyle()
            .set("position", "absolute")
            .set("top", "0")
            .set("left", "0")
            .set("width", "100%")
            .set("height", "100%");
        
        for (int i = 0; i < values.size(); i++) {
            double xPercent = (i * stepX);
            double yPercent = 100 - ((values.get(i) * 100.0) / maxValue);
            
            Div point = new Div();
            point.getStyle()
                .set("position", "absolute")
                .set("left", xPercent + "%")
                .set("top", yPercent + "%")
                .set("width", "14px")
                .set("height", "14px")
                .set("background", "#667eea")
                .set("border", "3px solid #FFFFFF")
                .set("border-radius", "50%")
                .set("transform", "translate(-50%, -50%)")
                .set("cursor", "pointer")
                .set("box-shadow", "0 3px 10px rgba(102, 126, 234, 0.5)")
                .set("transition", "all 0.3s ease")
                .set("z-index", "10");
            
            String label = labels.get(i);
            long value = values.get(i);
            
            // Tooltip
            Div tooltip = new Div();
            tooltip.getStyle()
                .set("position", "absolute")
                .set("bottom", "120%")
                .set("left", "50%")
                .set("transform", "translateX(-50%)")
                .set("background", "#2C3E50")
                .set("color", "#FFFFFF")
                .set("padding", "10px 16px")
                .set("border-radius", "10px")
                .set("font-size", "13px")
                .set("font-weight", "600")
                .set("white-space", "nowrap")
                .set("opacity", "0")
                .set("pointer-events", "none")
                .set("transition", "opacity 0.3s ease")
                .set("box-shadow", "0 6px 16px rgba(0,0,0,0.2)");
            
            tooltip.add(new Span(label + ": " + value + " réservation" + (value > 1 ? "s" : "")));
            
            point.add(tooltip);
            
            point.getElement().addEventListener("mouseenter", e -> {
                point.getStyle()
                    .set("width", "18px")
                    .set("height", "18px")
                    .set("box-shadow", "0 5px 20px rgba(102, 126, 234, 0.7)");
                tooltip.getStyle().set("opacity", "1");
            });
            
            point.getElement().addEventListener("mouseleave", e -> {
                point.getStyle()
                    .set("width", "14px")
                    .set("height", "14px")
                    .set("box-shadow", "0 3px 10px rgba(102, 126, 234, 0.5)");
                tooltip.getStyle().set("opacity", "0");
            });
            
            pointsContainer.add(point);
        }
        
        gridContainer.add(svgContainer, pointsContainer);
        
        // Labels en bas
        Div labelsContainer = new Div();
        labelsContainer.getStyle()
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("padding", "0 5px");
        
        for (String label : labels) {
            Span labelSpan = new Span(label);
            labelSpan.getStyle()
                .set("font-size", "12px")
                .set("color", "#666")
                .set("font-weight", "600");
            labelsContainer.add(labelSpan);
        }
        
        chartWrapper.add(gridContainer, labelsContainer);
        return chartWrapper;
    }
    
    private Div createDonutChart() {
        Div chartCard = new Div();
        chartCard.getStyle()
            .set("background", "#FFFFFF")
            .set("padding", "30px")
            .set("border-radius", "16px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.06)")
            .set("min-height", "450px")
            .set("display", "flex")
            .set("flex-direction", "column");
        
        H3 title = new H3("🎯 Statut des réservations");
        title.getStyle()
            .set("margin", "0 0 30px 0")
            .set("color", "#2C3E50")
            .set("font-size", "18px")
            .set("font-weight", "600");
        
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        List<Reservation> reservations = reservationService.findByUtilisateur(currentUser);
        
        long enAttente = reservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.EN_ATTENTE).count();
        long confirmees = reservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.CONFIRMEE).count();
        long annulees = reservations.stream()
            .filter(r -> r.getStatut() == ReservationStatus.ANNULEE).count();
        
        long total = enAttente + confirmees + annulees;
        
        Div donutContainer = new Div();
        donutContainer.getStyle()
            .set("flex", "1")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("position", "relative");
        
        if (total > 0) {
            Div donut = createModernDonut(enAttente, confirmees, annulees, total);
            Div legend = createLegend(enAttente, confirmees, annulees, total);
            donutContainer.add(donut, legend);
        } else {
            Span emptyMessage = new Span("Aucune réservation");
            emptyMessage.getStyle()
                .set("color", "#999")
                .set("font-style", "italic");
            donutContainer.add(emptyMessage);
        }
        
        chartCard.add(title, donutContainer);
        return chartCard;
    }
    
    private Div createModernDonut(long enAttente, long confirmees, long annulees, long total) {
        Div donutWrapper = new Div();
        donutWrapper.getStyle()
            .set("position", "relative")
            .set("width", "280px")
            .set("height", "280px")
            .set("margin-bottom", "30px");
        
        String donutId = "donut-" + UUID.randomUUID().toString().substring(0, 8);
        Div svgContainer = new Div();
        svgContainer.setId(donutId);
        svgContainer.getStyle()
            .set("width", "100%")
            .set("height", "100%");
        
        double percentAttente = (enAttente * 100.0) / total;
        double percentConfirmees = (confirmees * 100.0) / total;
        double percentAnnulees = (annulees * 100.0) / total;
        
        double angleAttente = (percentAttente * 360) / 100;
        double angleConfirmees = (percentConfirmees * 360) / 100;
        double angleAnnulees = (percentAnnulees * 360) / 100;
        
        String svg = createModernDonutSVG(angleAttente, angleConfirmees, angleAnnulees, 
                                    enAttente, confirmees, annulees, 
                                    percentAttente, percentConfirmees, percentAnnulees, 
                                    donutId);
        svgContainer.getElement().setProperty("innerHTML", svg);
        
        // Centre avec total
        Div center = new Div();
        center.getStyle()
            .set("position", "absolute")
            .set("top", "50%")
            .set("left", "50%")
            .set("transform", "translate(-50%, -50%)")
            .set("text-align", "center")
            .set("pointer-events", "none");
        
        Span totalNumber = new Span(String.valueOf(total));
        totalNumber.getStyle()
            .set("display", "block")
            .set("font-size", "48px")
            .set("font-weight", "800")
            .set("color", "#2C3E50")
            .set("line-height", "1");
        
        Span totalLabel = new Span("Total");
        totalLabel.getStyle()
            .set("display", "block")
            .set("font-size", "12px")
            .set("color", "#999")
            .set("margin-top", "8px")
            .set("font-weight", "600")
            .set("text-transform", "uppercase")
            .set("letter-spacing", "1.5px");
        
        center.add(totalNumber, totalLabel);
        donutWrapper.add(svgContainer, center);
        return donutWrapper;
    }
    
    private String createModernDonutSVG(double angle1, double angle2, double angle3,
                                  long val1, long val2, long val3,
                                  double percent1, double percent2, double percent3,
                                  String id) {
        return String.format("""
            <svg viewBox="0 0 200 200" style="width: 100%%; height: 100%%; transform: rotate(-90deg);">
                <defs>
                    <filter id="glow-%s">
                        <feGaussianBlur stdDeviation="2" result="coloredBlur"/>
                        <feMerge>
                            <feMergeNode in="coloredBlur"/>
                            <feMergeNode in="SourceGraphic"/>
                        </feMerge>
                    </filter>
                </defs>
                
                <!-- Fond gris clair -->
                <circle cx="100" cy="100" r="75" fill="none" stroke="#F5F5F5" stroke-width="32"/>
                
                <!-- Segment En attente - Orange -->
                <circle cx="100" cy="100" r="75" fill="none" stroke="#FF9F43" 
                        stroke-width="32" stroke-dasharray="%f 471" 
                        stroke-linecap="round"
                        style="transition: all 0.5s ease; filter: url(#glow-%s);"
                        opacity="0.9">
                    <title>En attente: %d (%.1f%%)</title>
                </circle>
                
                <!-- Segment Confirmées - Vert -->
                <circle cx="100" cy="100" r="75" fill="none" stroke="#10b981" 
                        stroke-width="32" stroke-dasharray="%f 471" 
                        stroke-dashoffset="-%f"
                        stroke-linecap="round"
                        style="transition: all 0.5s ease; filter: url(#glow-%s);"
                        opacity="0.9">
                    <title>Confirmées: %d (%.1f%%)</title>
                </circle>
                
                <!-- Segment Annulées - Rouge -->
                <circle cx="100" cy="100" r="75" fill="none" stroke="#ef4444" 
                        stroke-width="32" stroke-dasharray="%f 471" 
                        stroke-dashoffset="-%f"
                        stroke-linecap="round"
                        style="transition: all 0.5s ease; filter: url(#glow-%s);"
                        opacity="0.9">
                    <title>Annulées: %d (%.1f%%)</title>
                </circle>
            </svg>
            """, 
            id,
            (angle1 / 360) * 471, id, val1, percent1,
            (angle2 / 360) * 471, (angle1 / 360) * 471, id, val2, percent2,
            (angle3 / 360) * 471, ((angle1 + angle2) / 360) * 471, id, val3, percent3
        );
    }
    
    private Div createLegend(long enAttente, long confirmees, long annulees, long total) {
        Div legend = new Div();
        legend.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "15px")
            .set("width", "100%")
            .set("padding", "0 20px");
        
        legend.add(
            createLegendItem("En attente", enAttente, total, "#FF9F43"),
            createLegendItem("Confirmées", confirmees, total, "#10b981"),
            createLegendItem("Annulées", annulees, total, "#ef4444")
        );
        
        return legend;
    }
    
    private Div createLegendItem(String label, long value, long total, String color) {
        Div item = new Div();
        item.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "space-between")
            .set("padding", "12px 16px")
            .set("background", "#F8F9FA")
            .set("border-radius", "10px")
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
        
        Div leftSide = new Div();
        leftSide.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "12px");
        
        Div colorDot = new Div();
        colorDot.getStyle()
            .set("width", "12px")
            .set("height", "12px")
            .set("border-radius", "50%")
            .set("background", color)
            .set("box-shadow", "0 2px 8px " + color + "66");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("font-size", "14px")
            .set("font-weight", "600")
            .set("color", "#2C3E50");
        
        leftSide.add(colorDot, labelSpan);
        
        Div rightSide = new Div();
        rightSide.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("gap", "12px");
        
        double percentage = (value * 100.0) / total;
        
        Span valueSpan = new Span(String.valueOf(value));
        valueSpan.getStyle()
            .set("font-size", "16px")
            .set("font-weight", "700")
            .set("color", "#2C3E50");
        
        Span percentSpan = new Span(String.format("%.1f%%", percentage));
        percentSpan.getStyle()
            .set("font-size", "13px")
            .set("font-weight", "600")
            .set("color", "#999")
            .set("background", "#FFFFFF")
            .set("padding", "4px 10px")
            .set("border-radius", "20px");
        
        rightSide.add(valueSpan, percentSpan);
        
        item.add(leftSide, rightSide);
        return item;
    }
}