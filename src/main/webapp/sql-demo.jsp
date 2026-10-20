<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopsphere.dao.ProductDAO" %>
<%@ page import="com.shopsphere.model.Product" %>

<%
    List<Product> products = new ProductDAO().getActiveProducts();
    request.setAttribute("products", products);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ShopSphere SQL Demo</title>
</head>
<body>
    <h2>Product Query Demo</h2>
    <p>The product DAO fetches active products using the configured database connection.</p>

    <table border="1" cellpadding="8">
        <tr>
            <th>ID</th>
            <th>Product</th>
            <th>Price</th>
        </tr>

        <c:forEach var="p" items="${products}">
            <tr>
                <td><c:out value="${p.productId}"/></td>
                <td><c:out value="${p.name}"/></td>
                <td><c:out value="${p.price}"/></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
