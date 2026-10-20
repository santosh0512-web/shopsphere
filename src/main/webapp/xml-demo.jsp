<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>

<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere XML Tags Demo</title>
</head>
<body>
    <h2>JSP XML Tags Demonstration</h2>

    <x:parse var="catalog">
        <products>
            <product>
                <name>Headphones</name>
                <price>1499</price>
            </product>
            <product>
                <name>Keyboard</name>
                <price>899</price>
            </product>
        </products>
    </x:parse>

    <p>First Product: <x:out select="$catalog/products/product[1]/name"/></p>
    <p>First Product Price: ₹<x:out select="$catalog/products/product[1]/price"/></p>
    <p>Second Product: <x:out select="$catalog/products/product[2]/name"/></p>
    <p>Second Product Price: ₹<x:out select="$catalog/products/product[2]/price"/></p>

</body>
</html>