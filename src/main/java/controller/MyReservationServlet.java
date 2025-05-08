package controller;

import dao.DatabaseConnection;
import dao.EventDAO;
import dao.ReservationDAO;
import model.Reservation;
import model.user.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "MyReservationServlet", urlPatterns = {"/MyReservationServlet"})
public class MyReservationServlet extends HttpServlet {

    // Handle creating reservation
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getUserId();
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            ReservationDAO reservationDAO = new ReservationDAO(conn);

            int availableTickets = EventDAO.getTicketsAvailable(eventId);
            if (quantity > availableTickets) {
                request.setAttribute("error", "Not enough tickets available.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            boolean success = reservationDAO.reserveTickets(userId, eventId, quantity);
            if (success) {
                //EventDAO.reduceTickets(eventId, quantity);
                response.sendRedirect("MyReservationServlet"); // Refresh to show updated list
            } else {
                request.setAttribute("error", "Reservation failed.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Handle viewing reservations
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            ReservationDAO reservationDAO = new ReservationDAO(conn);
            List<Reservation> reservations = reservationDAO.getUserReservations(user.getUserId());

            request.setAttribute("reservations", reservations);
            request.getRequestDispatcher("myReservations.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load reservations.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
