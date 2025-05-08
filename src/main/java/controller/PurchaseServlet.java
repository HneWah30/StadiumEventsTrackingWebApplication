package controller;

import dao.DatabaseConnection;
import dao.EventDAO;
import dao.PurchaseDAO;
import dao.ReservationDAO;
import model.Event;
import model.Purchase;
import model.user.User;
import model.user.EmailUtil;

import java.io.IOException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PurchaseServlet", urlPatterns = {"/PurchaseServlet"})
public class PurchaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Parse request parameters
        String eventIdStr = request.getParameter("eventId");
        String quantityStr = request.getParameter("quantity");

        int quantity, eventId;
        try {
            quantity = Integer.parseInt(quantityStr);
            eventId = Integer.parseInt(eventIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ticket quantity or event ID.");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        // Check quantity
        if (quantity <= 0) {
            request.setAttribute("error", "Invalid ticket quantity.");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                request.setAttribute("error", "Database connection failed.");
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }

            EventDAO eventDAO = new EventDAO();
            PurchaseDAO purchaseDAO = new PurchaseDAO();
            ReservationDAO reservationDAO = new ReservationDAO(conn);

            // Fetch event details
            Event event = eventDAO.getEventById(eventId);
            if (event == null) {
                request.setAttribute("error", "Event not found.");
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }

            // Reduce ticket quantity
            boolean ticketReduced = eventDAO.reduceTickets(eventId, quantity);

            if (ticketReduced) {
                // Record purchase
                Purchase purchase = new Purchase(user.getUserId(), eventId, quantity);
                int generatedId = purchaseDAO.addPurchase(purchase);

                if (generatedId > 0) {
                    purchase.setPurchaseId(generatedId);

                    // Link reservation to this purchase
                    boolean updated = reservationDAO.linkReservationToPurchase(user.getUserId(), eventId, quantity, generatedId);
                    if (!updated) {
                        System.out.println("Reservation not linked to purchase.");
                    }

                    // Set confirmation attributes
                    request.setAttribute("message", "Successfully purchased " + quantity + " ticket(s) for " + event.getName() + ".");
                    request.setAttribute("reservationId", generatedId);
                    request.setAttribute("userName", user.getUsername());
                    request.setAttribute("event", event);
                    request.setAttribute("quantity", quantity);

                    // Send confirmation email
                    EmailUtil.sendConfirmationEmail(
                            user.getEmail(),
                            user.getUsername(),
                            event.getName(),
                            quantity
                    );

                } else {
                    request.setAttribute("error", "Purchase could not be recorded. Please try again.");
                }

            } else {
                request.setAttribute("error", "Not enough tickets available for " + event.getName() + ".");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during the purchase process.");
        }

        // Forward to confirmation page
        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}
