
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout - ShopSphere</title>
</head>
<body>
<h1>Checkout</h1>

<c:if test="${not empty error}">
    <p style="color:red;"><c:out value="${error}"/></p>
</c:if>

<h2>Order Summary</h2>

<c:choose>
    <c:when test="${empty items}">
        <p>Your cart is empty or the products are unavailable.</p>
        <a href="${pageContext.request.contextPath}/products">Browse products</a>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="8">
            <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Subtotal</th>
            </tr>

            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.name}"/></td>
                    <td><c:out value="${item.quantity}"/></td>
                    <td>₹<c:out value="${item.price}"/></td>
                    <td>₹<c:out value="${item.lineTotal}"/></td>
                </tr>
            </c:forEach>
        </table>

        <h3>Total: ₹<c:out value="${total}"/></h3>

        <h2>Select Delivery Address</h2>

        <c:choose>
            <c:when test="${empty addresses}">
                <p>Please add an address before checkout.</p>
                <a href="${pageContext.request.contextPath}/addresses">
                    Add address
                </a>
            </c:when>
            <c:otherwise>
                <form method="post"
                      action="${pageContext.request.contextPath}/checkout">

                    <c:forEach var="address" items="${addresses}">
                        <label>
                            <input type="radio" name="addressId"
                                   value="${address.addressId}" required>
                            <c:out value="${address.addressType}"/>:
                            <c:out value="${address.addressLine}"/>,
                            <c:out value="${address.city}"/>,
                            <c:out value="${address.state}"/> -
                            <c:out value="${address.pincode}"/>
                        </label>
                        <br>
                    </c:forEach>

                    <h2>Payment Method</h2>

                    <label>
                        <input type="radio" name="paymentMethod"
                               value="COD" required>
                        Cash on Delivery (Demo)
                    </label>
                    <br>

                    <label>
                        <input type="radio" name="paymentMethod"
                               value="MOCK_ONLINE" required>
                        Mock Online Payment (No real money charged)
                    </label>
                    <br><br>

                    <button type="submit">Place Order</button>
                </form>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<br>
<a href="${pageContext.request.contextPath}/cart">Back to Cart</a>
</body>
</html>
