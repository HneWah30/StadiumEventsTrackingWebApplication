<%@ page import="model.Event" %>
<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Header -->
<c:import url="header.jsp" />

<!-- Page Heading -->
<section class="container my-5">
  <h2 class="text-center mb-4">Interactive Event Calendar</h2>
  <!-- User Instruction Message -->
  <div class="alert alert-info text-center" role="alert">
    Click on any event on the calendar to view details and purchase tickets. 
    Don't forget to login before attempting to purchase tickets.
  </div>
  
  <div id="calendar"></div>

  <!-- Purchase Modal -->
  <div class="modal fade" id="purchaseModal" tabindex="-1" aria-labelledby="purchaseModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <form action="ConfirmReservationServlet" method="post" class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="purchaseModalLabel">Purchase Ticket</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" name="eventId" id="modalEventId">
          <p><strong>Event:</strong> <span id="modalEventTitle"></span></p>
          <p><strong>Date:</strong> <span id="modalEventDate"></span></p>
          <p><strong>Description:</strong> <span id="modalEventDescription"></span></p>
          <p><strong>Tickets Remaining:</strong> <span id="modalTicketsRemaining"></span></p>
          <div class="mb-3">
            <label for="quantity" class="form-label">Number of Tickets</label>
            <input type="number" name="quantity" class="form-control" id="modalTicketQty" min="1" required>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-success" id="confirmPurchaseBtn">Confirm Purchase</button>
        </div>
      </form>
    </div>
  </div>
</section>

<!-- FullCalendar CSS & JS -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.8/index.global.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.8/index.global.min.js"></script>

<!-- Initialize FullCalendar -->
<script>
  document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay'
      },
      eventClick: function(info) {
        const event = info.event;
        const tickets = event.extendedProps.ticketsAvailable;

        // Populate modal
        document.getElementById('modalEventId').value = event.id;
        document.getElementById('modalEventTitle').textContent = event.title;
        document.getElementById('modalEventDate').textContent = event.start.toISOString().slice(0, 10);
        document.getElementById('modalEventDescription').textContent = event.extendedProps.description || 'No description.';
        document.getElementById('modalTicketsRemaining').textContent = tickets;

        // Disable form if no tickets
        const confirmBtn = document.getElementById('confirmPurchaseBtn');
        const ticketQty = document.getElementById('modalTicketQty');
        if (tickets <= 0) {
          confirmBtn.disabled = true;
          ticketQty.disabled = true;
        } else {
          confirmBtn.disabled = false;
          ticketQty.disabled = false;
          ticketQty.max = tickets;
        }

        // Show modal
        new bootstrap.Modal(document.getElementById('purchaseModal')).show();
      },
      events: [
        <c:forEach var="event" items="${events}" varStatus="loop">
        {
          id: '${event.id}',
          title: '${event.name}',
          start: '<fmt:formatDate value="${event.dateTime}" pattern="yyyy-MM-dd" />',
          description: '${event.description}',
          ticketsAvailable: ${event.ticketsAvailable},
          <c:choose>
            <c:when test="${event.ticketsAvailable == 0}">
              color: 'gray',
              classNames: ['sold-out'],
            </c:when>
            <c:otherwise>
              color: '#28a745',  // Default color for available events
            </c:otherwise>
          </c:choose>
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
      ]
    });

    calendar.render();
  });
</script>

<!-- Footer -->
<c:import url="footer.jsp" />
