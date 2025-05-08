package controller;

import dao.EventDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Event;
import model.user.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageEventsServlet", urlPatterns = {"/ManageEventsServlet"})
public class ManageEventsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in and an admin
        if (user == null || !"ADMIN".equals(user.getRoleName())) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<Event> events = EventDAO.getAllEvents();
        request.setAttribute("events", events);

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminDashboard.jsp");
        dispatcher.forward(request, response);
    }
}
