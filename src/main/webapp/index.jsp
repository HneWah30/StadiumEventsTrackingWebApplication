<%@ page import="model.user.User" %>
<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Header Section -->
<c:import url="header.jsp" />

<!-- Logo Section -->
<section class="text-center my-4">
    <img src="images/logo.jpg" alt="Stadium Event Management Logo" class="img-fluid" width="200">
</section>

<!-- Page Content -->
<section class="text-center my-5">
    <h1 class="display-4">Stadium Event Management</h1>
    <div class="alert alert-info text-center" role="alert">
        Efficiently coordinate and oversee all stadium events with a dynamic and centralized platform.
        Access comprehensive listings of upcoming events, complete with filtering and management 
        capabilities to ensure streamlined operations and enhanced attendee engagement.
    </div>
    <c:if test="${not empty sessionScope.user}">
        <p class="mt-3"><strong>Welcome, ${sessionScope.user.username}!</strong></p>
    </c:if>
</section>

<!-- Featured Events Section with Images -->
<section class="container">
    <h2 class="text-center mb-4">Top Featured Events</h2>
    <div class="row">
        <div class="col-md-4">
            <div class="card">
                <img src="images/basketball.jpg" class="card-img-top" alt="Basketball Game">
                <div class="card-body">
                    <h5 class="card-title">Basketball Game</h5>
                    <p class="card-text">Exciting basketball game between the top teams.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card">
                <img src="images/soccer.jpg" class="card-img-top" alt="Soccer Game">
                <div class="card-body">
                    <h5 class="card-title">Soccer Game</h5>
                    <p class="card-text">The final match between the best soccer teams.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card">
                <img src="images/music-concert.jpg" class="card-img-top" alt="Music Concert">
                <div class="card-body">
                    <h5 class="card-title">Gospel Music Concert</h5>
                    <p class="card-text">An evening of beautiful Christian music performances.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Event Filters Section -->
<section>
    <h2>Upcoming Events</h2>
    <form action="EventServlet" method="get">
        <label for="eventType">Filter by Event Type:</label>
        <select id="eventType" name="eventType">
            <option value="">All</option>
            <option value="Basketball">Basketball</option>
            <option value="Hockey">Hockey</option>
            <option value="Concert">Concert</option>
            <option value="Football">Football</option>
            <option value="Soccer">Soccer</option>
        </select>

        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" name="startDate">

        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" name="endDate">

        <button type="submit">Filter</button>
    </form>
</section>

<!-- Dynamic Event Listings -->
<section>
    <h2>Upcoming Events</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Date</th>
                <th>Type</th>
                <th>Description</th>
                <th>Tickets Available</th>
                <th>Reserve</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty events}">
                    <c:forEach var="event" items="${events}">
                        <tr>
                            <td>${event.id}</td>
                            <td>${event.name}</td>
                            <td><fmt:formatDate value="${event.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            <td>${event.type}</td>
                            <td>${event.description}</td>
                            <td>${event.ticketsAvailable}</td>
                            <td>
                                <c:if test="${not empty sessionScope.user}">
                                    <form action="MyReservationServlet" method="post" style="display:flex; align-items:center; gap:5px;">
                                        <input type="hidden" name="eventId" value="${event.id}" />
                                        <input type="number" name="quantity" min="1" max="${event.ticketsAvailable}" value="1" required />
                                        <button type="submit" class="btn btn-success btn-sm">Reserve</button>
                                    </form>
                                </c:if>
                                <c:if test="${empty sessionScope.user}">
                                    <em>Login to reserve</em>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7">No events found.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</section>

<!-- Favorite Events Section (Only for Logged-in Users) -->
<c:if test="${not empty sessionScope.user}">
    <section class="container mt-5">
        <h2>Mark Events as Favorite</h2>
        <c:forEach var="event" items="${events}">
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${event.name}</h5>
                    <p class="card-text">
                        <fmt:formatDate value="${event.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" /> | ${event.type}
                    </p>
                    <p class="card-text">${event.description}</p>

                    <form action="EventServlet" method="post" style="display:inline;">
                        <input type="hidden" name="eventId" value="${event.id}" />
                        <c:choose>
                            <c:when test="${fn:contains(favoriteEventIds, event.id)}">
                                <button type="submit" name="action" value="unfavorite" class="btn btn-danger">💔 Unfavorite</button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" name="action" value="favorite" class="btn btn-outline-primary">❤️ Favorite</button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </div>
            </div>
        </c:forEach>
    </section>
</c:if>

<!-- Footer Section -->
<c:import url="footer.jsp" />
