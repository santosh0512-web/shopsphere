
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Orders - ShopSphere</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: left;
        }
        th {
            background: #eeeeee;
        }
    </style>
</head>
<body>

<h1>My Orders</h1>

<c:choose>
    <c:when test="${empty orders}">
        <p>You have not placed any orders yet.</p>
        <a href="${pageContext.request.contextPath}/products">Browse products</a>
    </c:when>

    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Date</th>
                    <th>Total Amount</th>
                    <th>Payment Method</th>
                    <th>Payment Status</th>
                    <th>Order Status</th>
                    <th>Details</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td><c:out value="${order.orderId}"/></td>
                        <td><c:out value="${order.createdAt}"/></td>
                        <td>₹<c:out value="${order.totalAmount}"/></td>
                        <td><c:out value="${order.paymentMethod}"/></td>
                        <td><c:out value="${order.paymentStatus}"/></td>
                        <td><c:out value="${order.orderStatus}"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/order-confirmation?id=${order.orderId}">
                                View order
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/products">Continue shopping</a>

</body>
</html>
