<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Header -->
<c:import url="header.jsp" />

<!-- Confirmation Section -->
<section class="container my-5">
  <h2 class="text-center mb-4">Reservation Confirmation</h2>

  <!-- Success Message -->
  <c:if test="${not empty message}">
    <div class="alert alert-success text-center">
      <strong>${message}</strong>
    </div>
  </c:if>

  <!-- Error Message -->
  <c:if test="${not empty error}">
    <div class="alert alert-danger text-center">
      <strong>${error}</strong>
    </div>
  </c:if>

  <!-- Reservation Details & Print Button -->
  <c:if test="${not empty reservationId}">
    <div id="receipt" class="reservation-details border rounded p-4 bg-light">
      <p class="text-center">
        <strong>Your Reservation ID:</strong> <span class="reservation-id">${reservationId}</span>
      </p>
      <p class="text-center">
        <strong>Thank you, ${userName}!</strong>
      </p>
      <p class="text-center">
        You’ve successfully reserved <strong>${quantity}</strong> ticket(s) for:
      </p>
      <h4 class="text-center">${event.name}</h4>
      <p class="text-center">
        <strong>Event Date:</strong> <fmt:formatDate value="${event.dateTime}" pattern="MMMM dd, yyyy"/>
      </p>
      <p class="text-center">
        <strong>Description:</strong> ${event.description}
      </p>
      <p class="text-center">
        <strong>Tickets Remaining:</strong> ${event.ticketsAvailable - quantity}
      </p>

      <!-- Print Button -->
      <div class="text-center mt-4">
        <button onclick="window.print()" class="btn btn-outline-dark">Print Receipt</button>
      </div>
    </div>
  </c:if>

  <!-- Links to My Reservations and Back to Events -->
  <div class="text-center mt-4">
    <a href="MyReservationServlet" class="btn btn-primary">View My Reservations</a>
    <a href="EventServlet" class="btn btn-secondary">Back to Events</a>
  </div>
</section>

<!-- Footer -->
<c:import url="footer.jsp" />
