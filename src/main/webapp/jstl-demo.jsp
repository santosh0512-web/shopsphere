
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere JSTL Demo</title>
</head>
<body>
    <h2>JSTL Core Demonstration</h2>

    <c:set var="storeName" value="ShopSphere" />
    <p>Store: ${storeName}</p>

    <c:set var="stock" value="10" />

    <c:if test="${stock > 0}">
        <p>Product is available.</p>
    </c:if>

    <h3>Products</h3>
    <c:forEach var="product" items="${['Headphones', 'Keyboard', 'Mouse']}">
        <p>${product}</p>
    </c:forEach>
</body>
</html>
