
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation - ShopSphere</title>
</head>
<body>
    <h1>Order placed successfully!</h1>

    <p>Order ID: <strong><c:out value="${orderId}"/></strong></p>
    <p>Total: ₹<c:out value="${total}"/></p>
    <p>Payment method: <c:out value="${paymentMethod}"/></p>
    <p>Payment status: <c:out value="${paymentStatus}"/></p>
    <p>Order status: <c:out value="${orderStatus}"/></p>
    <p>Created at: <c:out value="${createdAt}"/></p>

    <p>This is a demo checkout. No real online payment has been taken.</p>

    <a href="${pageContext.request.contextPath}/products">Continue shopping</a>
</body>
</html>
