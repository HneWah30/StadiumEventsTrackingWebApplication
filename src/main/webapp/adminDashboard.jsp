<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.user.User" %>

<%
    // Retrieve the User object from the session
    User user = (User) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role = user.getRole().name().toLowerCase(); // Get user's role

    if (!"admin".equals(role)) {
        response.sendRedirect("accessDenied.jsp");
        return;
    }
%>

<c:import url="header.jsp" />

<div class="container mt-5">
    <h1 class="mb-4">Admin Dashboard</h1>

    <!-- Success or error messages -->
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Add New Event Form -->
    <h2>Add New Event</h2>
    <form action="EventServlet" method="post" class="needs-validation" novalidate>
        <input type="hidden" name="action" value="addEvent" />

        <div class="mb-3">
            <label for="name" class="form-label">Event Name:</label>
            <input type="text" id="name" name="name" class="form-control" required>
            <div class="invalid-feedback">Please enter an event name.</div>
        </div>

        <div class="mb-3">
            <label for="dateTime" class="form-label">Date and Time:</label>
            <input type="text" id="dateTime" name="dateTime" placeholder="YYYY-MM-DD HH:mm:ss" class="form-control" required>
            <div class="invalid-feedback">Please provide the event's date and time.</div>
        </div>

        <div class="mb-3">
            <label for="type" class="form-label">Event Type:</label>
            <input type="text" id="type" name="type" class="form-control" required>
            <div class="invalid-feedback">Please enter an event type.</div>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description:</label>
            <textarea id="description" name="description" class="form-control" rows="4" required></textarea>
            <div class="invalid-feedback">Please provide a description.</div>
        </div>

        <div class="mb-3">
            <label for="ticketsAvailable" class="form-label">Tickets Available:</label>
            <input type="number" id="ticketsAvailable" name="ticketsAvailable" class="form-control" required>
            <div class="invalid-feedback">Please enter the number of tickets available.</div>
        </div>

        <button type="submit" class="btn btn-primary">Add Event</button>
    </form>
    
    <c:forEach var="r" items="${reservations}">
    <tr>
        <td>${r.eventName}</td>
        <td>${r.quantity}</td>
        <td>
            <span class="badge 
                ${r.status == 'CONFIRMED' ? 'bg-success' : 
                  r.status == 'PENDING' ? 'bg-warning text-dark' : 
                  'bg-danger'}">
                ${r.status}
            </span>
        </td>
        <td><fmt:formatDate value="${r.reservationTime}" pattern="MMM dd, yyyy hh:mm a" /></td>

        <c:if test="${isAdmin && r.status == 'PENDING'}">
            <td>
                <form action="ConfirmReservationServlet" method="post">
                    <input type="hidden" name="reservationId" value="${r.id}" />
                    <button type="submit" class="btn btn-sm btn-success">Confirm</button>
                </form>
            </td>
        </c:if>
    </tr>
</c:forEach>

<c:import url="footer.jsp" />

    