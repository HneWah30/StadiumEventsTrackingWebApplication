<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="header.jsp" />

<div class="container mt-5">
    <h2>Login</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="LoginServlet" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="email">Email:</label>
            <input type="email" name="email" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="password">Password:</label>
            <input type="password" name="password" class="form-control" required>
        </div>

        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" name="rememberMe" id="rememberMe">
            <label class="form-check-label" for="rememberMe">
                Remember Me
            </label>
        </div>

        <button type="submit" class="btn btn-primary">Login</button>
    </form>

    <div class="mt-3">
        <p><a href="forgotPassword.jsp">Forgot your password?</a></p>
        <p>Don't have an account? <a href="register.jsp">Register here</a>.</p>
    </div>
</div>

<c:import url="footer.jsp" />
