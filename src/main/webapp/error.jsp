<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="header.jsp" />

<body class="bg-light">
    <div class="container text-center mt-5">
        <h1 class="text-danger">Oops! Something went wrong.</h1>
        <p class="lead">We encountered an unexpected error.</p>

        <c:if test="${not empty exception}">
            <div class="alert alert-warning mt-4">
                <strong>Error:</strong> ${exception.message}
            </div>
        </c:if>

        <a href="index.jsp" class="btn btn-outline-primary mt-3">Return to Home</a>
    </div>
    
<c:import url="footer.jsp" />
