<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.user.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    User user = (User) session.getAttribute("user");
    String username = (user != null) ? user.getUsername() : null;
    String role = (user != null && user.getRole() != null) ? user.getRole().name().toLowerCase() : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Stadium Events Management</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom CSS -->
    <link rel="stylesheet" href="styles/styles.css">
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.jsp">Stadium Events</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="EventServlet">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="EventServlet?view=calendar">View Calendar</a></li>
                <li class="nav-item"><a class="nav-link" href="adminDashboard.jsp">Management Events</a></li>
                <li class="nav-item"><a class="nav-link" href="MyReservationServlet">My Reservations</a></li>
                

                <% if (username == null) { %>
                    <li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
                <% } else { %>
                    <li class="nav-item">
                        <span class="navbar-text text-white me-2">Welcome to Stadium Event management, <%= username %></span>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="LogoutServlet">Logout</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
