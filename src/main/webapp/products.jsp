
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopsphere.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere | Products</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            color: #222;
        }

        header {
            background: #222;
            color: white;
            padding: 20px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
        }

        header h1 {
            margin: 0;
            font-size: 26px;
        }

        header a {
            color: white;
            text-decoration: none;
            margin-left: 15px;
        }

        .container {
            max-width: 1200px;
            margin: auto;
            padding: 30px 20px;
        }

        .container > h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        .message {
            text-align: center;
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 6px;
        }

        .success {
            background: #d8f3dc;
            color: #1b4332;
        }

        .error {
            background: #ffe0e0;
            color: #9b2226;
        }

        .products {
            display: grid;
            grid-template-columns:
                repeat(auto-fit, minmax(230px, 1fr));
            gap: 22px;
        }

        .card {
            background: white;
            padding: 22px;
            border-radius: 10px;
            box-shadow: 0 2px 10px #00000012;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .card h3 {
            margin: 0;
            font-size: 21px;
        }

        .brand {
            color: #666;
            margin: 0;
        }

        .description {
            color: #444;
            line-height: 1.5;
        }

        .price {
            color: #087443;
            font-size: 22px;
            font-weight: bold;
            margin: 5px 0;
        }

        .stock {
            color: #555;
            margin: 0;
        }

        .add-form {
            margin-top: auto;
            padding-top: 10px;
        }

        button {
            width: 100%;
            background: #222;
            color: white;
            border: none;
            padding: 12px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
        }

        button:hover {
            background: #444;
        }

        button:disabled {
            background: #999;
            cursor: not-allowed;
        }

        footer {
            text-align: center;
            padding: 20px;
            color: #666;
        }
    </style>
</head>

<body>

<header>
    <h1>ShopSphere</h1>
    <a href="<%= request.getContextPath() %>/cart">My Cart</a>


    <nav>
        <a href="<%= request.getContextPath() %>/products">
            Products
        </a>

        <a href="<%= request.getContextPath() %>/logout"
            onclick="return confirm('Are you sure you want to logout?');">
            Logout
        </a>
    </nav>

</header>

<div class="container">

    <h2>Explore Our Products</h2>

    <%
        String added = request.getParameter("added");

        if ("true".equals(added)) {
    %>
        <div class="message success">
            Product added to your cart successfully!
        </div>
    <%
        } else if ("false".equals(added)) {
    %>
        <div class="message error">
            Unable to add product. Please try again.
        </div>
    <%
        }
    %>

    <div class="products">

        <%
            List<Product> products =
                (List<Product>) request.getAttribute("products");

            if (products != null && !products.isEmpty()) {

                for (Product p : products) {
        %>

            <div class="card">

                <h3><%= p.getName() %></h3>

                <p class="brand">
                    Brand: <%= p.getBrand() %>
                </p>

                <p class="description">
                    <%= p.getDescription() == null
                        ? "Product details coming soon."
                        : p.getDescription() %>
                </p>

                <p class="price">
                    ₹<%= p.getPrice() %>
                </p>

                <p class="stock">
                    Available stock: <%= p.getStock() %>
                </p>

                <form class="add-form"
                      action="<%= request.getContextPath() %>/add-to-cart"
                      method="post">

                    <input type="hidden"
                           name="productId"
                           value="<%= p.getProductId() %>">

                    <button type="submit"
                            <%= p.getStock() <= 0 ? "disabled" : "" %>>
                        <%= p.getStock() <= 0
                            ? "Out of Stock"
                            : "Add to Cart" %>
                    </button>

                </form>

            </div>

        <%
                }

            } else {
        %>

            <p>No products available right now.</p>

        <%
            }
        %>

    </div>

</div>

<footer>
    ShopSphere - Your Online Shopping Store
</footer>

</body>
</html>
