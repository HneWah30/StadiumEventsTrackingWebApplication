package controller;

import dao.UserDAO;
import model.user.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.info("Login attempt for email: " + email);

        try {
            // Get user by email
            User user = UserDAO.getUserByEmail(email);

            if (user == null) {
                logger.warning("Login failed: Email not found - " + email);
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Verify password
            if (user.verifyPassword(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRoleName());

                logger.info("Login successful for: " + email + ", role: " + user.getRoleName());

                // Redirect based on role
                if ("ADMIN".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect("adminDashboard.jsp");
                } else {
                    response.sendRedirect("index.jsp");
                }
            } else {
                logger.warning("Login failed: Incorrect password for email - " + email);
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.severe("❌ Login error: " + e.getMessage());
            e.printStackTrace(); // optional: remove in production

            // Friendly message for users
            request.setAttribute("error", "An unexpected error occurred while processing your login. Please try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
