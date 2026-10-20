
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Delivery Addresses - ShopSphere</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f5f5f5;
        }

        .container {
            max-width: 750px;
            margin: auto;
        }

        .card {
            background: white;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 8px;
        }

        input, select, button {
            display: block;
            width: 100%;
            box-sizing: border-box;
            padding: 10px;
            margin: 10px 0;
        }

        button {
            background: #222;
            color: white;
            border: none;
            cursor: pointer;
        }

        .message {
            color: green;
        }
    </style>
</head>
<body>
<div class="container">

    <h1>My Delivery Addresses</h1>

    <%
        String saved = request.getParameter("saved");
        if ("true".equals(saved)) {
    %>
        <p class="message">Address saved successfully.</p>
    <%
        } else if ("false".equals(saved)) {
    %>
        <p style="color:red;">Unable to save address. Please try again.</p>
    <%
        }
    %>

    <div class="card">
        <h2>Saved Addresses</h2>

        <c:choose>
            <c:when test="${empty addresses}">
                <p>No saved addresses yet.</p>
            </c:when>

            <c:otherwise>
                <c:forEach var="address" items="${addresses}">
                    <div class="card">
                        <strong>${address.addressType}</strong>
                        <p>${address.addressLine}</p>
                        <p>${address.city}, ${address.state} - ${address.pincode}</p>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="card">
        <h2>Add New Address</h2>

        <form action="${pageContext.request.contextPath}/addresses"
              method="post">

            <label>Address Line</label>
            <input type="text" name="addressLine"
                   maxlength="255" required>

            <label>City</label>
            <input type="text" name="city"
                   maxlength="100" required>

            <label>State</label>
            <input type="text" name="state"
                   maxlength="100" required>

            <label>Pincode</label>
            <input type="text" name="pincode"
                   pattern="[0-9]{6}" maxlength="6"
                   title="Enter a valid 6-digit pincode" required>

            <label>Address Type</label>
            <select name="addressType" required>
                <option value="HOME">Home</option>
                <option value="WORK">Work</option>
                <option value="OTHER">Other</option>
            </select>

            <button type="submit">Save Address</button>
        </form>
    </div>

    <p>
        <a href="${pageContext.request.contextPath}/cart">
            Back to Cart
        </a>
    </p>

</div>
</body>
</html>
