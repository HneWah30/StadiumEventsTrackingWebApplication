package controller;

import dao.EventDAO;
import dao.FavoriteDAO;
import model.Event;
import model.user.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EventServlet", urlPatterns = {"/EventServlet"})
public class EventServlet extends HttpServlet {

    private void loadAndForwardEvents(HttpServletRequest request, HttpServletResponse response, String jspPage)
            throws ServletException, IOException {

        String eventType = request.getParameter("eventType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        Timestamp startDate = parseDate(startDateStr, "00:00:00");
        Timestamp endDate = parseDate(endDateStr, "23:59:59");

        List<Event> events = EventDAO.getFilteredEvents(eventType, startDate, endDate);
        request.setAttribute("events", events);
        request.setAttribute("now", new Timestamp(System.currentTimeMillis()));
        request.setAttribute("noEvents", events.isEmpty());

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            List<Integer> favoriteEventIds = FavoriteDAO.getFavoriteEventIds(user.getUserId());
            if (favoriteEventIds == null) {
                favoriteEventIds = new ArrayList<>();
            }
            request.setAttribute("favoriteEventIds", favoriteEventIds);
            request.setAttribute("favorites", FavoriteDAO.getFavoritesByUserId(user.getUserId()));
        }

        request.getRequestDispatcher(jspPage).forward(request, response);
    }

    private Timestamp parseDate(String dateStr, String timeSuffix) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                return Timestamp.valueOf(dateStr + " " + timeSuffix);
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        String jspPage = "calendar".equalsIgnoreCase(view) ? "calendar.jsp" : "index.jsp";
        loadAndForwardEvents(request, response, jspPage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String eventIdStr = request.getParameter("eventId");
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("addEvent".equals(action)) {
            handleAddEvent(request, user);
            loadAndForwardEvents(request, response, "index.jsp");
            return;
        }

        if (eventIdStr != null && !eventIdStr.trim().isEmpty()) {
            try {
                int eventId = Integer.parseInt(eventIdStr.trim());
                if ("favorite".equals(action)) {
                    handleFavorite(response, request, user, eventId, true);
                } else if ("unfavorite".equals(action)) {
                    handleFavorite(response, request, user, eventId, false);
                } else {
                    response.sendRedirect("index.jsp");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid event ID.");
                loadAndForwardEvents(request, response, "favorites.jsp");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private void handleAddEvent(HttpServletRequest request, User user) {
        if (!"ADMIN".equalsIgnoreCase(user.getRole().name())) {
            request.setAttribute("error", "Unauthorized: Only admins can add events.");
            return;
        }

        try {
            String name = request.getParameter("name");
            String dateTimeStr = request.getParameter("dateTime");
            String type = request.getParameter("type");
            String description = request.getParameter("description");
            int ticketsAvailable = Integer.parseInt(request.getParameter("ticketsAvailable"));

            Timestamp dateTime = Timestamp.valueOf(dateTimeStr);
            Event newEvent = new Event(0, name, dateTime, type, description, ticketsAvailable);

            boolean success = EventDAO.addEvent(newEvent);
            request.setAttribute(success ? "message" : "error", success ? "Event added successfully." : "Failed to add event.");

        } catch (Exception e) {
            request.setAttribute("error", "Invalid input. Please check your data.");
        }
    }

    private void handleFavorite(HttpServletResponse response, HttpServletRequest request, User user, int eventId, boolean isFavorite)
        throws IOException, ServletException {

        boolean success = isFavorite ? 
            FavoriteDAO.addFavoriteEvent(user.getUserId(), eventId) : 
            FavoriteDAO.removeFavoriteEvent(user.getUserId(), eventId);

        if (success) {
            response.sendRedirect("EventServlet");  // Ensure correct redirection after action
        } else {
            request.setAttribute("error", isFavorite ? 
                "Could not add event to favorites." : 
                "Could not remove event from favorites.");
            loadAndForwardEvents(request, response, "index.jsp");
        }
    }
}
