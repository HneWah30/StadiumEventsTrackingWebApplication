<%-- 
    Document   : accessDenied
    Created on : Apr 15, 2025, 10:02:26 PM
    Author     : ezeki
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Event" %>

<c:import url="header.jsp" />
<!DOCTYPE html>
<div class="container mt-5 text-center">
    <h2>Access Denied</h2>
    <p>You do not have permission to access this page.</p>
    <a href="index.jsp" class="btn btn-primary mt-3">Back to Home</a>
</div>
<c:import url="footer.jsp" />