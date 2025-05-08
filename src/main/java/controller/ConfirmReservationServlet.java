/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EventDAO;
import dao.PurchaseDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Event;
import model.Purchase;
import model.user.EmailUtil;
import model.user.User;

/**
 *
 * @author ezeki
 */
@WebServlet(name = "ConfirmReservationServlet", urlPatterns = {"/ConfirmReservationServlet"})
public class ConfirmReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String eventIdStr = request.getParameter("eventId");
        String quantityStr = request.getParameter("quantity");

        int eventId, quantity;
        try {
            eventId = Integer.parseInt(eventIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input data.");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        Event event = EventDAO.getEventById(eventId);
        if (event == null) {
            request.setAttribute("error", "Event not found.");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        if (quantity <= 0 || quantity > event.getTicketsAvailable()) {
            request.setAttribute("error", "Invalid or excessive ticket quantity.");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        // Attempt to reduce tickets and save purchase
        boolean success = EventDAO.reduceTickets(eventId, quantity);
        if (success) {
            Purchase purchase = new Purchase(user.getUserId(), eventId, quantity);
            int purchaseId = PurchaseDAO.addPurchase(purchase);

            if (purchaseId > 0) {
                request.setAttribute("message", "Purchase successful.");
                request.setAttribute("reservationId", purchaseId);
                request.setAttribute("userName", user.getUsername());
                request.setAttribute("event", event);
                request.setAttribute("quantity", quantity);

                EmailUtil.sendConfirmationEmail(
                    user.getEmail(),
                    user.getUsername(),
                    event.getName(),
                    quantity
                );
            } else {
                request.setAttribute("error", "Could not complete purchase.");
            }
        } else {
            request.setAttribute("error", "Not enough tickets available.");
        }

        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}