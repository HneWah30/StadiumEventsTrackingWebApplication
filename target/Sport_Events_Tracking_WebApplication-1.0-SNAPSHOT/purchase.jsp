<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Event" %>

<!-- Header Section -->
<c:import url="header.jsp" />

<%
    String eventId = request.getParameter("eventId");
%>

<div class="container my-5">
    <!-- Display Messages -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Ticket Purchase Card -->
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Purchase Ticket</h3>
        </div>
        <div class="card-body">
            <p class="lead">You're buying a ticket for <strong>Event ID: <%= eventId %></strong></p>

            <form action="PurchaseServlet" method="post" class="mt-4">
                <input type="hidden" name="eventId" value="<%= eventId %>">

                <div class="mb-3">
                    <label for="quantity" class="form-label">Number of Tickets:</label>
                    <input type="number" name="quantity" id="quantity" class="form-control" min="1" required>
                </div>

                <button type="submit" class="btn btn-success w-100">Buy Now</button>
            </form>
        </div>
    </div>
</div>

<!-- Footer Section -->
<c:import url="footer.jsp" />
