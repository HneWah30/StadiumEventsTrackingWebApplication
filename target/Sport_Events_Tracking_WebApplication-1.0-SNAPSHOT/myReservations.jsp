<%@ page import="java.util.List" %>
<%@ page import="model.Reservation" %>
<%@ page import="model.Event" %>
<%@ page import="model.user.User" %>
<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:import url="header.jsp" />

<div class="container my-5">
    <h2 class="mb-4 text-center">My Reservations</h2>

    <c:if test="${empty reservations}">
        <div class="alert alert-info">You have no reservations yet.</div>
    </c:if>

    <c:if test="${not empty reservations}">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>Event</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Reserved At</th>
                </tr>
            </thead>
            <tbody>
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
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<c:import url="footer.jsp" />
