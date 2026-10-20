
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopsphere.model.Cart" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    List<Cart> cartItems =
            (List<Cart>) request.getAttribute("cartItems");

    DecimalFormat money = new DecimalFormat("#,##0.00");
    double total = 0;
    String context = request.getContextPath();

    String updated = request.getParameter("updated");
    String removed = request.getParameter("removed");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere | My Cart</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 30px 16px;
            font-family: Arial, sans-serif;
            background: #202020;
            color: #f5f5f5;
        }

        .container {
            max-width: 900px;
            margin: auto;
        }

        h1 {
            text-align: center;
        }

        .item {
            background: #111;
            border-radius: 10px;
            padding: 20px;
            margin: 16px 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        .price {
            color: #55c982;
        }

        a {
            color: #8bc5ff;
        }

        .empty {
            text-align: center;
            padding: 30px;
            background: #111;
            border-radius: 10px;
        }

        .total {
            text-align: right;
            font-size: 20px;
            font-weight: bold;
        }

        .back {
            margin-top: 25px;
        }

        input[type="number"] {
            width: 75px;
            padding: 8px;
            border-radius: 5px;
        }

        button {
            padding: 9px 12px;
            border: 0;
            border-radius: 5px;
            cursor: pointer;
            margin: 4px 0;
        }

        .update-btn {
            background: #55c982;
            color: #111;
        }

        .remove-btn {
            background: #b83232;
            color: white;
        }

        .message {
            padding: 12px;
            margin: 15px 0;
            background: #123c29;
            border-radius: 6px;
        }

        .error {
            background: #4a2020;
        }

        .checkout-btn {
            display: inline-block;
            padding: 12px 22px;
            background: #55c982;
            color: #111;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            margin-top: 10px;
        }

        .checkout-btn:hover {
            background: #72dfa0;
        }

        .cart-actions {
            text-align: right;
            margin-top: 20px;
        }
    </style>
</head>

<body>
<div class="container">

    <h1>My Shopping Cart</h1>

    <% if ("true".equals(updated)) { %>
        <p class="message">Quantity updated successfully.</p>
    <% } else if ("false".equals(updated)) { %>
        <p class="message error">
            Quantity update failed. Check the available stock.
        </p>
    <% } %>

    <% if ("true".equals(removed)) { %>
        <p class="message">Product removed from cart.</p>
    <% } else if ("false".equals(removed)) { %>
        <p class="message error">Product could not be removed.</p>
    <% } %>

    <% if (cartItems == null || cartItems.isEmpty()) { %>

        <div class="empty">
            <p>Your cart is empty.</p>
            <a href="<%= context %>/products">Browse Products</a>
        </div>

    <% } else { %>

        <% for (Cart item : cartItems) {
            double subtotal = item.getPrice() * item.getQuantity();
            total += subtotal;
        %>

            <div class="item">
                <div>
                    <h3><%= item.getProductName() %></h3>

                    <p>
                        Price: ₹<%= money.format(item.getPrice()) %>
                    </p>

                    <p class="price">
                        Subtotal: ₹<%= money.format(subtotal) %>
                    </p>
                </div>

                <div>
                    <form action="<%= context %>/update-cart"
                          method="post">

                        <input type="hidden"
                               name="productId"
                               value="<%= item.getProductId() %>">

                        <label>Quantity:</label>

                        <input type="number"
                               name="quantity"
                               min="1"
                               value="<%= item.getQuantity() %>"
                               required>

                        <button class="update-btn" type="submit">
                            Update
                        </button>
                    </form>

                    <form action="<%= context %>/remove-from-cart"
                          method="post"
                          onsubmit="return confirm('Remove this product from your cart?');">

                        <input type="hidden"
                               name="productId"
                               value="<%= item.getProductId() %>">

                        <button class="remove-btn" type="submit">
                            Remove
                        </button>
                    </form>
                </div>
            </div>

        <% } %>

        <p class="total">
            Total: ₹<%= money.format(total) %>
        </p>

        <div class="cart-actions">
            <a class="checkout-btn" href="<%= context %>/checkout">
                Proceed to Checkout →
            </a>
        </div>

    <% } %>

    <div class="back">
        <a href="<%= context %>/products">← Back to Products</a>
    </div>

</div>
</body>
</html>
